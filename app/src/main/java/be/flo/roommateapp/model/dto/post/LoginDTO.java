package be.flo.roommateapp.model.dto.post;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.PostDTO;
import be.flo.roommateapp.model.util.annotation.Pattern;

/**
 * Created by florian on 10/11/14.
 */
public class LoginDTO extends PostDTO {

    @Pattern(regexp = Pattern.EMAIL, message = R.string.verification_email)
    protected String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$", message = R.string.verification_password)
    protected String password;

    public LoginDTO() {
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

    @Override
    public String toString() {
        return "LoginDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
