package be.roommate.app.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.DTO;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class HomeDTO extends DTO {

    private Long id;

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Size(min = 1, max = 3, message = "VALIDATION_SIZE")
    private String moneySymbol;

    private List<CommentDTO> comments;

    private Boolean hasNewComment;

    private String choreFrequency;

    public HomeDTO() {
    }

    public String getChoreFrequency() {
        return choreFrequency;
    }

    public void setChoreFrequency(String choreFrequency) {
        this.choreFrequency = choreFrequency;
    }

    public Boolean getHasNewComment() {
        return hasNewComment;
    }

    public void setHasNewComment(Boolean hasNewComment) {
        this.hasNewComment = hasNewComment;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    @Override
    public String toString() {
        return "HomeDTO{" +
                "id=" + id +
                ", moneySymbol='" + moneySymbol + '\'' +
                '}';
    }
}
