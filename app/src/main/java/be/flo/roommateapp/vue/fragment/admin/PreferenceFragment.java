package be.flo.roommateapp.vue.fragment.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.HomeDTO;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

/**
 * Created by florian on 14/01/15.
 */
public class PreferenceFragment extends Fragment implements RequestActionInterface {

    private Form form;
    private HomeDTO home;
    private Menu menu;
    private Animation refreshAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //create view
        super.onCreate(savedInstanceState);

        //load animation for refresh button
        refreshAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
        refreshAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        getActivity().invalidateOptionsMenu();

        //build layout
        View view = inflater.inflate(R.layout.fragment_admin_preference, container, false);


        //load data
        home = Storage.getHome();


        //build field
        try {

            //create field
            try {
                form = new Form(getActivity(), home,
                        new Field.FieldProperties(HomeDTO.class.getDeclaredField("moneySymbol"), R.string.g_money)
                );
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            //and insert field into view
            ViewGroup insertPoint = (ViewGroup) view.findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
        return view;
    }

    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(getActivity(),errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {
        if (form != null) {
            form.setEnabled(!loading);
        }
        if (loading) {
            // create animation and add to the refresh item
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.loading_icon, null);
            menu.findItem(R.id.b_save_home).setActionView(iv);
            iv.startAnimation(refreshAnimation);
        } else {
            if (menu.findItem(R.id.b_save_home).getActionView() != null) {
                menu.findItem(R.id.b_save_home).getActionView().clearAnimation();
                menu.findItem(R.id.b_save_home).setActionView(null);
            }
        }
    }

    @Override
    public void successAction(DTO successDTO) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.g_save_data_success),Toast.LENGTH_SHORT).show();
        Storage.setHome(((HomeDTO) successDTO));
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        assert getActivity().getActionBar() != null;

        this.menu = menu;

        getActivity().getActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_admin_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_save_home:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * save operation
     */
    protected void save() {
        try {

            WebClient<HomeDTO> webClient = new WebClient<>(getActivity(),RequestEnum.HOME_EDIT,
                    home,
                    home.getId(),
                    HomeDTO.class);

            //control the DTO
            DTO dto = form.control();

            if (dto != null) {

                //send request
                Request request = new Request(this, webClient);

                //execute request
                request.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
    }
}
