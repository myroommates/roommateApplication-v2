package be.roommate.app.model.util;

import be.roommate.app.R;

/**
 * Created by florian on 11/11/14.
 * Error message (to translate)
 */
public enum ErrorMessage {

    WRONG_SENT_DTO(R.string.error_webclient_wrong_send_dto),
    NULL_ELEMENT(R.string.error_webclient_result_null),
    UNEXPECTED_ERROR(R.string.error_unexpected),
    NOT_CONNECTED(R.string.notConnectedDescr);

    private final int message;

    private ErrorMessage(int message) {

        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
