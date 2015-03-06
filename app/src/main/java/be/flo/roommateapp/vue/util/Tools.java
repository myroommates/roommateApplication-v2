package be.flo.roommateapp.vue.util;

import android.app.Activity;
import android.text.SpannableString;

import be.flo.roommateapp.R;

/**
 * Created by florian on 5/03/15.
 */
public class Tools {


    public static CharSequence getHelp(Activity activity,int textRef){
        SpannableString s = new SpannableString(activity.getString(textRef));
        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(70, 0), 0, s.length(), 0);
        return s;
    }
}
