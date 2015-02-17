package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;

/**
 * Created by florian on 14/01/15.
 */
public class TicketDebtorDTO extends DTO implements Comparable<TicketDebtorDTO>{

    private Long roommateId;

    private Double value;

    public Long getRoommateId() {
        return roommateId;
    }

    public void setRoommateId(Long roommateId) {
        this.roommateId = roommateId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TicketDebtorDTO{" +
                "value=" + value +
                ", roommateId=" + roommateId +
                '}';
    }

    @Override
    public int compareTo(TicketDebtorDTO another) {
        return this.roommateId.compareTo(another.getRoommateId());
    }
}
