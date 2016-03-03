package be.roommate.app.model.dto.post;

import be.roommate.app.model.dto.technical.PostDTO;

/**
 * Created by flo on 03/03/16.
 */
public class ChoreFrequencyDTO extends PostDTO {

    private String frequency;

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
