package be.flo.roommateapp.model.dto.post;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.PostDTO;
import be.flo.roommateapp.model.util.annotation.Pattern;
import be.flo.roommateapp.model.util.annotation.Size;

/**
 * Created by florian on 11/11/14.
 */
public class RegistrationDTO extends PostDTO {

    @Size(min = 2, max = 50, message = R.string.verification_roommate_name)
    private String name;
    @Pattern(regexp = Pattern.EMAIL, message = R.string.verification_email)
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
