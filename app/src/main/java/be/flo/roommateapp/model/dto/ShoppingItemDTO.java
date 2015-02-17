package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.Size;

import java.util.Date;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemDTO extends DTO {

    private Long id;

    @Size(min = 1, max = 1000, message = "entre 1 et 1000 caractères")
    private String description;

    private Date creationDate;

    private Long homeId;

    private Long creatorId;

    private boolean wasBought;

    private Boolean onlyForMe;

    public ShoppingItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public boolean isWasBought() {
        return wasBought;
    }

    public void setWasBought(boolean wasBought) {
        this.wasBought = wasBought;
    }

    public Boolean getOnlyForMe() {
        return onlyForMe;
    }

    public void setOnlyForMe(Boolean onlyForMe) {
        this.onlyForMe = onlyForMe;
    }

    @Override
    public String toString() {
        return "ShoppingItemDTO{" +
                "description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", homeId=" + homeId +
                ", creatorId=" + creatorId +
                ", wasBought=" + wasBought +
                '}';
    }
}
