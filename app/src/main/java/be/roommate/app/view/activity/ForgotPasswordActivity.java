package be.roommate.app.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import be.roommate.app.R;
import be.roommate.app.model.dto.ResultDTO;
import be.roommate.app.model.dto.post.ForgotPasswordDTO;
import be.roommate.app.model.dto.post.LoginDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.util.Tools;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.Form;

public class ForgotPasswordActivity extends ActionBarActivity implements RequestActionInterface {

    private Form form = null;
    private Dialog loadingDialog;
    private String emailUsed;

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

        setContentView(R.layout.activity_forgot_password);

        try {

            form = new Form(this, new ForgotPasswordDTO(),
                    new Field.FieldProperties(LoginDTO.class.getDeclaredField("email"), R.string.g_email,
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
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

                            emailUsed = ((ForgotPasswordDTO)dto).getEmail();

                            //create request
                            WebClient<ResultDTO> webClient = new WebClient<>(

                                    RequestEnum.FORGOT_PASSWORD,
                                    dto,
                                    ResultDTO.class);

                            Request request = new Request(ForgotPasswordActivity.this,ForgotPasswordActivity.this, webClient);

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
        ((TextView)findViewById(R.id.help)).setText(Tools.getHelp(this,R.string.help_forgot_password));
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

        Toast.makeText(this,R.string.forgot_password_success,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_EMAIL,emailUsed);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
