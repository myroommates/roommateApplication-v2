package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.Pattern;
import be.flo.roommateapp.model.util.annotation.Size;

/**
 * Created by florian on 11/11/14.
 * roommate
 */
public class RoommateDTO extends DTO implements Writable {

    private Long id;
    @Size(min = 2, max = 50, message = R.string.verification_roommate_name)
    private String name;
    @Size(min = 1, max = 3, message = R.string.verification_roommate_nameAbrv)
    private String nameAbrv;
    @Pattern(regexp = Pattern.EMAIL, message = R.string.verification_email)
    private String email;

    private float iconColor;

    private boolean isAdmin;

    private String languageCode;

    private boolean keepSessionOpen;

    private int iconColorTop;

    private int iconColorBottom;

    public RoommateDTO() {
    }


    public int getIconColorTop() {
        return iconColorTop;
    }

    public void setIconColorTop(int iconColorTop) {
        this.iconColorTop = iconColorTop;
    }

    public int getIconColorBottom() {
        return iconColorBottom;
    }

    public void setIconColorBottom(int iconColorBottom) {
        this.iconColorBottom = iconColorBottom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAbrv() {
        return nameAbrv;
    }

    public void setNameAbrv(String nameAbrv) {
        this.nameAbrv = nameAbrv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getIconColor() {
        return iconColor;
    }

    public void setIconColor(float iconColor) {
        this.iconColor = iconColor;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isKeepSessionOpen() {
        return keepSessionOpen;
    }

    public void setKeepSessionOpen(boolean keepSessionOpen) {
        this.keepSessionOpen = keepSessionOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RoommateDTO) {
            if (((RoommateDTO) o).getId().equals(this.id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getString() {
        return name;
    }

    @Override
    public String toString() {
        return "RoommateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameAbrv='" + nameAbrv + '\'' +
                ", email='" + email + '\'' +
                ", iconColor=" + iconColor +
                ", isAdmin=" + isAdmin +
                ", languageCode='" + languageCode + '\'' +
                ", keepSessionOpen=" + keepSessionOpen +
                '}';
    }
}
