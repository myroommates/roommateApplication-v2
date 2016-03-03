package be.roommate.app.model.util.externalRequest;

import android.content.Context;
import android.os.AsyncTask;

import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.view.RequestActionInterface;

/**
 * Created by florian on 24/11/14.
 * Send a request to the server
 */
public class Request extends AsyncTask<String, Void, Void> {

    //private final Activity activity;
    //private final RequestEventsInterface requestEventsInterface;
    private DTO successDTO;
    private String errorMessage = null;
    private final WebClient webClient;
    private RequestActionInterface requestActionInterface;
    private Context context;


    public Request(Context context, RequestActionInterface requestActionInterface,
                   WebClient webClient) {

        this.context = context;
        this.webClient = webClient;
        this.requestActionInterface = requestActionInterface;
    }

    /**
     * send request
     */
    @Override
    protected Void doInBackground(String... params) {


        try {
            successDTO = webClient.sendRequest(context);
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

        requestActionInterface.loadingAction(false);

        if (errorMessage != null) {

            requestActionInterface.displayErrorMessage(errorMessage);

        } else {
            requestActionInterface.successAction(successDTO);
        }

    }

    @Override
    protected void onPreExecute() {
        requestActionInterface.loadingAction(true);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //display
    }

}
