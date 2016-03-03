package be.roommate.app.view.activity.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;

import be.roommate.app.R;
import be.roommate.app.model.dto.ChoreDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 11/11/14.
 */
public class EditChoreActivity extends AbstractEditActivity<ChoreDTO> {

    //constant
    public static final String CHORE_ID = "CHORE_ID";

    private boolean edit;
    private ChoreDTO choreDTO;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //recover chore id
        Intent i = getIntent();

        //create chore
        if (i.getLongExtra(CHORE_ID, -1L) != -1L) {
            choreDTO = Storage.getChore(i.getLongExtra(CHORE_ID, -1L));
            edit = true;
        } else {
            choreDTO = new ChoreDTO();
        }

        //build field
        try {

            //create field
            try {
                form = new Form(this, choreDTO,
                        new Field.FieldProperties(ChoreDTO.class.getDeclaredField("name"), R.string.g_name,
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES),
                        new Field.FieldProperties(ChoreDTO.class.getDeclaredField("description"), R.string.chore_description,
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE));
                form.intialize();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            //and insert field into view
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }

    }

    @Override
    protected int getActivityTitle() {
        if (edit) {
            return R.string.edit_chore_edit;
        } else {
            return R.string.create_chore_edit;
        }
    }

    @Override
    protected WebClient<ChoreDTO> getWebClient() {
        WebClient<ChoreDTO> webClient;
        if (edit) {
            //create request
            webClient = new WebClient<>(RequestEnum.CHORE_EDIT,
                    choreDTO,
                    ChoreDTO.class);
            webClient.setParams("choreId", choreDTO.getId() + "");
        } else {
            webClient = new WebClient<>(RequestEnum.CHORE_ADD,
                    choreDTO,
                    ChoreDTO.class);
        }
        return webClient;
    }

    @Override
    public void successAction(DTO successDTO) {
        if (edit) {
            //update
            Storage.editChore(((ChoreDTO) successDTO));
        } else {
            //add new entity
            Storage.addChore(((ChoreDTO) successDTO));
        }

        backToMainActivity();
    }

}
