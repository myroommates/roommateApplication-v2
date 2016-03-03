package be.roommate.app.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import be.roommate.app.R;
import be.roommate.app.model.dto.LoginSuccessDTO;
import be.roommate.app.model.dto.post.LoginDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.util.Tools;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.FieldEditText;
import be.roommate.app.view.widget.Form;

public class LoginActivity extends ActionBarActivity implements RequestActionInterface {

    public static final String EXTRA_EMAIL = "email";
    private Form form = null;
    private Dialog loadingDialog;

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //mask keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingDialog = DialogConstructor.dialogLoading(this);

        setContentView(R.layout.activity_login);


        LoginDTO dto = new LoginDTO();

        //try to recover the email
        if (getIntent().getStringExtra(EXTRA_EMAIL) != null) {
            dto.setEmail(getIntent().getStringExtra(EXTRA_EMAIL));
        }

        // to registration
        findViewById(R.id.b_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        try {
            //email field
            LayoutInflater inflater = LayoutInflater.from(this);
            FieldEditText emailField = (FieldEditText) inflater.inflate(R.layout.field_text, null);
            emailField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            List<String> accountEmails = Tools.getAccountEmails(this);
            emailField.setAutoCompleteValues(accountEmails);

            form = new Form(this, dto,true,
                    new Field.FieldProperties(LoginDTO.class.getDeclaredField("email"), R.string.g_email,
                            emailField),
                    new Field.FieldProperties(LoginDTO.class.getDeclaredField("password"), R.string.g_password,
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            form.intialize();


            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

            // login action
            findViewById(R.id.b_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DTO dto = null;
                    try {
                        dto = form.control();

                        if (dto != null) {

                            //create request
                            WebClient<LoginSuccessDTO> webClient = new WebClient<>(RequestEnum.LOGIN,
                                    dto,
                                    LoginSuccessDTO.class);

                            Request request = new Request(LoginActivity.this,LoginActivity.this, webClient);

                            //execute request
                            request.execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //help
        ((TextView) findViewById(R.id.help)).setText(Tools.getHelp(this, R.string.help_login));
    }

    @Override
    public void onStart() {
        super.onStart();

        //try to recover the email
        if (getIntent().getStringExtra(EXTRA_EMAIL) != null) {
            form.getField(R.string.g_email).setValue(getIntent().getStringExtra(EXTRA_EMAIL));
        }
    }


    @Override
    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(this, errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {

        form.setEnabled(!loading);
        if (loading) {
            loadingDialog.show();

            // create animation and add to the refresh item
        } else {
            loadingDialog.cancel();
        }
    }

    @Override
    public void successAction(DTO successDTO) {

        Storage.store(this, (LoginSuccessDTO) successDTO);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //startActivityForResult(intent,0);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
