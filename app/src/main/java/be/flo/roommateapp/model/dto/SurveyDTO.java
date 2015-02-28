package be.flo.roommateapp.model.dto;

import be.flo.roommateapp.model.dto.technical.DTO;

import java.util.HashMap;

/**
 * Created by florian on 25/02/15.
 */
public class SurveyDTO extends DTO {

    private String question;

    private String surveyKey;

    private HashMap<Long, String> answers;

    private boolean multipleAnswers;

    public SurveyDTO() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSurveyKey() {
        return surveyKey;
    }

    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }

    public HashMap<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Long, String> answers) {
        this.answers = answers;
    }

    public boolean isMultipleAnswers() {
        return multipleAnswers;
    }

    public void setMultipleAnswers(boolean multipleAnswers) {
        this.multipleAnswers = multipleAnswers;
    }

    @Override
    public String toString() {
        return "SurveyDTO{" +
                "question='" + question + '\'' +
                ", surveyKey='" + surveyKey + '\'' +
                ", answers=" + answers +
                ", multipleAnswers=" + multipleAnswers +
                '}';
    }
}
