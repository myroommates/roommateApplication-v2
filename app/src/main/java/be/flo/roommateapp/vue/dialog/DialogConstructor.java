package be.flo.roommateapp.vue.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.SurveyDTO;


/**
 * Created by florian on 30/11/14.
 */
public class DialogConstructor {

    public static void dialogSurvey(Activity activity, SurveyDTO surveyDTO) {

        new SurveyDialog(activity, surveyDTO).show();
    }

    public static Dialog dialogLoading(Activity activity) {

        activity.setFinishOnTouchOutside(false);

        //build the loadingDialog loading
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        //load animation for refresh button
        Animation refreshAnimation = AnimationUtils.loadAnimation(activity, R.anim.rotation);
        refreshAnimation.setRepeatCount(Animation.INFINITE);

        View loadingView = inflater.inflate(R.layout.loading_icon_black, null);
        loadingView.startAnimation(refreshAnimation);

        builder.setView(loadingView)
                .setTitle(R.string.g_loading)
                .setCancelable(false);
        return builder.create();
    }

    public static Dialog dialogCalculator(Activity activity, CalculatorEventInterface calculatorEventInterface, Double defaultValue) {

        //build the loadingDialog loading
        return new CalculatorDialog(activity, calculatorEventInterface, defaultValue);
    }

    public static Dialog dialogWarning(Activity activity, String message) {

        Dialog d = new Dialog(activity);
        TextView l = new TextView(activity);
        d.setTitle(R.string.g_warning);
        l.setPadding(15, 15, 15, 15);
        l.setText(message);
        d.setContentView(l);

        return d;
    }

    public static void displayErrorMessage(Context activity, String errorMessage) {

        final Dialog d = new Dialog(activity, R.style.dialog_error);
        d.setContentView(R.layout.dialog_error);
        d.setTitle(R.string.g_error);
        d.findViewById(R.id.b_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });
        ((TextView) d.findViewById(R.id.text)).setText(errorMessage);
        d.show();
    }

    public static void dialogChangePassword(Activity activity) {

        new ChangePasswordDialog(activity).show();
    }
}
