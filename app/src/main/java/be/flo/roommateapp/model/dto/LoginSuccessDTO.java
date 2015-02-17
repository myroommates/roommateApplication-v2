package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class LoginSuccessDTO extends DTO {

    private RoommateDTO currentRoommate;
    private HomeDTO home;
    private List<RoommateDTO> roommates;
    private List<TicketDTO> tickets;
    private List<ShoppingItemDTO> shoppingItems;
    private String authenticationKey;

    public LoginSuccessDTO() {
    }

    public List<ShoppingItemDTO> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(List<ShoppingItemDTO> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public RoommateDTO getCurrentRoommate() {
        return currentRoommate;
    }

    public void setCurrentRoommate(RoommateDTO currentRoommate) {
        this.currentRoommate = currentRoommate;
    }

    public HomeDTO getHome() {
        return home;
    }

    public void setHome(HomeDTO home) {
        this.home = home;
    }

    public List<RoommateDTO> getRoommates() {
        return roommates;
    }

    public void setRoommates(List<RoommateDTO> roommates) {
        this.roommates = roommates;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(TicketDTO ticket) {
        if (tickets == null) {
            tickets = new ArrayList<TicketDTO>();
        }
        tickets.add(ticket);
    }

    public void addRoommate(RoommateDTO roommate) {
        if (roommates == null) {
            roommates = new ArrayList<RoommateDTO>();
        }
        roommates.add(roommate);
    }

    @Override
    public String toString() {
        return "LoginSuccessDTO{" +
                "currentRoommate=" + currentRoommate +
                ", home=" + home +
                ", roommates=" + roommates +
                ", tickets=" + tickets +
                ", shoppingItems=" + shoppingItems +
                ", authenticationKey='" + authenticationKey + '\'' +
                '}';
    }
}
