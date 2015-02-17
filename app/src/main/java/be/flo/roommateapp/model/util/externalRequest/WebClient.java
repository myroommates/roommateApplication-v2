package be.flo.roommateapp.model.util.externalRequest;

import android.util.Log;
import be.flo.roommateapp.model.dto.ExceptionDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.service.ErrorMessageService;
import be.flo.roommateapp.model.util.ErrorMessage;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;


/**
 * Created by florian on 11/11/14.
 * Communicate with the serveur
 */
public class WebClient<U extends DTO> {

    //main url of the service
    //public final static String TARGET_URL = "http://192.168.1.4:9000/";
    //main url of the service - office
    //public final static String TARGET_URL = "http://192.168.18.190:9000/";
    //  test
    //public final static String TARGET_URL = "http://roommate-service.herokuapp.com/";
    //  official
    public final static String TARGET_URL = "http://roommate.herokuapp.com/";

    //error message service
    private ErrorMessageService errorMessageService = new ErrorMessageService();


    private final RequestEnum request;
    private String param1 = null;
    private Class<U> expectedResult;
    private DTO dto;

    public WebClient(RequestEnum request, Long id, Class<U> expectedResult) {
        this.request = request;
        this.param1 = String.valueOf(id);
        this.expectedResult = expectedResult;
    }

    public WebClient(RequestEnum request, DTO dto, Long id, Class<U> expectedResult) {
        this.request = request;
        this.dto = dto;
        this.param1 = String.valueOf(id);
        this.expectedResult = expectedResult;
    }

    public WebClient(RequestEnum request, DTO dto, String param1, Class<U> expectedResult) {
        this.request = request;
        this.dto = dto;
        this.param1 = param1;
        this.expectedResult = expectedResult;
    }

    public WebClient(RequestEnum request, Class<U> expectedResult) {
        this.request = request;
        this.expectedResult = expectedResult;
    }

    public WebClient(RequestEnum request, DTO dto, Class<U> expectedResult) {
        this.request = request;
        this.dto = dto;
        this.expectedResult = expectedResult;
    }

    /**
     * build, send and manage a http request.
     * Build the request by parameters of the request get
     */
    public U sendRequest() throws MyException {//RequestEnum request, DTO dto, Long id, Class<U> expectedResult) throws MyException {

        Log.w("webclient", "request :  " + request);

        //control request
        if (request == null) {
            throw new MyException(errorMessageService.getMessage(ErrorMessage.NULL_ELEMENT, "request"));
        }

        //control entrance
        if (request.getSentDTO() != null && !dto.getClass().equals(request.getSentDTO())) {
            throw new MyException(errorMessageService.getMessage(ErrorMessage.WRONG_SENT_DTO, dto.getClass(), request.getSentDTO()));
        }

        //initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = gsonBuilder.create();

        //build url
        String urlString = TARGET_URL + request.getTarget();

        //add id into request if needed
        if (request.isRequestId()) {
            if (param1 == null) {
                throw new MyException("the request " + request.toString() + " needs an id");
            }
            urlString = urlString.replace(":param1", param1);
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
            //add params
            //

            //add Dto
            if (dto != null) {
                if (httpRequest instanceof HttpEntityEnclosingRequestBase) {
                    String json = gson.toJson(dto);
                    StringEntity params = new StringEntity(json,"UTF-8");
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
                    throw new MyException(exception.getMessage());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    throw new MyException(jsonString);
                }
            }

            return null;

            // handle response here...
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new MyException(errorMessageService.getMessage(ErrorMessage.UNEXPECTED_ERROR, ex.getMessage()));
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
