package be.flo.roommateapp.vue.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ChangePasswordDTO;
import be.flo.roommateapp.model.dto.GoogleRegistrationDTO;
import be.flo.roommateapp.model.dto.LoginSuccessDTO;
import be.flo.roommateapp.model.dto.ResultDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

/**
 * Created by florian on 18/02/15.
 */
public class ConfirmGoogleAccountDialog extends Dialog implements RequestActionInterface {


    private Activity activity;
    private RequestActionInterface requestActionInterface;
    private GoogleRegistrationDTO googleRegistrationDTO;
    private Form form;
    private Dialog dialog;

    public ConfirmGoogleAccountDialog(Activity activity,RequestActionInterface requestActionInterface,String googleKey,String email) {
        super(activity);
        this.activity = activity;
        this.requestActionInterface = requestActionInterface;

        googleRegistrationDTO = new GoogleRegistrationDTO();
        googleRegistrationDTO.setGoogleKey(googleKey);
        googleRegistrationDTO.setEmail(email);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTitle(R.string.dialog_confirm_google_account_title);
        setContentView(R.layout.dialog_confirm_dialog);

        try {


            form = new Form(activity,
                    googleRegistrationDTO,
                    new Field.FieldProperties(GoogleRegistrationDTO.class.getDeclaredField("password"), R.string.g_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
            );

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
                ConfirmGoogleAccountDialog.this.cancel();
            }
        });
    }

    protected void save() {
        try {

            WebClient<LoginSuccessDTO> webClient = new WebClient<>(getContext(), RequestEnum.GOOGLE_REGISTRATION,
                    googleRegistrationDTO,
                    LoginSuccessDTO.class);

            //control the DTO
            DTO dto = form.control();

            if (dto != null) {

                //send request
                Request request = new Request(this, webClient);

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
        requestActionInterface.successAction(successDTO);
        cancel();
    }
}
