package be.roommate.app.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import java.util.List;
import java.util.Locale;

import be.roommate.app.R;
import be.roommate.app.model.dto.LoginSuccessDTO;
import be.roommate.app.model.dto.post.RegistrationDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.technical.navigation.MenuManager;
import be.roommate.app.view.util.Tools;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.FieldEditText;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 2/11/14.
 */
public class RegistrationActivity extends ActionBarActivity implements
        RequestActionInterface {

    private Form form = null;
    private Dialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mask keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        loadingDialog = DialogConstructor.dialogLoading(this);

        //create the view
        setContentView(R.layout.activity_registration);


        //create field
        try {
            //email field
            LayoutInflater inflater = LayoutInflater.from(this);
            FieldEditText emailField = (FieldEditText) inflater.inflate(R.layout.field_text, null);
            emailField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            List<String> accountEmails = Tools.getAccountEmails(this);
            emailField.setAutoCompleteValues(accountEmails);

            form = new Form(this, new RegistrationDTO(), true,

                    new Field.FieldProperties(RegistrationDTO.class.getDeclaredField("name"), R.string.my_name,
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
                    new Field.FieldProperties(RegistrationDTO.class.getDeclaredField("email"), R.string.g_email,
                            emailField));
            form.intialize();


            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        findViewById(R.id.b_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        //help
        ((TextView) findViewById(R.id.help)).setText(Tools.getHelp(this, R.string.help_registration));

    }

    private void save() {
        try {
            DTO dto = form.control();

            ((RegistrationDTO) dto).setLang(Locale.getDefault().getLanguage());

            if (dto != null) {

                //create request
                WebClient<LoginSuccessDTO> webClient = new WebClient<>(RequestEnum.REGISTRATION,
                        dto,
                        LoginSuccessDTO.class);

                Request request = new Request(RegistrationActivity.this,RegistrationActivity.this, webClient);

                //execute request
                request.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.INTENT_MENU, MenuManager.MenuElement.MENU_EL_CONFIG.getOrder());
        intent.putExtra(MainActivity.INTENT_TAB, MenuManager.SubMenuElement.ADMIN_ROOMMATE_LIST.getOrder());

        //TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
        //startActivityForResult(intent,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(RegistrationActivity.this, WelcomeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
