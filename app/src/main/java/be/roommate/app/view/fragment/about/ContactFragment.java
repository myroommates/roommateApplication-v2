package be.roommate.app.view.fragment.about;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import be.roommate.app.R;
import be.roommate.app.model.dto.ContactUsDTO;
import be.roommate.app.model.dto.ResultDTO;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.Request;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.technical.navigation.MenuManager;
import be.roommate.app.view.widget.Field;
import be.roommate.app.view.widget.Form;

/**
 * Created by florian on 18/02/15.
 */
public class ContactFragment extends Fragment implements RequestActionInterface {

    private Animation refreshAnimation;
    private ContactUsDTO contactUsDTO;
    private Form form;
    private Menu menu;

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
        View view = inflater.inflate(R.layout.fragment_about_contactus, container, false);

        contactUsDTO = new ContactUsDTO();

        try {

            //create field
            form = new Form(getActivity(), contactUsDTO,
                    new Field.FieldProperties(ContactUsDTO.class.getDeclaredField("subject"), R.string.g_subject,
                            InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES),
                    new Field.FieldProperties(ContactUsDTO.class.getDeclaredField("content"), R.string.g_message,
                            InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE));
            form.intialize();

            //and insert field into view
            ViewGroup insertPoint = (ViewGroup) view.findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException | NoSuchFieldException e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }

        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        this.menu = menu;

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_about_contact_us, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_about_contact_us_send:
                send();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * save operation
     */
    protected void send() {
        try {

            WebClient<ResultDTO> webClient = new WebClient<>(RequestEnum.CONTACT_US,
                    contactUsDTO,
                    ResultDTO.class);

            //control the DTO
            DTO dto = form.control();

            if (dto != null) {

                //send request
                Request request = new Request(ContactFragment.this.getActivity(),this, webClient);

                //execute request
                request.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(getActivity(), errorMessage);
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
            menu.findItem(R.id.b_about_contact_us_send).setActionView(iv);
            iv.startAnimation(refreshAnimation);
        } else {
            if (menu.findItem(R.id.b_about_contact_us_send).getActionView() != null) {
                menu.findItem(R.id.b_about_contact_us_send).getActionView().clearAnimation();
                menu.findItem(R.id.b_about_contact_us_send).setActionView(null);
            }
        }
    }

    @Override
    public void successAction(DTO successDTO) {
        //clear fields
        form.clearFields();
        Toast.makeText(getActivity(), R.string.about_contact_us_success, Toast.LENGTH_LONG).show();
    }
}
