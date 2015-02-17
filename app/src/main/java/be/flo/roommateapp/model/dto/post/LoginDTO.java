package be.flo.roommateapp.model.dto.post;

import be.flo.roommateapp.model.dto.technical.PostDTO;
import be.flo.roommateapp.model.util.annotation.Pattern;

/**
 * Created by florian on 10/11/14.
 */
public class LoginDTO extends PostDTO {

    @Pattern(regex = Pattern.EMAIL, message = "Email conforme attendu")
    protected String email;

    @Pattern(regex = "^[a-zA-Z0-9]{6,18}$", message = "Entre 6 et 18 lettres ou chiffre")
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
