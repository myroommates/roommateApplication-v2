package be.roommate.app.model.dto.list;

import be.roommate.app.model.dto.RoommateDTO;
import be.roommate.app.model.dto.technical.DTO;

import java.util.List;

/**
 * Created by florian on 19/11/14.
 */
public class ListRoommateDTO extends DTO {

    private List<RoommateDTO> list;

    public ListRoommateDTO() {
    }

    public List<RoommateDTO> getList() {
        return list;
    }

    public void setList(List<RoommateDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListRoommateDTO{" +
                "list=" + list +
                '}';
    }
}
