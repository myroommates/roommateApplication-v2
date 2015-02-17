package be.flo.roommateapp.vue.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.RequestActionInterface;
import be.flo.roommateapp.vue.activity.LoginActivity;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

/**
 * Created by florian on 14/01/15.
 */
public class MyProfileFragment extends Fragment implements RequestActionInterface {

    private View view;
    private Form form;
    private RoommateDTO currentRoommate;
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
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);


        //load data
        currentRoommate = Storage.getCurrentRoommate();


        //build field
        try {

            //create field
            try {
                form = new Form(getActivity(), currentRoommate,
                        new Field.FieldProperties(RoommateDTO.class.getDeclaredField("name"), R.string.g_name, InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
                        new Field.FieldProperties(RoommateDTO.class.getDeclaredField("nameAbrv"), R.string.g_name_abrv),
                        new Field.FieldProperties(RoommateDTO.class.getDeclaredField("email"), R.string.g_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            //and insert field into view
            ViewGroup insertPoint = (ViewGroup) view.findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

            view.findViewById(R.id.b_logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

        } catch (MyException e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
        return view;
    }

    public void displayErrorMessage(String errorMessage) {
        view.findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.error_message)).setText(errorMessage);
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
            menu.findItem(R.id.b_save_my_profile).setActionView(iv);
            iv.startAnimation(refreshAnimation);
            view.findViewById(R.id.error_message_container).setVisibility(View.GONE);
        } else {
            if (menu.findItem(R.id.b_save_my_profile).getActionView() != null) {
                menu.findItem(R.id.b_save_my_profile).getActionView().clearAnimation();
                menu.findItem(R.id.b_save_my_profile).setActionView(null);
            }
        }
    }

    @Override
    public void successAction(DTO successDTO) {

    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        assert getActivity().getActionBar() != null;

        this.menu = menu;

        getActivity().getActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile_my_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_save_my_profile:
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

            WebClient<RoommateDTO> webClient = new WebClient<>(RequestEnum.ROOMMATE_EDIT,
                    currentRoommate,
                    currentRoommate.getId(),
                    RoommateDTO.class);

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


    public void logout() {
        //FragmentManager fm = getActivity().getSupportFragmentManager();
        //fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        /*
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        */
        Storage.clean(getActivity());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        //TaskStackBuilder.create(getActivity()).addNextIntentWithParentStack(intent).startActivities();
        //startActivityForResult(intent,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );//| FragmentManager.POP_BACK_STACK_INCLUSIVE);
        startActivity(intent);
        getActivity().finish();

    }
}
