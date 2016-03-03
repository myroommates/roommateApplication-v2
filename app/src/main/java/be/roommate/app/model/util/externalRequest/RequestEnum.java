package be.roommate.app.model.util.externalRequest;

import be.roommate.app.model.dto.*;
import be.roommate.app.model.dto.list.ListChoreDTO;
import be.roommate.app.model.dto.list.ListRoommateDTO;
import be.roommate.app.model.dto.list.ListTicketDTO;
import be.roommate.app.model.dto.post.ChoreFrequencyDTO;
import be.roommate.app.model.dto.post.ForgotPasswordDTO;
import be.roommate.app.model.dto.post.LoginDTO;
import be.roommate.app.model.dto.post.RegistrationDTO;
import be.roommate.app.model.dto.post.SurveyResultDTO;
import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 11/11/14.
 * List the aviable request from the server
 */
public enum RequestEnum {

    // connection
    GOOGLE_CONNECTION(
            RequestType.POST, false, "rest/google", GoogleConnectionDTO.class, LoginSuccessDTO.class),
    GOOGLE_REGISTRATION(
            RequestType.PUT, false, "rest/google", GoogleRegistrationDTO.class, LoginSuccessDTO.class),
    REGISTRATION(
            RequestType.POST, false, "rest/registration", RegistrationDTO.class, LoginSuccessDTO.class),
    LOGIN(
            RequestType.POST, false, "rest/login", LoginDTO.class, LoginSuccessDTO.class),
    LOAD_DATA(
            RequestType.POST, true, "rest/load_data", null, LoginSuccessDTO.class),
    LOGIN_FACEBOOK(
            RequestType.GET, false, "rest/login/facebook/:access_token/:user_id", null, LoginSuccessDTO.class),
    FORGOT_PASSWORD(
            RequestType.PUT, false, "rest/password", ForgotPasswordDTO.class, ResultDTO.class),

    //ticket
    TICKET_GET(
            RequestType.GET, true, "rest/ticket", null, ListTicketDTO.class),
    TICKET_CREATE(
            RequestType.POST, true, "rest/ticket", TicketDTO.class, TicketDTO.class),
    TICKET_EDIT(
            RequestType.PUT, true, "rest/ticket/:ticketId", TicketDTO.class, TicketDTO.class),
    TICKET_REMOVE(
            RequestType.DELETE, true, "rest/ticket/:ticketId", null, ResultDTO.class),
    TICKET_COMMENT(
            RequestType.POST, true, "rest/ticket/comment/:associateId", CommentDTO.class, CommentDTO.class),
    TICKET_COMMENT_READ(
            RequestType.PUT, true, "rest/ticket/comment/read/:associateId", null, ResultDTO.class),

    //roommate
    ROOMMATE_GET(
            RequestType.GET, true, "rest/roommate", null, ListRoommateDTO.class),
    ROOMMATE_CREATE(
            RequestType.POST, true, "rest/roommate", RoommateDTO.class, RoommateDTO.class),
    ROOMMATE_EDIT(
            RequestType.PUT, true, "rest/roommate/:roommateId", RoommateDTO.class, RoommateDTO.class),
    ROOMMATE_REMOVE(
            RequestType.DELETE, true, "rest/roommate/:roommateId", null, RoommateDTO.class),
    ROOMMATE_CHANGE_PASSWORD(
            RequestType.PUT, true, "rest/roommate/password/:roommateId", null, ResultDTO.class),

    //shopping
    SHOPPING_ITEM_GET(
            RequestType.GET, true, "rest/shoppingItem", null, ListTicketDTO.class),
    SHOPPING_ITEM_CREATE(
            RequestType.POST, true, "rest/shoppingItem", ShoppingItemDTO.class, ShoppingItemDTO.class),
    SHOPPING_ITEM_EDIT(
            RequestType.PUT, true, "rest/shoppingItem/:shoppingItemId", ShoppingItemDTO.class, ShoppingItemDTO.class),
    SHOPPING_ITEM_BOUGHT(
            RequestType.PUT, true, "rest/shoppingItem/wasBought/:shoppingItemId", null, ResultDTO.class),
    SHOPPING_COMMENT(
            RequestType.POST, true, "rest/shoppingItem/comment/:associateId", CommentDTO.class, CommentDTO.class),
    SHOPPING_COMMENT_READ(
            RequestType.PUT, true, "rest/shoppingItem/comment/read/:associateId", null, ResultDTO.class),
    SHOPPING_ITEM_REMOVE(
            RequestType.DELETE, true, "rest/shoppingItem/:shoppingItemId", null, ResultDTO.class),

    //home
    HOME_EDIT(
            RequestType.PUT, true, "rest/home/:homeId", null, HomeDTO.class),

    //COMMENT
    COMMENT_ADD(
            RequestType.POST, true, "rest/comment", CommentDTO.class, CommentDTO.class),
    COMMENT_READ(
            RequestType.POST, true, "rest/read", null, ResultDTO.class),
    COMMENT_EDIT(
            RequestType.PUT, true, "rest/comment/:associateId", CommentDTO.class, ResultDTO.class),
    COMMENT_REMOVE(
            RequestType.DELETE, true, "rest/comment/:associateId", null, ResultDTO.class),

    //CHORE
    CHORE_FREQUENCY_EDIT(
            RequestType.PUT, true, "rest/chore/frequency", ChoreFrequencyDTO.class, ResultDTO.class),
    CHORE_GET(
            RequestType.GET, true, "rest/chore", null, ListChoreDTO.class),
    CHORE_ADD(
            RequestType.POST, true, "rest/chore", ChoreDTO.class, ChoreDTO.class),
    CHORE_EDIT(
            RequestType.PUT, true, "rest/chore/:choreId", ChoreDTO.class, ChoreDTO.class),
    CHORE_REMOVE(
            RequestType.DELETE, true, "rest/chore/:choreId", null, ResultDTO.class),


    //about
    CONTACT_US(
            RequestType.POST, true, "rest/contactus", ContactUsDTO.class, ResultDTO.class),
    //survey
    SURVEY(
            RequestType.POST, true, "rest/survey", SurveyResultDTO.class, ResultDTO.class);


    private final RequestType requestType;
    private final boolean needAuthentication;
    private final String target;
    private final Class<? extends DTO> sentDTO;
    private final Class<? extends DTO> receivedDTO;

    private RequestEnum(RequestType requestType,
                        boolean needAuthentication,
                        String target,
                        Class<? extends DTO> sentDTO,
                        Class<? extends DTO> receivedDTO) {

        this.requestType = requestType;
        this.needAuthentication = needAuthentication;
        this.target = target;
        this.sentDTO = sentDTO;
        this.receivedDTO = receivedDTO;
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
