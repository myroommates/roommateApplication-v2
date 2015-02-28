package be.flo.roommateapp.vue.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.LoginSuccessDTO;
import be.flo.roommateapp.model.dto.post.RegistrationDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.technical.AbstractActivity;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

import java.util.Locale;

/**
 * Created by florian on 2/11/14.
 */
public class RegistrationActivity extends AbstractActivity implements RequestActionInterface {

    private Form form = null;
    private Dialog loadingDialog;
    //private Menu menu;
    //private Animation refreshAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingDialog = DialogConstructor.dialogLoading(this);

        //create the view
        setContentView(R.layout.activity_registration);

        //create field
        try {
            try {
                form = new Form(this, new RegistrationDTO(),

                        new Field.FieldProperties(RegistrationDTO.class.getDeclaredField("name"), R.string.my_name,
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
                        new Field.FieldProperties(RegistrationDTO.class.getDeclaredField("email"), R.string.g_email,
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException e) {
            e.printStackTrace();
        }

        findViewById(R.id.b_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    private void save() {
        try {
            DTO dto = form.control();

            ((RegistrationDTO)dto).setLang(Locale.getDefault().getLanguage());

            if (dto != null) {

                //create request
                WebClient<LoginSuccessDTO> webClient = new WebClient<>(this, RequestEnum.REGISTRATION,
                        dto,
                        LoginSuccessDTO.class);

                Request request = new Request(RegistrationActivity.this, webClient);

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
        Intent intent = new Intent(this, MAIN_ACTIVITY);
        intent.putExtra(MainActivity.INTENT_MENU, MenuManager.MenuElement.MENU_EL_CONFIG.getOrder());
        intent.putExtra(MainActivity.INTENT_TAB, MenuManager.SubMenuElement.ADMIN_ROOMMATE_LIST.getOrder());

        //TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
        //startActivityForResult(intent,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
