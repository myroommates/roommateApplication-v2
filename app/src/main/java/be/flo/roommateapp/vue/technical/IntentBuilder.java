package be.flo.roommateapp.vue.technical;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import be.flo.roommateapp.vue.activity.MainActivity;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;

/**
 * Created by florian on 5/02/15.
 */
public class IntentBuilder {


    public static Intent buildIntent(Fragment fragment,Class<? extends Activity> activityClass){
        Intent intent = new Intent(fragment.getActivity(), activityClass);

        //add menu parameter
        intent.putExtra(MainActivity.INTENT_MENU, MenuManager.MenuElement.getByClass(fragment.getClass()).getOrder());
        intent.putExtra(MainActivity.INTENT_TAB,MenuManager.SubMenuElement.getByClass(fragment.getClass()).getOrder());

        return intent;
    }
}
