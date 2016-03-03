package be.roommate.app.model.dto.post;

import javax.validation.constraints.NotNull;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 26/02/15.
 */
public class SurveyResultDTO extends DTO{

    @NotNull(message = "VALIDATION_NOT_NULL")
    private String surveyKey;

    private Long surveyAnswerId;

    public String getSurveyKey() {
        return surveyKey;
    }

    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }

    public Long getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(Long surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
    }
}
