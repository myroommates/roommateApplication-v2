package be.roommate.app.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 11/11/14.
 * tickets
 */
public class TicketDTO extends DTO {

    private Long id;

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Size(min=1,max=1000, message = "VALIDATION_SIZE")
    private String description;

    @NotNull(message = "VALIDATION_NOT_NULL")
    private Date date;

    private List<TicketDebtorDTO> debtorList;

    private String category;

    @NotNull(message = "VALIDATION_NOT_NULL")
    private Long payerId;

    private List<CommentDTO> comments;

    private Boolean hasNewComment;

    public TicketDTO() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TicketDebtorDTO> getDebtorList() {
        return debtorList;
    }

    public void setDebtorList(List<TicketDebtorDTO> debtorList) {
        this.debtorList = debtorList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public void addTicketDebtor(TicketDebtorDTO ticketDebtor) {
        if (debtorList == null) {
            debtorList = new ArrayList<>();
        }
        debtorList.add(ticketDebtor);
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", debtorList=" + debtorList +
                ", category='" + category + '\'' +
                ", payerId=" + payerId +
                '}';
    }
}
