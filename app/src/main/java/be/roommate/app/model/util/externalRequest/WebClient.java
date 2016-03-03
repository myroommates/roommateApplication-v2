package be.roommate.app.model.util.externalRequest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.roommate.app.model.dto.ExceptionDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.service.ErrorMessageService;
import be.roommate.app.model.util.Constant;
import be.roommate.app.model.util.ErrorMessage;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;


/**
 * Created by florian on 11/11/14.
 * Communicate with the serveur
 */
public class WebClient<U extends DTO> {

    //error message service
    private ErrorMessageService errorMessageService = new ErrorMessageService();


    private final RequestEnum request;
    private Class<U> expectedResult;
    private DTO dto;
    private Map<String, String> params = new HashMap<String, String>();

    public WebClient(RequestEnum request, Class<U> expectedResult) {
        this.request = request;
        this.expectedResult = expectedResult;
    }

    public WebClient(RequestEnum request, DTO dto, Class<U> expectedResult) {
        this.request = request;
        this.dto = dto;
        this.expectedResult = expectedResult;
    }

    public void setParams(String key, String value) {
        params.put(key, value);
    }

    /**
     * build, send and manage a http request.
     * Build the request by parameters of the request get
     */
    public U sendRequest(Context context) throws MyException {

        //test connection
        if (!isOnline(context)) {
            throw new MyException(errorMessageService.getMessage(context, ErrorMessage.NOT_CONNECTED));
        }

        Log.w("webclient", "request :  " + request);

        //control request
        if (request == null) {
            throw new MyException(errorMessageService.getMessage(context, ErrorMessage.NULL_ELEMENT, "request"));
        }

        //control entrance
        if (dto != null) {
            controlSentDTOClass(context, dto.getClass());
        }

        //initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        gsonBuilder
                .create();
        Gson gson = gsonBuilder.create();

        //build url
        String urlString = Constant.TARGET_URL + request.getTarget();

        //add other params
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlString = urlString.replace(":" + entry.getKey(), entry.getValue());
        }


        //initialize http client
        HttpClient httpClient = new DefaultHttpClient();

        //build the url request
        try {
            //build the url by requested by
            final HttpRequestBase httpRequest;
            switch (request.getRequestType()) {
                case GET:
                    httpRequest = new HttpGet(urlString);
                    break;
                case POST:
                    httpRequest = new HttpPost(urlString);
                    break;
                case DELETE:
                    httpRequest = new HttpDelete(urlString);
                    break;
                case PUT:
                    httpRequest = new HttpPut(urlString);
                    break;
                default:
                    throw new MyException("request type not found");
            }

            //
            //add header
            //
            //source - to identify the type of source
            httpRequest.addHeader("applicationSource", "ANDROID");

            //add Dto
            if (dto != null) {
                if (httpRequest instanceof HttpEntityEnclosingRequestBase) {
                    String json = gson.toJson(dto);
                    StringEntity params = new StringEntity(json, "UTF-8");
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(params);
                    httpRequest.addHeader("content-type", "application/json");
                } else {
                    throw new MyException("cannot add dto into request type " + request.getRequestType().toString());
                }
            }

            //add authentication if it's needed
            if (request.needAuthentication()) {
                if (Storage.getAuthenticationKey() == null) {
                    throw new MyException("You need to be connected for this request : " + request.getTarget());
                }
                httpRequest.setHeader("authenticationKey", Storage.getAuthenticationKey());
            }

            Log.w("webclient", "send request to " + urlString);

            //send request
            HttpResponse response = httpClient.execute(httpRequest);

            Log.w("webclient", "response : " + response.getStatusLine());
            Log.w("webclient", "response : " + response.getEntity().toString());

            if (response.getStatusLine().getStatusCode() == 200) {

                //receive response
                if (request.getReceivedDTO() != null) {

                    String jsonString = EntityUtils.toString(response.getEntity());

                    return gson.fromJson(jsonString, expectedResult);
                }
            } else {

                //error
                String jsonString = EntityUtils.toString(response.getEntity());
                Log.w("WebClient", "error with code " + response.getStatusLine().getStatusCode());


                try {
                    ExceptionDTO exception = gson.fromJson(jsonString, ExceptionDTO.class);
                    String message = exception.getMessage();
                    if (message == null || message.length() == 0) {
                        message = "Unknow error";
                    }
//                    if (message.equals("NOT_CONNECTED") && context instanceof Activity) {
//                        Storage.clean(context);
//                        ViewManager.goToWelcomeActivity((Activity) context);
//                    } else {
                    throw new MyException(message);
//                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    throw new MyException(jsonString);
                }
            }

            return null;

            // handle response here...
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new MyException(errorMessageService.getMessage(context, ErrorMessage.UNEXPECTED_ERROR, ex.getMessage()));
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private boolean controlSentDTOClass(Context context, Class dtoClass) throws MyException {
        if (request.getSentDTO() != null && !dtoClass.equals(request.getSentDTO())) {
            if (dto.getClass().getSuperclass() != null && !controlSentDTOClass(context, dtoClass.getSuperclass())) {
                throw new MyException(errorMessageService.getMessage(context, ErrorMessage.WRONG_SENT_DTO, request.getSentDTO(), dto.getClass()));
            }
        }
        return true;
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
