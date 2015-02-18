package be.flo.roommateapp.model.service;

/**
 * Created by florian on 11/11/14.
 */
public class ErrorMessageService {

    public String getMessage(String message, Object... params) {

        for (int i = 0; i < params.length; i++) {
            message.replace("{" + i + "}", params[i].toString());
        }

        return message;
    }
}
