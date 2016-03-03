package be.roommate.app.model.dto;

import be.roommate.app.model.dto.technical.DTO;

/**
 * Created by florian on 27/12/14.
 */
public class FaqDTO extends DTO {

    private String question;

    private String answer;

    public FaqDTO() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "FaqDTO{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
