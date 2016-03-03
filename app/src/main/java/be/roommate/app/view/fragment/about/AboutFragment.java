package be.roommate.app.view.fragment.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.roommate.app.R;
import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.view.RequestActionInterface;

/**
 * Created by florian on 17/02/15.
 */
public class AboutFragment extends Fragment implements RequestActionInterface {

    private static final String url ="http://www.myroommatesapp.com";

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
        ((TextView)view.findViewById(R.id.app_version)).setText("v. "+pInfo.versionName);
        ((TextView)view.findViewById(R.id.website)).setText(url);
        ((TextView)view.findViewById(R.id.website)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)view.findViewById(R.id.website)).setClickable(true);
        ((TextView)view.findViewById(R.id.website)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });


        ((TextView)view.findViewById(R.id.app_name)).setText(this.getActivity().getString(stringId));
        ((TextView)view.findViewById(R.id.copyright)).setText("copyright 2014-"+currentYear);


        return view;
    }

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
