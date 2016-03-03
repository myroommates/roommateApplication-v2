package be.roommate.app.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import be.roommate.app.R;
import be.roommate.app.model.dto.ResultDTO;
import be.roommate.app.model.dto.SurveyDTO;
import be.roommate.app.model.dto.post.SurveyResultDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;

import java.util.Map;

/**
 * Created by florian on 15/02/15.
 */
public class SurveyDialog extends Dialog implements RequestActionInterface {


    private Activity activity;
    private SurveyDTO surveyDTO;
    private boolean wasAnswered = false;

    public SurveyDialog(Activity activity, SurveyDTO surveyDTO) {
        super(activity);
        this.activity = activity;
        this.surveyDTO = surveyDTO;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        //TODO
        setTitle("Survey");
        setContentView(R.layout.dialog_survey);

        //insert question
        ((TextView) findViewById(R.id.survey_question)).setText(surveyDTO.getQuestion());

        //insert answersush
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);

        for (final Map.Entry<Long, String> answer : surveyDTO.getAnswers().entrySet()) {

            LayoutInflater layoutInflater = getLayoutInflater();
            Button button = (Button) layoutInflater.inflate(R.layout.dialog_survey_button, insertPoint, false);
            button.setText(answer.getValue());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save(answer.getKey());
                }
            });

            insertPoint.addView(button);
        }

        //add skip button
        findViewById(R.id.b_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(null);
            }
        });
    }

    @Override
    public void cancel() {

        if (!wasAnswered) {
            save(null);
        } else {
            super.cancel();
        }
    }


    protected void save(Long answer) {

        wasAnswered = true;

        SurveyResultDTO surveyResultDTO = new SurveyResultDTO();
        surveyResultDTO.setSurveyKey(surveyDTO.getSurveyKey());
        surveyResultDTO.setSurveyAnswerId(answer);

        try {

            WebClient<ResultDTO> webClient = new WebClient<>(RequestEnum.SURVEY,
                    surveyResultDTO,
                    ResultDTO.class);


            //send request
            Request request = new Request(SurveyDialog.this.getContext(),this, webClient);

            //execute request
            request.execute();

            cancel();
            Storage.clearSurvey();
            Toast.makeText(activity, R.string.survey_answer_thanks, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        //empty
    }

    @Override
    public void loadingAction(boolean loading) {
        //empty
    }

    @Override
    public void successAction(DTO successDTO) {
        //empty
    }

}
