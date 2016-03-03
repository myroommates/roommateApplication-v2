package be.roommate.app.model.service;

import android.content.Context;

import be.roommate.app.model.util.ErrorMessage;

/**
 * Created by florian on 11/11/14.
 */
public class ErrorMessageService {

    public String getMessage(Context context,ErrorMessage errorMessage, Object... params) {

        String message = context.getResources().getString(errorMessage.getMessage());

        for (int i = 0; i < params.length; i++) {
            message=message.replace("{" + i + "}", params[i].toString());
        }

        return message;
    }
}
