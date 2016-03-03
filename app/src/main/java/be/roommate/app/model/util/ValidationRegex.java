package be.roommate.app.model.util;

/**
 * Created by flo on 01/03/16.
 */
public class ValidationRegex {

    public static final String EMAIL = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String PASSWORD = "^[a-zA-Z0-9_-]{6,18}$";

}
