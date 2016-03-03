package be.roommate.app.view.technical;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import be.roommate.app.view.activity.MainActivity;
import be.roommate.app.view.technical.navigation.MenuManager;

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
