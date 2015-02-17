package be.flo.roommateapp.vue.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.LoginSuccessDTO;
import be.flo.roommateapp.model.dto.post.LoginDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.technical.AbstractActivity;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

public class LoginActivity extends AbstractActivity implements RequestActionInterface {

    private Form form = null;
    private Dialog loadingDialog;

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loadingDialog = DialogConstructor.dialogLoading(this);

        setContentView(R.layout.activity_login);

        // to registration
        findViewById(R.id.b_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        try {

            try {
                form = new Form(this, new LoginDTO(),
                        new Field.FieldProperties(LoginDTO.class.getDeclaredField("email"), R.string.g_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS),
                        new Field.FieldProperties(LoginDTO.class.getDeclaredField("password"), R.string.g_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

            // login action
            findViewById(R.id.b_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        DTO dto = form.control();

                        if (dto != null) {

                            //create request
                            WebClient<LoginSuccessDTO> webClient = new WebClient<>(RequestEnum.LOGIN,
                                    dto,
                                    LoginSuccessDTO.class);

                            Request request = new Request(LoginActivity.this, webClient);

                            //execute request
                            request.execute();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void displayErrorMessage(String errorMessage) {
        findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.error_message)).setText(errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {

        form.setEnabled(!loading);
        if (loading) {
            loadingDialog.show();

            // create animation and add to the refresh item
            findViewById(R.id.error_message_container).setVisibility(View.GONE);
        } else {
            loadingDialog.cancel();
        }
    }

    @Override
    public void successAction(DTO successDTO) {

        Storage.store(this, (LoginSuccessDTO) successDTO);
        Intent intent = new Intent(LoginActivity.this, MAIN_ACTIVITY);
        //startActivityForResult(intent,0);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
