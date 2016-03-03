package be.roommate.app.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 18/02/15.
 */
public class ContactUsDTO extends DTO{

    @NotNull(message = "VALIDATION_NOT_NULL")
    private long roommateId;

    @Size(min=1,max=255, message = "VALIDATION_SIZE")
    private String subject;

    @Size(min=1,max=3000, message = "VALIDATION_SIZE")
    private String content;

    public ContactUsDTO() {
    }

    public long getRoommateId() {
        return roommateId;
    }

    public void setRoommateId(long roommateId) {
        this.roommateId = roommateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContactUsDTO{" +
                "roommateId=" + roommateId +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
