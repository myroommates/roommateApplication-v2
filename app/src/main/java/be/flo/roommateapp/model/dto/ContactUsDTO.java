package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.NotNull;
import be.flo.roommateapp.model.util.annotation.Size;

/**
 * Created by florian on 18/02/15.
 */
public class ContactUsDTO extends DTO{
    @NotNull
    private long roommateId;

    @Size(min=1,max=255,message = R.string.verification_contact_us_subject)
    private String subject;

    @Size(min=1,max=3000,message = R.string.verification_contact_us_content)
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
