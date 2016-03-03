package be.roommate.app.model.dto;

import javax.validation.constraints.Pattern;

import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.ValidationRegex;

/**
 * Created by florian on 27/12/14.
 */
public class ChangePasswordDTO extends DTO {

    @Pattern(regexp = ValidationRegex.PASSWORD,message = "VALIDATION_PASSWORD")
    private String oldPassword;

    @Pattern(regexp = ValidationRegex.PASSWORD,message = "VALIDATION_PASSWORD")
    private String newPassword;

    public ChangePasswordDTO() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordDTO{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
