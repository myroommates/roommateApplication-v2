package be.flo.roommateapp.vue.technical.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.flo.roommateapp.R;

/**
 * Created by florian on 3/12/14.
 */
public class Pager extends Fragment {//StatePagerAdapter {

    //private Activity activity;

    public static final String ARG_MENU_NUMBER = "ARG_MENU_NUMBER";
    public static final String STATE_SELECTED_TAB = "STATE_SELECTED_TAB";
    private static final String ARG_TAB_NUMBER = "ARG_TAB_NUMBER";

    private ViewPager mViewPager;
    private MenuManager.MenuElement menuElement;
    private int tabNumber = 0;

    public static Pager newInstance(int menuPosition, Integer lastTab) {

        Pager pager = new Pager();
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_NUMBER, menuPosition);
        if (lastTab != null) {
            args.putInt(ARG_TAB_NUMBER, lastTab);
        }
        pager.setArguments(args);
        return pager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuElement = MenuManager.MenuElement.getByOrder(getArguments().getInt(ARG_MENU_NUMBER));
        if (getArguments().getInt(ARG_TAB_NUMBER, -1) != -1) {
            tabNumber = getArguments().getInt(ARG_TAB_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, container, false);
        setHasOptionsMenu(true);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        android.support.v4.view.PagerAdapter pagerAdapter = new PagerAdapter(getActivity(), menuElement, getChildFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = ((TabLayout) view.findViewById(R.id.sliding_tabs));
        tabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(tabNumber);


        if (savedInstanceState != null) {
            int currentSelectedTab = savedInstanceState.getInt(STATE_SELECTED_TAB);
            mViewPager.setCurrentItem(currentSelectedTab);
        }
    }


    public Integer getCurrentPosition() {
        if(mViewPager==null){
            return null;
        }
        return mViewPager.getCurrentItem();
    }
}
