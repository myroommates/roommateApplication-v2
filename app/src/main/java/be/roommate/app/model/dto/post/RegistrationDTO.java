package be.roommate.app.model.dto.post;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.PostDTO;
import be.roommate.app.model.util.ValidationRegex;

/**
 * Created by florian on 11/11/14.
 */
public class RegistrationDTO extends PostDTO {

    @Size(min=2,max=50, message = "VALIDATION_SIZE")
    private String name;
    @Pattern(regexp = ValidationRegex.EMAIL,message = "VALIDATION_EMAIL")
    private String email;

    private String lang;

    public RegistrationDTO() {
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
