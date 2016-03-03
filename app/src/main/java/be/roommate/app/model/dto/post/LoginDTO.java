package be.roommate.app.model.dto.post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import be.roommate.app.model.dto.technical.PostDTO;
import be.roommate.app.model.util.ValidationRegex;

/**
 * Created by florian on 10/11/14.
 */
public class LoginDTO extends PostDTO {

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Pattern(regexp = ValidationRegex.EMAIL,message = "VALIDATION_EMAIL")
    private String email;

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Pattern(regexp = ValidationRegex.PASSWORD,message = "VALIDATION_PASSWORD")
    private String password;

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
