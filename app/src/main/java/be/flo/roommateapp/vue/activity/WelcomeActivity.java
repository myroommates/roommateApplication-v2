package be.flo.roommateapp.vue.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.plus.PlusClient;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.GoogleConnectionDTO;
import be.flo.roommateapp.model.dto.LoginSuccessDTO;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.technical.AbstractActivity;
import be.flo.roommateapp.vue.util.Tools;

public class WelcomeActivity extends AbstractActivity
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        RequestActionInterface, AuthInterface {

    // A magic number we will use to know that our sign-in error resolution activity has completed
    private static final int OUR_REQUEST_CODE = 49404;

    private PlusClient mPlusClient;
    private SignInButton mPlusSignInButton;
    private ConnectionResult mConnectionResult;
    private Dialog loadingDialog;
    private GoogleApiClient mGoogleApiClient;

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //build loading loadingDialog
        loadingDialog = DialogConstructor.dialogLoading(this);

        setContentView(R.layout.activity_welcome);

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

        //build plusClient
        mPlusClient =
                new PlusClient.Builder(this, this, this).setScopes(
                        Scopes.PLUS_LOGIN,
                        Scopes.PROFILE,
                        Scopes.PLUS_ME).build();

        // Find the Google+ sign in button.
        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS) {
            // Set a listener to connect the user when the G+ button is clicked.
            mPlusSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });

        } else {
            // Don't offer G+ sign in if the app's version is too low to support Google Play
            // Services.
            mPlusSignInButton.setVisibility(View.GONE);
            return;
        }


    }

    public void signIn() {
        if (!mPlusClient.isConnected()) {
            // Show the loadingDialog as we are now signing in.
            displayLoadingModal(true);//setProgressBarVisible(true);
            // Make sure that we will start the resolution (e.g. fire the intent and pop up a
            // loadingDialog for the user) for any errors that come in.
            //TODO mAutoResolveOnFail = true;
            // We should always have a connection result ready to resolve,
            // so we can start that process.
            if (mConnectionResult != null) {
                startResolution();
            } else {

                if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
                    mPlusClient.connect();
                }
            }
        }

        //updateConnectButtonState();
    }

    /**
     * A helper method to flip the mResolveOnFail flag and start the resolution
     * of the ConnectionResult from the failed connect() call.
     */
    private void startResolution() {
        try {
            // Don't start another resolution now until we have a result from the activity we're
            // about to start.
            //TODO mAutoResolveOnFail = false;
            // If we can resolve the error, then call start resolution and pass it an integer tag
            // we can use to track.
            // This means that when we get the onActivityResult callback we'll know it's from
            // being started here.
            mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Any problems, just try to connect() again so we get a new ConnectionResult.
            mConnectionResult = null;
            if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
                mPlusClient.connect();
            }
        }
    }

    private void displayLoadingModal(boolean display) {
        if (display) {
            loadingDialog.show();
        } else {
            loadingDialog.cancel();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {


        mGoogleApiClient  = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();

        boolean isConnected =mPlusClient.isConnected();

        String name =mPlusClient.getAccountName();
        GetGooglePlusToken getGooglePlusToken = new GetGooglePlusToken(this,name,this);
        getGooglePlusToken.execute();

    }
    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(this, errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {
        loadingDialog.show();
    }

    @Override
    public void successAction(DTO successDTO) {
        Storage.store(this, (LoginSuccessDTO) successDTO);
        Intent intent = new Intent(WelcomeActivity.this, MAIN_ACTIVITY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void setAuth(String accessToken1) {
        sendRequest(accessToken1);
    }



    private void sendRequest(String token){

        String email = mPlusClient.getAccountName();
        String name;
        if (mPlusClient.getCurrentPerson() != null && mPlusClient.getCurrentPerson().hasDisplayName()) {
            name = mPlusClient.getCurrentPerson().getDisplayName();
        } else {
            name = email.split("@")[0];
        }

        //TODO send connection method
        //TODO => need a key !!

        //build dto
        GoogleConnectionDTO dto = new GoogleConnectionDTO();
        dto.setEmail(email);
        dto.setName(name);
        dto.setKey(token);

        WebClient<LoginSuccessDTO> webClient = new WebClient<>(this, RequestEnum.GOOGLE_CONNECTION,
                dto,
                LoginSuccessDTO.class);

        //send request
        Request request = new Request(this, webClient);

        //execute request
        request.execute();
    }

}
