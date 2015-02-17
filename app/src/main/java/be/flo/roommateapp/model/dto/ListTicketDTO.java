package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;

import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class ListTicketDTO extends DTO {

    private List<TicketDTO> list;

    public ListTicketDTO() {
    }

    public List<TicketDTO> getList() {
        return list;
    }

    public void setList(List<TicketDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListTicketDTO{" +
                "list=" + list +
                '}';
    }
}
