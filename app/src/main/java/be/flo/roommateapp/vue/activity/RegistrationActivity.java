package be.flo.roommateapp.vue.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
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
import be.flo.roommateapp.vue.technical.AbstractActivity;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

/**
 * Created by florian on 2/11/14.
 *
 */
public class RegistrationActivity extends AbstractActivity implements RequestActionInterface {

    private Form form = null;
    private Menu menu;
    private Animation refreshAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create the view
        setContentView(R.layout.activity_registration);

        //create field
        try {
            try {
                form = new Form(this, new RegistrationDTO(),

                        new Field.FieldProperties(RegistrationDTO.class.getDeclaredField("name"), R.string.my_name, InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
                        new Field.FieldProperties(RegistrationDTO.class.getDeclaredField("email"), R.string.g_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException e) {
            e.printStackTrace();
        }

        //load animation for refresh button
        refreshAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        refreshAnimation.setRepeatCount(Animation.INFINITE);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        assert this.getActionBar() != null;

        this.getActionBar().setTitle(R.string.g_registration);
        this.menu = menu;
        menu.clear();
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        try {
            DTO dto = form.control();

            if (dto != null) {

                //create request
                WebClient<LoginSuccessDTO> webClient = new WebClient<>(RequestEnum.REGISTRATION,
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
        findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.error_message)).setText(errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {

        form.setEnabled(!loading);
        if (loading) {
            // create animation and add to the refresh item
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.loading_icon, null);
            menu.findItem(R.id.b_save).setActionView(iv);
            iv.startAnimation(refreshAnimation);
            findViewById(R.id.error_message_container).setVisibility(View.GONE);
        } else {
            if (menu.findItem(R.id.b_save).getActionView() != null) {
                menu.findItem(R.id.b_save).getActionView().clearAnimation();
                menu.findItem(R.id.b_save).setActionView(null);
            }
        }
    }

    @Override
    public void successAction(DTO successDTO) {
        Storage.store(this, (LoginSuccessDTO) successDTO);
        Intent intent = new Intent(this, MAIN_ACTIVITY);
        //TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
        //startActivityForResult(intent,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
