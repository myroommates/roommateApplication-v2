package be.flo.roommateapp.vue.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;

import java.io.IOException;

/**
 * Created by florian on 1/04/15.
 */
public class GetGooglePlusToken extends AsyncTask<Void, Void, String> {
    Activity context;
    //private GoogleApiClient mGoogleApiClient;
    String  accountname;
    private AuthInterface authInterface;
    private String TAG  = this.getClass().getSimpleName();
    private String token;



    public GetGooglePlusToken(Activity context,
                              String  accountname,
    AuthInterface authInterface){//GoogleApiClient mGoogleApiClient) {
        this.context = context;
        //this.mGoogleApiClient = mGoogleApiClient;
        this.accountname=accountname;
        this.authInterface = authInterface;
    }

    @Override
    protected String doInBackground(Void... params) {
        String accessToken1 = null;
        try {

            Bundle bundle = new Bundle();

            //String  accountname  =   Plus.AccountApi.getAccountName(mGoogleApiClient);
            String scope = "oauth2:" + Scopes.PLUS_LOGIN + " " + "https://www.googleapis.com/auth/userinfo.email" + " https://www.googleapis.com/auth/plus.profile.agerange.read"+" https://www.googleapis.com/auth/userinfo.profile";
            accessToken1 = GoogleAuthUtil.getToken(context,
                    accountname,
                    scope);


            return accessToken1;

        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            //TODO: HANDLE
            Log.e(TAG, "transientEx");
            transientEx.printStackTrace();
            accessToken1 = null;

        } catch (UserRecoverableAuthException e) {
            // Recover
            context.startActivityForResult(e.getIntent(), 234);//GoogleAuthUtil.REQUEST_AUTHORIZATION);
            Log.e(TAG, "UserRecoverableAuthException");
            e.printStackTrace();
            accessToken1 = null;

        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            Log.e(TAG, "GoogleAuthException");
            authEx.printStackTrace();
            accessToken1 = null;
        } catch (Exception e) {
            Log.e(TAG, "RuntimeException");
            e.printStackTrace();
            accessToken1 = null;
            throw new RuntimeException(e);
        }
        Log.wtf(TAG, "Code should not go here");
        accessToken1 = null;
        return accessToken1;
    }

    @Override
    protected void onPostExecute(String response) {
        token = response;
        Log.d(TAG, "Google access token = " + response);

        authInterface.setAuth(response);
    }

    public String getToken() {
        return token;
    }
}
