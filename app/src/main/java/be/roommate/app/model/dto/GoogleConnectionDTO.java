package be.roommate.app.model.dto;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 1/04/15.
 */
public class GoogleConnectionDTO extends DTO{

    private String email;

    private String name;

    private String key;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "GoogleConnectionDTO{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
