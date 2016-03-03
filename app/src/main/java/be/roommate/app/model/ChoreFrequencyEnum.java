package be.roommate.app.model;

/**
 * Created by flo on 03/03/16.
 */
public enum ChoreFrequencyEnum {

    WEEKLY("chore_frequency_weekly"),
    TWO_WEEK("chore_frequency_two_week"),
    FOUR_WEEK("chore_frequency_four_week");

    private final String translation;

    ChoreFrequencyEnum(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
