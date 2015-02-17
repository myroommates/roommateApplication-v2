package be.flo.roommateapp.model.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by florian on 18/11/14.
 * control if the field is not null
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    public String message() default "merci de remplir ce champs";
}
