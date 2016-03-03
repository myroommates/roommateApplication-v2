package be.roommate.app.model.dto.list;

import java.util.List;

import be.roommate.app.model.dto.ChoreDTO;
import be.roommate.app.model.dto.RoommateDTO;
import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 19/11/14.
 */
public class ListChoreDTO extends DTO {

    private List<ChoreDTO> list;

    public ListChoreDTO() {
    }

    public List<ChoreDTO> getList() {
        return list;
    }

    public void setList(List<ChoreDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListRoommateDTO{" +
                "list=" + list +
                '}';
    }
}
