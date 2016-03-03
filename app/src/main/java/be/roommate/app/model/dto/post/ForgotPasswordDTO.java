package be.roommate.app.model.dto.post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import be.roommate.app.model.dto.technical.PostDTO;
import be.roommate.app.model.util.ValidationRegex;

/**
 * Created by florian on 4/03/15.
 */
public class ForgotPasswordDTO extends PostDTO{

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Pattern(regexp = ValidationRegex.EMAIL,message = "VALIDATION_EMAIL")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ForgotPasswordDTO{" +
                "email='" + email + '\'' +
                '}';
    }
}
