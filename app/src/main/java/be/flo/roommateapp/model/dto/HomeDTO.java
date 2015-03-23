package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.annotation.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class HomeDTO extends DTO {

    private Long id;

    @Size(min = 1, max = 3, message = R.string.verification_home_money_symbol)
    private String moneySymbol;

    private List<CommentDTO> comments;

    private Boolean hasNewComment;

    public HomeDTO() {
    }

    public Boolean getHasNewComment() {
        return hasNewComment;
    }

    public void setHasNewComment(Boolean hasNewComment) {
        this.hasNewComment = hasNewComment;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HomeDTO{" +
                "id=" + id +
                ", moneySymbol='" + moneySymbol + '\'' +
                ", comments=" + comments +
                '}';
    }
}
