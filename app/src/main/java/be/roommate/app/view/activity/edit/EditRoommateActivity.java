package be.roommate.app.view.activity.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import be.roommate.app.R;
import be.roommate.app.model.dto.RoommateDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.FieldEditText;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 11/11/14.
 */
public class EditRoommateActivity extends AbstractEditActivity<RoommateDTO> {

    private boolean edit;
    private RoommateDTO roommateDTO;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //recover roommateDTO id
        Intent i = getIntent();

        //create roommateDTO
        if (i.getLongExtra("roommateId", -1L) != -1L) {
            roommateDTO = Storage.getRoommate(i.getLongExtra("roommateId", -1L));
            edit = true;
        } else {
            roommateDTO = new RoommateDTO();
        }

        //build field
        try {

            //create field
            form = new Form(this, roommateDTO,
                    new Field.FieldProperties(RoommateDTO.class.getDeclaredField("name"), R.string.g_name,
                            InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PERSON_NAME|InputType.TYPE_TEXT_FLAG_CAP_WORDS),
                    new Field.FieldProperties(RoommateDTO.class.getDeclaredField("nameAbrv"), R.string.g_name_abrv,
                            InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_WORDS),
                    new Field.FieldProperties(RoommateDTO.class.getDeclaredField("email"), R.string.g_email,
                            InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
            form.intialize();
            final Field fieldName = form.getField(R.string.g_name);
            final Field fieldNameAbrv = form.getField(R.string.g_name_abrv);

            ((FieldEditText) fieldName.getFieldProperties().getInputView()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String name = (String) fieldName.getValue();

                    if (name == null || name.length() == 0) {
                        fieldNameAbrv.setValue(null);
                    } else if (name.length() < 3) {
                        fieldNameAbrv.setValue(name);
                    } else {
                        fieldNameAbrv.setValue(name.substring(0, 3));
                    }
                }
            });


            //and insert field into view
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException | NoSuchFieldException e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }

    }

    @Override
    protected int getActivityTitle() {
        if (edit) {
            return R.string.edit_roommate_edit;
        } else {
            return R.string.edit_roommate_create;
        }
    }

    @Override
    protected WebClient<RoommateDTO> getWebClient() {
        WebClient<RoommateDTO> webClient;
        if (edit) {
            //create request
            webClient = new WebClient<>(RequestEnum.ROOMMATE_EDIT,
                    roommateDTO,
                    RoommateDTO.class);
            webClient.setParams("roommateId",roommateDTO.getId()+"");
        } else {
            webClient = new WebClient<>(RequestEnum.ROOMMATE_CREATE,
                    roommateDTO,
                    RoommateDTO.class);
        }
        return webClient;
    }

    @Override
    public void successAction(DTO successDTO) {
        if (edit) {
            //update
            Storage.editRoommate(((RoommateDTO) successDTO));
        } else {
            //add new entity
            Storage.addRoommate(((RoommateDTO) successDTO));
        }
        backToMainActivity();
    }
}
