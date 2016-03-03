package be.roommate.app.view.fragment.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import be.roommate.app.R;
import be.roommate.app.model.util.Storage;
import be.roommate.app.view.listAdapter.FaqListAdapter;

/**
 * Created by florian on 18/02/15.
 */
public class FAQFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_about_faq, container, false);


        FaqListAdapter faqListAdapter = new FaqListAdapter(getActivity());
        faqListAdapter.addAll(Storage.getFaq());

        ListView listView = (ListView) view.findViewById(R.id.list_insertion);

        //add the adapter
        listView.setAdapter(faqListAdapter);

        return view;
    }
}
