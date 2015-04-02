package be.flo.roommateapp.model.dto;


import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.NotNull;
import be.flo.roommateapp.model.util.annotation.Pattern;

/**
 * Created by florian on 2/04/15.
 */
public class GoogleRegistrationDTO extends DTO{

    @Pattern(regexp = Pattern.EMAIL, message = R.string.verification_email)
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message = R.string.verification_password)
    private String password;

    @NotNull
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
