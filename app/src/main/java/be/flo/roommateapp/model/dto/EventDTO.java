package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.NotNull;
import be.flo.roommateapp.model.util.annotation.Size;

import java.util.Date;

/**
 * Created by florian on 4/12/14.
 */
public class EventDTO extends DTO {

    private Long id;

    @Size(min = 1, max = 1000, message = "entre 1 et 1000 caractères")
    private String description;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    private String repeatableFrequency;

    private Long homeId;

    private Long creatorId;

    public EventDTO() {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRepeatableFrequency() {
        return repeatableFrequency;
    }

    public void setRepeatableFrequency(String repeatableFrequency) {
        this.repeatableFrequency = repeatableFrequency;
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

    @Override
    public String toString() {
        return "EventDTO{" +
                "description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", repeatableFrequency='" + repeatableFrequency + '\'' +
                ", homeId=" + homeId +
                ", creatorId=" + creatorId +
                '}';
    }
}
