package be.flo.roommateapp.model.dto.post;

import be.flo.roommateapp.model.dto.technical.PostDTO;
import be.flo.roommateapp.model.util.annotation.Pattern;
import be.flo.roommateapp.model.util.annotation.Size;

/**
 * Created by florian on 11/11/14.
 */
public class RegistrationDTO extends PostDTO {

    @Size(min = 2, max = 50, message = "entre 2 et 50 caractères")
    private String name;
    @Pattern(regex = Pattern.EMAIL, message = "email attendue")
    private String email;

    public RegistrationDTO() {
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
