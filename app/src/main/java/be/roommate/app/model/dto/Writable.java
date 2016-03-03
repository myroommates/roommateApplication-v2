package be.roommate.app.model.dto;

/**
 * Created by florian on 23/11/14.
 * return a string used to display the dto content : firstName for roommate, etc.
 * used for list adapter
 */
public interface Writable {

    Object getString();
}
