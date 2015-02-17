package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;

import java.util.List;

/**
 * Created by florian on 19/11/14.
 */
public class ListShoppingItemDTO extends DTO {

    private List<ShoppingItemDTO> list;

    public ListShoppingItemDTO() {
    }

    public List<ShoppingItemDTO> getList() {
        return list;
    }

    public void setList(List<ShoppingItemDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListShoppingItemDTO{" +
                "list=" + list +
                '}';
    }
}
