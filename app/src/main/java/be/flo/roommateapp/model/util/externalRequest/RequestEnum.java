package be.flo.roommateapp.model.util.externalRequest;

import be.flo.roommateapp.model.dto.*;
import be.flo.roommateapp.model.dto.post.LoginDTO;
import be.flo.roommateapp.model.dto.post.RegistrationDTO;
import be.flo.roommateapp.model.dto.technical.DTO;

/**
 * Created by florian on 11/11/14.
 * List the aviable request from the server
 */
public enum RequestEnum {

    // connection
    REGISTRATION(
            RequestType.POST, false, false, "rest/registration", RegistrationDTO.class, LoginSuccessDTO.class),
    LOGIN(
            RequestType.POST, false, false, "rest/login", LoginDTO.class, LoginSuccessDTO.class),
    LOAD_DATA(
            RequestType.POST, true, false, "rest/load_data", null, LoginSuccessDTO.class),

    //ticket
    TICKET_GET(
            RequestType.GET, true, false, "rest/ticket", null, ListTicketDTO.class),
    TICKET_CREATE(
            RequestType.POST, true, false, "rest/ticket", TicketDTO.class, TicketDTO.class),
    TICKET_EDIT(
            RequestType.PUT, true, true, "rest/ticket/:param1", TicketDTO.class, TicketDTO.class),
    TICKET_REMOVE(
            RequestType.DELETE, true, true, "rest/ticket/:param1", null, ResultDTO.class),
    SHOPPING_ITEM_REMOVE(
            RequestType.DELETE, true, true, "rest/shoppingItem/:param1", null, ResultDTO.class),

    //roommate
    ROOMMATE_GET(
            RequestType.GET, true, false, "rest/roommate", null, ListRoommateDTO.class),
    ROOMMATE_CREATE(
            RequestType.POST, true, false, "rest/roommate", RoommateDTO.class, RoommateDTO.class),
    ROOMMATE_EDIT(
            RequestType.PUT, true, true, "rest/roommate/:param1", RoommateDTO.class, RoommateDTO.class),
    ROOMMATE_REMOVE(
            RequestType.DELETE, true, true, "rest/roommate/:param1", null, RoommateDTO.class),

    //shopping
    SHOPPING_ITEM_GET(
            RequestType.GET, true, false, "rest/shoppingItem", null, ListTicketDTO.class),
    SHOPPING_ITEM_CREATE(
            RequestType.POST, true, false, "rest/shoppingItem", ShoppingItemDTO.class, ShoppingItemDTO.class),
    SHOPPING_ITEM_EDIT(
            RequestType.PUT, true, true, "rest/shoppingItem/:param1", ShoppingItemDTO.class, ShoppingItemDTO.class),
    SHOPPING_ITEM_BOUGHT(
            RequestType.PUT, true, true, "rest/shoppingItem/wasBought/:param1", null, ResultDTO.class),

    //home
    HOME_EDIT(
            RequestType.PUT, true, true, "rest/home/:param1", null, HomeDTO.class);


    private final RequestType requestType;
    private final boolean needAuthentication;
    private final boolean requestId;
    private final String target;
    private final Class<? extends DTO> sentDTO;
    private final Class<? extends DTO> receivedDTO;

    private RequestEnum(RequestType requestType,
                        boolean needAuthentication,
                        boolean requestId,
                        String target,
                        Class<? extends DTO> sentDTO,
                        Class<? extends DTO> receivedDTO) {

        this.requestType = requestType;
        this.needAuthentication = needAuthentication;
        this.requestId = requestId;
        this.target = target;
        this.sentDTO = sentDTO;
        this.receivedDTO = receivedDTO;
    }

    public boolean isRequestId() {
        return requestId;
    }


    public String getTarget() {
        return target;
    }

    public Class<? extends DTO> getSentDTO() {
        return sentDTO;
    }

    public Class<? extends DTO> getReceivedDTO() {
        return receivedDTO;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public boolean needAuthentication() {
        return needAuthentication;
    }

    public static enum RequestType {
        GET, POST, PUT, DELETE
    }
}
