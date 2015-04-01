package be.flo.roommateapp.vue.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

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
import be.flo.roommateapp.vue.util.Tools;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

import java.util.Locale;

/**
 * Created by florian on 2/11/14.
 */
public class RegistrationActivity extends AbstractActivity implements
        RequestActionInterface,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private Form form = null;
    private Dialog loadingDialog;
    private SignInButton mPlusSignInButton;
    //private Menu menu;
    //private Animation refreshAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GOOGLE CONNECTION
        mPlusClient =
                new PlusClient.Builder(this, this, this).setScopes(Scopes.PLUS_LOGIN,
                        Scopes.PLUS_ME).build();
        //GOOGLE CONNECTION

        loadingDialog = DialogConstructor.dialogLoading(this);

        //create the view
        setContentView(R.layout.activity_registration);

        // Find the Google+ sign in button.
        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
        if (supportsGooglePlayServices()) {
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

        //help
        ((TextView)findViewById(R.id.help)).setText(Tools.getHelp(this, R.string.help_registration));

    }

    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }

    /**
     * Try to sign in the user.
     */
    public void signIn() {
        if (!mPlusClient.isConnected()) {
            // Show the dialog as we are now signing in.
            setProgressBarVisible(true);
            // Make sure that we will start the resolution (e.g. fire the intent and pop up a
            // dialog for the user) for any errors that come in.
            mAutoResolveOnFail = true;
            // We should always have a connection result ready to resolve,
            // so we can start that process.
            if (mConnectionResult != null) {
                startResolution();
            } else {
                // If we don't have one though, we can start connect in
                // order to retrieve one.
                initiatePlusClientConnect();
            }
        }

        updateConnectButtonState();
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

    private static final String TAG = PlusBaseActivity.class.getSimpleName();

    // A magic number we will use to know that our sign-in error resolution activity has completed
    private static final int OUR_REQUEST_CODE = 49404;

    // A flag to stop multiple dialogues appearing for the user
    private boolean mAutoResolveOnFail;

    // A flag to track when a connection is already in progress
    public boolean mPlusClientIsConnecting = false;

    // This is the helper object that connects to Google Play Services.
    private PlusClient mPlusClient;

    // The saved result from {@link #onConnectionFailed(ConnectionResult)}.  If a connection
    // attempt has been made, this is non-null.
    // If this IS null, then the connect method is still running.
    private ConnectionResult mConnectionResult;


    /**
     * Called when the {@link PlusClient} revokes access to this app.
     */
    //protected abstract void onPlusClientRevokeAccess();

    /**
     * Called when the PlusClient is successfully connected.
     */
    //protected abstract void onPlusClientSignIn();

    /**
     * Called when the {@link PlusClient} is disconnected.
     */
    //protected abstract void onPlusClientSignOut();

    /**
     * Called when the {@link PlusClient} is blocking the UI.  If you have a progress bar widget,
     * this tells you when to show or hide it.
     */
    //protected abstract void onPlusClientBlockingUI(boolean show);

    /**
     * Called when there is a change in connection state.  If you have "Sign in"/ "Connect",
     * "Sign out"/ "Disconnect", or "Revoke access" buttons, this lets you know when their states
     * need to be updated.
     */
    //protected abstract void updateConnectButtonState();
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the PlusClient connection.
        // Scopes indicate the information about the user your application will be able to access.
        mPlusClient =
                new PlusClient.Builder(this, this, this).setScopes(Scopes.PLUS_LOGIN,
                        Scopes.PLUS_ME).build();
    }
