package be.flo.roommateapp.model.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by florian on 17/11/14.
 * control string by regex
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {

    public static final String EMAIL = "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$;";
    public static final String PASSWORD = "^[a-zA-Z0-9_-]{6,18}$";

    public String regexp();

    public int message();

}
