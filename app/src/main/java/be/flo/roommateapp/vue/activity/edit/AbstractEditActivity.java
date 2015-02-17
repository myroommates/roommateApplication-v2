package be.flo.roommateapp.vue.activity.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.activity.MainActivity;
import be.flo.roommateapp.vue.technical.AbstractActivity;
import be.flo.roommateapp.vue.widget.Form;

/**
 * Created by florian on 4/12/14.
 */
public abstract class AbstractEditActivity<T extends DTO> extends AbstractActivity implements RequestActionInterface {


    protected Form form = null;
    private Menu menu;
    private Animation refreshAnimation;
    private Integer menuSelected=null;
    private Integer tabSelected=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //create view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_basic);

        //load animation for refresh button
        refreshAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        refreshAnimation.setRepeatCount(Animation.INFINITE);

        //recover shopping item id
        Intent i = getIntent();

        //save selectedMenu
        if (i.getIntExtra(MainActivity.INTENT_MENU,-1) != -1) {
            menuSelected = i.getIntExtra(MainActivity.INTENT_MENU,-1);
        }//save selectedTab
        if (i.getIntExtra(MainActivity.INTENT_TAB,-1) != -1) {
            tabSelected = i.getIntExtra(MainActivity.INTENT_TAB,-1);
        }
    }

    /**
     * Create the menu items
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        this.menu = menu;

        assert getActionBar() != null;

        this.getActionBar().setTitle(getActivityTitle());

        menu.clear();
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    protected abstract int getActivityTitle();

    protected abstract WebClient<T> getWebClient();

    /**
     * Add event on menu items
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayErrorMessage(String errorMessage) {
        findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.error_message)).setText(errorMessage);
    }

    public void loadingAction(boolean loading) {
        if (form != null) {
            form.setEnabled(!loading);
        }
        if (loading) {
            // create animation and add to the refresh item
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.loading_icon, null);
            menu.findItem(R.id.b_save).setActionView(iv);
            iv.startAnimation(refreshAnimation);
            findViewById(R.id.error_message_container).setVisibility(View.GONE);
        } else {
            if (menu.findItem(R.id.b_save).getActionView() != null) {
                menu.findItem(R.id.b_save).getActionView().clearAnimation();
                menu.findItem(R.id.b_save).setActionView(null);
            }
        }
    }

    /**
     * save operation
     */
    protected void save() {
        try {
            //control the DTO
            DTO dto = form.control();

            if (dto != null) {

                //send request
                Request request = new Request(this, getWebClient());

                //execute request
                request.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
    }

    protected void backToMainActivity() {

        //inject item
        Intent intent =new Intent(this, MAIN_ACTIVITY);

        if(menuSelected!=null){
            intent.putExtra(MainActivity.INTENT_MENU,menuSelected);
        }

        if(tabSelected!=null){
            intent.putExtra(MainActivity.INTENT_TAB,tabSelected);
        }

        startActivity(intent);
    }

}