*/

    /**
     * Connect the {@link PlusClient} only if a connection isn't already in progress.  This will
     * call back to {@link #onConnected(android.os.Bundle)} or
     * {@link #onConnectionFailed(com.google.android.gms.common.ConnectionResult)}.
     */
    private void initiatePlusClientConnect() {
        if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
            mPlusClient.connect();
        }
    }

    /**
     * Disconnect the {@link PlusClient} only if it is connected (otherwise, it can throw an error.)
     * This will call back to {@link #onDisconnected()}.
     */
    private void initiatePlusClientDisconnect() {
        if (mPlusClient.isConnected()) {
            mPlusClient.disconnect();
        }
    }

    /**
     * Sign out the user (so they can switch to another account).
     */
    public void signOut() {

        // We only want to sign out if we're connected.
        if (mPlusClient.isConnected()) {
            // Clear the default account in order to allow the user to potentially choose a
            // different account from the account chooser.
            mPlusClient.clearDefaultAccount();

            // Disconnect from Google Play Services, then reconnect in order to restart the
            // process from scratch.
            initiatePlusClientDisconnect();

            Log.v(TAG, "Sign out successful!");
        }

        updateConnectButtonState();
    }

    /**
     * Revoke Google+ authorization completely.
     */
    public void revokeAccess() {

        if (mPlusClient.isConnected()) {
            // Clear the default account as in the Sign Out.
            mPlusClient.clearDefaultAccount();

            // Revoke access to this entire application. This will call back to
            // onAccessRevoked when it is complete, as it needs to reach the Google
            // authentication servers to revoke all tokens.
            mPlusClient.revokeAccessAndDisconnect(new PlusClient.OnAccessRevokedListener() {
                public void onAccessRevoked(ConnectionResult result) {
                    updateConnectButtonState();
                    onPlusClientRevokeAccess();
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        initiatePlusClientConnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        initiatePlusClientDisconnect();
    }

    public boolean isPlusClientConnecting() {
        return mPlusClientIsConnecting;
    }

    private void setProgressBarVisible(boolean flag) {
        mPlusClientIsConnecting = flag;
        //TODO onPlusClientBlockingUI(flag);
    }

    /**
     * A helper method to flip the mResolveOnFail flag and start the resolution
     * of the ConnectionResult from the failed connect() call.
     */
    private void startResolution() {
        try {
            // Don't start another resolution now until we have a result from the activity we're
            // about to start.
            mAutoResolveOnFail = false;
            // If we can resolve the error, then call start resolution and pass it an integer tag
            // we can use to track.
            // This means that when we get the onActivityResult callback we'll know it's from
            // being started here.
            mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            // Any problems, just try to connect() again so we get a new ConnectionResult.
            mConnectionResult = null;
            initiatePlusClientConnect();
        }
    }

    /**
     * An earlier connection failed, and we're now receiving the result of the resolution attempt
     * by PlusClient.
     *
     * @see #onConnectionFailed(ConnectionResult)
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        updateConnectButtonState();
        if (requestCode == OUR_REQUEST_CODE && responseCode == RESULT_OK) {
            // If we have a successful result, we will want to be able to resolve any further
            // errors, so turn on resolution with our flag.
            mAutoResolveOnFail = true;
            // If we have a successful result, let's call connect() again. If there are any more
            // errors to resolve we'll get our onConnectionFailed, but if not,
            // we'll get onConnected.
            initiatePlusClientConnect();
        } else if (requestCode == OUR_REQUEST_CODE && responseCode != RESULT_OK) {
            // If we've got an error we can't resolve, we're no longer in the midst of signing
            // in, so we can stop the progress spinner.
            setProgressBarVisible(false);
        }
    }

    /**
     * Successfully connected (called by PlusClient)
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        updateConnectButtonState();
        setProgressBarVisible(false);
        //TODO onPlusClientSignIn();
    }

    /**
     * Successfully disconnected (called by PlusClient)
     */
    @Override
    public void onDisconnected() {
        updateConnectButtonState();
        //TODO onPlusClientSignOut();
    }

    /**
     * Connection failed for some reason (called by PlusClient)
     * Try and resolve the result.  Failure here is usually not an indication of a serious error,
     * just that the user's input is needed.
     *
     * @see #onActivityResult(int, int, Intent)
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        updateConnectButtonState();

        // Most of the time, the connection will fail with a user resolvable result. We can store
        // that in our mConnectionResult property ready to be used when the user clicks the
        // sign-in button.
        if (result.hasResolution()) {
            mConnectionResult = result;
            if (mAutoResolveOnFail) {
                // This is a local helper function that starts the resolution of the problem,
                // which may be showing the user an account chooser or similar.
                startResolution();
            }
        }
    }

    public PlusClient getPlusClient() {
        return mPlusClient;
    }

    protected void updateConnectButtonState() {
        //TODO: Update this logic to also handle the user logged in by email.
        boolean connected = getPlusClient().isConnected();

        //TODO  mSignOutButtons.setVisibility(connected ? View.VISIBLE : View.GONE);
        mPlusSignInButton.setVisibility(connected ? View.GONE : View.VISIBLE);
        //TODO mEmailLoginFormView.setVisibility(connected ? View.GONE : View.VISIBLE);
    }

    protected void onPlusClientRevokeAccess() {
        // TODO: Access to the user's G+ account has been revoked.  Per the developer terms, delete
        // any stored user data here.
    }
}
