package be.flo.roommateapp.vue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;
import be.flo.roommateapp.vue.technical.navigation.NavigationDrawerFragment;
import be.flo.roommateapp.vue.technical.navigation.Pager;


public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String INTENT_TAB = "intent_tab";
    public static final String INTENT_MENU = "intent_menu";

    private Integer lastMenu = null;
    private Integer lastTab = null;
    private Integer lastPositionNaviabled = null;


    private Pager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sliderBar = new SliderBar();
        super.onCreate(savedInstanceState);

        //used to display the correct tab after pause
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt(INTENT_TAB, -1) != -1) {
                lastTab = savedInstanceState.getInt(INTENT_TAB);
            }
            if (savedInstanceState.getInt(INTENT_MENU, -1) != -1) {
                lastMenu = savedInstanceState.getInt(INTENT_MENU);
            }
        }

        setContentView(R.layout.activity_main);

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //recover roommateDTO id
        Intent i = getIntent();
        if (i.getIntExtra(INTENT_MENU, -1) != -1) {
            lastMenu = i.getIntExtra(INTENT_MENU, -1);
            if (i.getIntExtra(INTENT_TAB, -1) != -1) {
                lastTab = i.getIntExtra(INTENT_TAB, -1);
            }
            mNavigationDrawerFragment.setPosition(i.getIntExtra(INTENT_MENU, -1));
            i.removeExtra(INTENT_MENU);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //reload data
        if (!Storage.testStorage()) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        //} else {
        //    super.onStart();
        //}

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        lastPositionNaviabled = position;

        pager = null;
        Fragment target;

        // update the main content by replacing fragments
        if (MenuManager.MenuElement.getByOrder(position).getSubMenuElements().length == 1) {
            target = MenuManager.MenuElement.getSubMenuElementByPosition(position, 0).getFragment();
        } else {
            if (lastMenu != null && lastMenu == position && lastTab != null) {
                pager = Pager.newInstance(position, lastTab);
                lastTab = null;
            } else {
                pager = Pager.newInstance(position, null);
            }
            target = pager;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, target);

        if (findViewById(R.id.container)!=null && ((FrameLayout) findViewById(R.id.container)).getChildCount() > 0) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (pager != null) {
            outState.putInt(INTENT_TAB, pager.getCurrentPosition());

        }
        if (lastPositionNaviabled != null) {
            outState.putInt(INTENT_MENU, lastPositionNaviabled);
        }
    }

}
