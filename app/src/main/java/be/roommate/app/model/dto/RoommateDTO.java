package be.roommate.app.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.ValidationRegex;

/**
 * Created by florian on 11/11/14.
 * roommate
 */
public class RoommateDTO extends DTO implements Writable {

    private Long id;
    @NotNull(message = "VALIDATION_NOT_NULL")
    @Size(min=1,max=50, message = "VALIDATION_SIZE")
    private String name;
    @Size(min = 1, max = 3, message = "VALIDATION_SIZE")
    private String nameAbrv;
    @NotNull(message = "VALIDATION_NOT_NULL")
    @Pattern(regexp = ValidationRegex.EMAIL,message = "VALIDATION_EMAIL")
    private String email;

    private float iconColor;

    private boolean isAdmin;

    private String languageCode;

    private boolean keepSessionOpen;

    private int iconColorTop;

    private int iconColorBottom;

    private boolean active;

    public RoommateDTO() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
                ", languageCode='" + languageCode + '\'' +
                ", iconColorTop=" + iconColorTop +
                ", iconColorBottom=" + iconColorBottom +
                ", active=" + active +
                '}';
    }
}
