package be.roommate.app.model.util.externalRequest;

import be.roommate.app.model.util.exception.MyException;

/**
 * Created by florian on 3/04/15.
 */
public class CallbackException extends MyException{

    private RequestCallback requestCallback;

    public CallbackException(RequestCallback requestCallback) {
        super("");
        this.requestCallback = requestCallback;
    }

    public RequestCallback getRequestCallback() {
        return requestCallback;
    }
}
