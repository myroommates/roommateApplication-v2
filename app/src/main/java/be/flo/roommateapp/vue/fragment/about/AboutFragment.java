package be.flo.roommateapp.vue.fragment.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.vue.RequestActionInterface;

/**
 * Created by florian on 17/02/15.
 */
public class AboutFragment extends Fragment implements RequestActionInterface {

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
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        };

        String currentYear = new SimpleDateFormat("yyyy").format(new Date());

        int stringId = this.getActivity().getApplicationInfo().labelRes;
        ((TextView)view.findViewById(R.id.app_version)).setText(pInfo.versionName);
        ((TextView)view.findViewById(R.id.website)).setText("http://www.myroommatesapp.com");
        ((TextView)view.findViewById(R.id.website)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)view.findViewById(R.id.app_name)).setText("v. "+this.getActivity().getString(stringId));
        ((TextView)view.findViewById(R.id.copyright)).setText("copyright 2014-"+currentYear);


        return view;
    }
/*

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        assert getActivity().getActionBar() != null;

        getActivity().getActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_about, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_save_home:
                //save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    public void displayErrorMessage(String errorMessage) {

    }

    @Override
    public void loadingAction(boolean loading) {

    }

    @Override
    public void successAction(DTO successDTO) {

    }
}
