package be.roommate.app.model.dto;


import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 18/03/15.
 */
public class CommentDTO extends DTO implements Comparable<CommentDTO>{

    private Long id;

    @NotNull(message = "VALIDATION_NOT_NULL")
    @Size(min=1,max=1000, message = "VALIDATION_SIZE")
    private String comment;

    private Long creatorId;

    private Date dateCreation;

    private Date dateEdit;

    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", creatorId=" + creatorId +
                ", dateCreation=" + dateCreation +
                ", dateEdit=" + dateEdit +
                ", parentId=" + parentId +
                '}';
    }

    @Override
    public int compareTo(CommentDTO another) {
        return this.getDateCreation().compareTo(another.getDateCreation());
    }
}
