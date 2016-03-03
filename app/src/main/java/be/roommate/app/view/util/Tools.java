package be.roommate.app.view.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.text.SpannableString;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by florian on 5/03/15.
 */
public class Tools {


    public static CharSequence getHelp(Activity activity,int textRef){
        SpannableString s = new SpannableString(activity.getString(textRef));
        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(70, 0), 0, s.length(), 0);
        return s;
    }
    public static List<String> getAccountEmails(Activity activity){
        List<String> emails = new ArrayList<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(activity).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }
        return emails;
    }
}
