package be.roommate.app.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import be.roommate.app.R;
import be.roommate.app.model.dto.ChangePasswordDTO;
import be.roommate.app.model.dto.ResultDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 18/02/15.
 */
public class ChangePasswordDialog extends Dialog implements RequestActionInterface {


    private Activity activity;
    private ChangePasswordDTO changePasswordDTO;
    private Form form;
    private Dialog dialog;

    public ChangePasswordDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTitle(R.string.dialog_change_password_title);
        setContentView(R.layout.dialog_change_password);

        changePasswordDTO = new ChangePasswordDTO();

        try {


            form = new Form(activity,
                    changePasswordDTO,
                    new Field.FieldProperties(ChangePasswordDTO.class.getDeclaredField("oldPassword"), R.string.dialog_change_password_old_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD),
                    new Field.FieldProperties(ChangePasswordDTO.class.getDeclaredField("newPassword"), R.string.dialog_change_password_new_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
            );
            form.intialize();

            ((ViewGroup) findViewById(R.id.insert_point)).addView(form);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        findViewById(R.id.b_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        findViewById(R.id.b_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog.this.cancel();
            }
        });
    }

    protected void save() {
        try {

            WebClient<ResultDTO> webClient = new WebClient<>(RequestEnum.ROOMMATE_CHANGE_PASSWORD,
                    changePasswordDTO,
                    ResultDTO.class);
            webClient.setParams("roommateId",Storage.getCurrentRoommate().getId()+"");

            //control the DTO
            DTO dto = form.control();

            if (dto != null) {

                //send request
                Request request = new Request(ChangePasswordDialog.this.getContext(),this, webClient);

                //execute request
                request.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        dialog.cancel();
        DialogConstructor.displayErrorMessage(getContext(), errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {
        dialog = DialogConstructor.dialogLoading(activity);
    }

    @Override
    public void successAction(DTO successDTO) {
        dialog.cancel();
        Toast.makeText(getContext(), R.string.dialog_change_password_new_success, Toast.LENGTH_SHORT).show();
        cancel();
    }
}
