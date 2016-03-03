package be.roommate.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.FacebookSdk;

import be.roommate.app.R;
import be.roommate.app.model.dto.LoginSuccessDTO;
import be.roommate.app.model.service.AccountService;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;

/**
 * Created by florian on 23/11/14.
 */
public class LoadingActivity extends Activity {

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        //test authentication
        String authenticationKey = AccountService.getAuthenticationKey(this);

        //init facebook
        FacebookSdk.sdkInitialize(this);

        if (authenticationKey == null && getIntent().getData() != null && getIntent().getData().toString().contains("?")) {
            authenticationKey = getIntent().getData().toString().split("\\?")[1];
        }

        if (authenticationKey != null) {
            Storage.setAuthenticationKey(authenticationKey);
            LoadDataRequest loadDataRequest = new LoadDataRequest();
            loadDataRequest.execute();
        } else {
            startActivity(new Intent(LoadingActivity.this, WelcomeActivity.class));
        }

    }


    /**
     * login request
     */
    private class LoadDataRequest extends AsyncTask<String, Void, Void> {

        private LoginSuccessDTO loginSuccessDTO;
        private String errorMessage;

        private LoadDataRequest() {
        }

        /**
         * send request
         */
        @Override
        protected Void doInBackground(String... params) {

            WebClient<LoginSuccessDTO> webClient = new WebClient<>(RequestEnum.LOAD_DATA, LoginSuccessDTO.class);

            try {
                loginSuccessDTO = webClient.sendRequest(LoadingActivity.this);
            } catch (MyException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
            return null;
        }

        /**
         * after execution
         */
        @Override
        protected void onPostExecute(Void result) {

            if (errorMessage != null) {
                startActivity(new Intent(LoadingActivity.this, WelcomeActivity.class));
            } else {
                //store data and change interface
                Storage.store(LoadingActivity.this, loginSuccessDTO);
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //display
        }

    }
}
