package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.Size;

/**
 * Created by florian on 11/11/14.
 */
public class HomeDTO extends DTO {

    private Long id;

    @Size(min = 1, max = 3, message = "between 1 and 3 characters")
    private String moneySymbol;

    public HomeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    @Override
    public String toString() {
        return "HomeDTO{" +
                "id=" + id +
                ", moneySymbol='" + moneySymbol + '\'' +
                '}';
    }
}
