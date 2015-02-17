package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.NotNull;
import be.flo.roommateapp.model.util.annotation.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 * tickets
 */
public class TicketDTO extends DTO {

    private Long id;

    @Size(min = 1, max = 1000, message = "entre 1 et 1000 caractères")
    private String description;

    @NotNull
    private Date date;

    private List<TicketDebtorDTO> debtorList;

    private String category;

    @NotNull
    private Long payerId;

    public TicketDTO() {
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
