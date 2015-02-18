package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.Pattern;

/**
 * Created by florian on 27/12/14.
 */
public class ChangePasswordDTO extends DTO {

    @Pattern(regexp = Pattern.PASSWORD,message = R.string.verification_password)
    private String oldPassword;

    @Pattern(regexp = Pattern.PASSWORD,message = R.string.verification_password)
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
