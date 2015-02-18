package be.flo.roommateapp.vue.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.vue.widget.Field;


/**
 * Created by florian on 30/11/14.
 */
public class DialogConstructor {

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

    public static void displayErrorMessage(Activity activity, String errorMessage) {

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
        final Dialog d = new Dialog(activity);
        d.setTitle(R.string.dialog_change_password_title);
        d.setContentView(R.layout.dialog_change_password);

        try {
            Field fieldOldPassword = new Field(activity, new Field.FieldProperties(RoommateDTO.class.getDeclaredField("password"), R.string.dialog_change_password_old_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            Field fieldNewPassword = new Field(activity, new Field.FieldProperties(RoommateDTO.class.getDeclaredField("password"), R.string.dialog_change_password_new_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));

            ((ViewGroup) d.findViewById(R.id.insert_point)).addView(fieldOldPassword);
            ((ViewGroup) d.findViewById(R.id.insert_point)).addView(fieldNewPassword);

            d.findViewById(R.id.b_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        d.show();
    }
}
