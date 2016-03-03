package be.roommate.app.view.activity.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;

import java.util.ArrayList;

import be.roommate.app.R;
import be.roommate.app.model.dto.CommentDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.activity.CommentLogActivity;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 11/11/14.
 */
public class EditCommentActivity extends AbstractEditActivity<CommentDTO> {

    //constant
    public static final String COMMENT_ID = "commentId";
    public static final String COMMENT_TYPE = "commentType";
    public static final String COMMENT_ASSOCIATE_ID = "commentAssociateId";
    public static final String COMMENT_PARENT_ID = "commentParentId";

    private boolean edit;
    private CommentDTO commentDTO;
    private CommentLogActivity.CommentTypeEnum commentType;
    private long associateId;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //recover shopping item id
        Intent i = getIntent();

        //create shoppingItem
        if (i.getLongExtra(COMMENT_ID, -1L) != -1L) {
            commentDTO = Storage.getComment(i.getLongExtra(COMMENT_ID, -1L));
            edit = true;
        } else {
            commentDTO = new CommentDTO();
        }

        if (i.getStringExtra(COMMENT_TYPE) == null) {
            displayErrorMessage("comment type needed");
            backToMainActivity();
        } else {

            commentType = CommentLogActivity.CommentTypeEnum.getByName(i.getStringExtra(COMMENT_TYPE));

            if (commentType.isRequiredId() && i.getLongExtra(COMMENT_ASSOCIATE_ID, -1L) == -1L) {
                displayErrorMessage("comment associate id needed");
                backToMainActivity();
            } else {

                associateId = i.getLongExtra(COMMENT_ASSOCIATE_ID, -1L);


                //build field
                try {

                    //create field
                    try {
                        form = new Form(this, commentDTO,
                                new Field.FieldProperties(CommentDTO.class.getDeclaredField("comment"), R.string.g_comment,
                                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE));
                        form.intialize();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                    //and insert field into view
                    ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
                    insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

                    //TODO add comment parent

                } catch (MyException e) {
                    e.printStackTrace();
                    displayErrorMessage(e.getMessage());
                }
            }
        }

    }

    @Override
    protected int getActivityTitle() {
        return R.string.editComment_title;
    }

    @Override
    protected WebClient getWebClient() {

        WebClient<CommentDTO> webClient;
        //create request
        webClient = new WebClient<>(
                commentType.getRequestEnumRead(),
                commentDTO,
                CommentDTO.class);
        webClient.setParams("commentId", associateId + "");
        return webClient;
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

        backToMainActivity();
    }

}
