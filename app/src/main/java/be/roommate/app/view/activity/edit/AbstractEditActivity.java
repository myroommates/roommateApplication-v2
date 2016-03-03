package be.roommate.app.view.activity.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import be.roommate.app.R;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.activity.MainActivity;
import be.roommate.app.view.activity.WelcomeActivity;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 4/12/14.
 */
public abstract class AbstractEditActivity<T extends DTO> extends ActionBarActivity implements RequestActionInterface {


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

        //mask keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        this.getSupportActionBar().setTitle(getActivityTitle());

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
            case android.R.id.home:
                startActivity(new Intent(AbstractEditActivity.this, MainActivity.class));
                return true;
            case R.id.b_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(this, errorMessage);
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
                Request request = new Request(AbstractEditActivity.this,this, getWebClient());

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
        Intent intent =new Intent(this, MainActivity.class);

        if(menuSelected!=null){
            intent.putExtra(MainActivity.INTENT_MENU,menuSelected);
        }

        if(tabSelected!=null){
            intent.putExtra(MainActivity.INTENT_TAB,tabSelected);
        }

        startActivity(intent);
    }

}
