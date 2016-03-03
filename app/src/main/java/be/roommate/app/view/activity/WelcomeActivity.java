package be.roommate.app.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import be.roommate.app.R;
import be.roommate.app.model.dto.LoginSuccessDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.technical.navigation.MenuManager;
import be.roommate.app.view.util.Tools;
import be.roommate.app.view.widget.FacebookButton;

public class WelcomeActivity extends Activity  implements RequestActionInterface {

    private CallbackManager callbackManager;
    private Dialog loadingDialog;
    private int counter = 0;

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //build loading dialog
        loadingDialog = DialogConstructor.dialogLoading(this);

        //mask keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        // to registration
        findViewById(R.id.b_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, RegistrationActivity.class));
            }
        });

        // to login
        findViewById(R.id.b_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });
        //help
        ((TextView) findViewById(R.id.help)).setText(Tools.getHelp(this, R.string.help_not_registered));

        //init facebook
        final FacebookButton facebookLoginButton = (FacebookButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // facebook Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                WebClient<LoginSuccessDTO> webClient = new WebClient<LoginSuccessDTO>(RequestEnum.LOGIN_FACEBOOK,
                        LoginSuccessDTO.class);

                webClient.setParams("access_token", loginResult.getAccessToken().getToken());
                webClient.setParams("user_id", loginResult.getAccessToken().getUserId());

                Request request = new Request(WelcomeActivity.this, WelcomeActivity.this, webClient);

                //execute request
                request.execute();
            }

            @Override
            public void onCancel() {
                int i =0;
            }

            @Override
            public void onError(FacebookException exception) {
                //TODO
                Log.w("LOG", "ERROR : " + exception.toString());
                int i = 0;
                // App code
            }
        });

        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));

                ((TextView) findViewById(R.id.key)).setText(hashKey);
                findViewById(R.id.key).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter++;
                        if (counter >= 3) {
                            ((TextView) findViewById(R.id.key)).setTextColor(getResources().getColor(R.color.color_black));
                        }
                    }
                });
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Storage.testStorage()) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(this, errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {

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
        final Intent intent;
        if(Storage.getRoommateList().size() == 1) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.INTENT_MENU, MenuManager.MenuElement.MENU_EL_CONFIG.getOrder());
            intent.putExtra(MainActivity.INTENT_TAB, MenuManager.SubMenuElement.ADMIN_ROOMMATE_LIST.getOrder());
        }
        else{
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
