package be.flo.roommateapp.vue.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.CommentDTO;
import be.flo.roommateapp.model.dto.ResultDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.listAdapter.CommentListAdapter;
import be.flo.roommateapp.vue.technical.AbstractActivity;

public class CommentLogActivity extends AbstractActivity implements RequestActionInterface {


    //constant
    public static final String COMMENT_ID = "commentId";
    public static final String COMMENT_TYPE = "commentType";
    public static final String COMMENT_ASSOCIATE_ID = "commentAssociateId";
    public static final String COMMENT_PARENT_ID = "commentParentId";

    private Long associateId;
    private CommentTypeEnum commentType;
    private Dialog loadingDialog;
    private CommentListAdapter adapter;

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {

        super.onStart();

        setContentView(R.layout.activity_comment_log);

        loadingDialog = DialogConstructor.dialogLoading(this);

        Intent i = getIntent();

        if (i.getStringExtra(COMMENT_TYPE) == null) {
            displayErrorMessage("comment type needed");
            //TODO ?
            //backToMainActivity();
        } else {

            commentType = CommentTypeEnum.getByName(i.getStringExtra(COMMENT_TYPE));

            if (commentType.isRequiredId() && i.getLongExtra(COMMENT_ASSOCIATE_ID, -1L) == -1L) {
                displayErrorMessage("comment associate id needed");
                //TODO ?
                //backToMainActivity();
            } else {

                associateId = i.getLongExtra(COMMENT_ASSOCIATE_ID, -1L);

                //recover list
                ListView listView = (ListView) findViewById(R.id.list_insertion);
                listView.setClickable(false);
                listView.setDivider(null);
                listView.setDividerHeight(0);


                adapter = new CommentListAdapter(this);

                //add adapter
                listView.setAdapter(adapter);

                refreshConversation();


                //button
                findViewById(R.id.btn_comment_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save();
                    }
                });

                new ReadRequest();


            }
        }
        ((ListView) findViewById(R.id.list_insertion)).scrollTo(0, ((ListView) findViewById(R.id.list_insertion)).getHeight());
    }


    @Override
    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(this, errorMessage);
    }

    /**
     * save operation
     */
    protected void save() {

        String comment = ((EditText) findViewById(R.id.comment_field)).getText().toString();

        if (comment != null && comment.length() > 0) {

            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setComment(comment);

            WebClient<CommentDTO> webClient = new WebClient<>(this,
                    commentType.requestEnum,
                    commentDTO,
                    associateId,
                    CommentDTO.class);

            //send request
            Request request = new Request(this, webClient);

            //execute request
            request.execute();
        }
    }

    @Override
    public void loadingAction(boolean loading) {


        if (loading) {
            loadingDialog.show();

            // create animation and add to the refresh item
        } else {
            loadingDialog.cancel();
        }
    }

    @Override
    public void successAction(DTO successDTO) {
        switch (commentType) {
            case SHOPPING_ITEM:
                if (Storage.getShoppingItem(associateId).getComments() == null) {
                    Storage.getShoppingItem(associateId).setComments(new ArrayList<CommentDTO>());
                }

                Storage.getShoppingItem(associateId).getComments().add(((CommentDTO) successDTO));
                break;
            case TICKET:
                if (Storage.getTicket(associateId).getComments() == null) {
                    Storage.getTicket(associateId).setComments(new ArrayList<CommentDTO>());
                }
                Storage.getTicket(associateId).getComments().add(((CommentDTO) successDTO));
                break;
            case HOME:
                if (Storage.getHome().getComments() == null) {
                    Storage.getHome().setComments(new ArrayList<CommentDTO>());
                }
                Storage.getHome().getComments().add(((CommentDTO) successDTO));
                break;
            case EDIT:
                Storage.editComment(((CommentDTO) successDTO));
                break;
        }

        refreshConversation();

        //clean field
        ((EditText) findViewById(R.id.comment_field)).setText(null);
    }

    private void refreshConversation() {

        adapter.clear();

        List<CommentDTO> listComments = null;

        switch (commentType) {
            case SHOPPING_ITEM:
                listComments = Storage.getShoppingItem(associateId).getComments();
                Storage.getShoppingItem(associateId).setHasNewComment(false);
                break;
            case TICKET:
                listComments = Storage.getTicket(associateId).getComments();
                Storage.getTicket(associateId).setHasNewComment(false);
                break;
            case HOME:
                listComments = Storage.getHome().getComments();
                Storage.getHome().setHasNewComment(false);
                break;
            case EDIT:
                break;
        }

        if(listComments!=null) {

            //build list
            Collections.sort(listComments);
            List<CommentDTO> finalList = new ArrayList<>();

            for (CommentDTO comment : listComments) {
                finalList.add(comment);
                //TODO manage children
            }

            //adapter
            adapter.addAll(finalList);
        }}

    public static enum CommentTypeEnum {
        SHOPPING_ITEM(RequestEnum.SHOPPING_COMMENT, RequestEnum.SHOPPING_COMMENT_READ, true),
        TICKET(RequestEnum.TICKET_COMMENT, RequestEnum.TICKET_COMMENT_READ, true),
        HOME(RequestEnum.COMMENT_ADD, RequestEnum.COMMENT_READ, false),
        EDIT(RequestEnum.COMMENT_EDIT, null, true);

        private RequestEnum requestEnum;
        private RequestEnum requestEnumRead;
        private boolean requiredId;
        private boolean expectedResultDTO;

        CommentTypeEnum(RequestEnum requestEnum, RequestEnum requestEnumRead, boolean requiredId) {
            this.requestEnum = requestEnum;
            this.requiredId = requiredId;
            this.requestEnumRead = requestEnumRead;
        }

        public boolean isRequiredId() {
            return requiredId;
        }

        public static CommentTypeEnum getByName(String stringExtra) {
            for (CommentTypeEnum commentTypeEnum : values()) {
                if (commentTypeEnum.name().equals(stringExtra)) {
                    return commentTypeEnum;
                }
            }

            return null;
        }

        public boolean isExpectedResultDTO() {
            return expectedResultDTO;
        }

        public RequestEnum getRequestEnumRead() {
            return requestEnumRead;
        }
    }

    public class ReadRequest implements RequestActionInterface {


        public ReadRequest() {


            //build read request
            WebClient<ResultDTO> webClient = new WebClient<>(CommentLogActivity.this, commentType.getRequestEnumRead(), associateId, ResultDTO.class);

            Request request = new Request(this, webClient);

            //execute request
            request.execute();
        }

        @Override
        public void displayErrorMessage(String errorMessage) {

        }

        @Override
        public void loadingAction(boolean loading) {

        }

        @Override
        public void successAction(DTO successDTO) {

        }
    }
}
