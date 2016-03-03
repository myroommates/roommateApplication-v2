package be.roommate.app.model.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.ValidationRegex;

/**
 * Created by florian on 2/04/15.
 */
public class GoogleRegistrationDTO extends DTO{

    @Pattern(regexp = ValidationRegex.EMAIL,message = "VALIDATION_EMAIL")
    private String email;

    @Pattern(regexp = ValidationRegex.PASSWORD,message = "VALIDATION_PASSWORD")
    private String password;

    @NotNull(message = "VALIDATION_NOT_NULL")
    private String googleKey;

    public GoogleRegistrationDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    @Override
    public String toString() {
        return "GoogleRegistrationDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", googleKey='" + googleKey + '\'' +
                '}';
    }
}
