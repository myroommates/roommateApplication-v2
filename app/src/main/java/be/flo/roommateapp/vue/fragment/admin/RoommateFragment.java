package be.flo.roommateapp.vue.fragment.admin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ListRoommateDTO;
import be.flo.roommateapp.model.dto.ResultDTO;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.activity.edit.EditRoommateActivity;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.listAdapter.RoommateListAdapter;
import be.flo.roommateapp.vue.technical.IntentBuilder;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;

/**
 * Created by florian on 4/12/14.
 */
public class RoommateFragment extends Fragment {

    private RoommateListAdapter adapter = null;
    private View view;
    private Animation refreshAnimation;
    private Menu menu;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load animation for refresh button
        refreshAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotation);
        refreshAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        //force the menu refresh
        getActivity().invalidateOptionsMenu();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_rommate_manager, container, false);

        //create the adapter
        adapter = new RoommateListAdapter(this.getActivity());

        //recover the list
        ListView listView = (ListView) view.findViewById(R.id.list_insertion);

        //add the adapter
        listView.setAdapter(adapter);

        //add the contextual menu to the list
        this.registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    /**
     * create the list of tickets
     */
    private void refreshList() {
        adapter.clear();

        adapter.addAll(Storage.getRoommateList());

        //notify that the model changed
        adapter.notifyDataSetChanged();
    }

    /**
     * create the menu for each menu item
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_roommate, menu);
    }

    /**
     * context menu action
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.b_remove:
                //refresh the roommate list
                RemoveRequest request = new RemoveRequest(adapter.getItem(info.position));
                request.execute();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Build the actionBar menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        assert getActivity().getActionBar() != null;

        getActivity().getActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        this.menu = menu;

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_admin_roommate, menu);
    }

    /**
     * ActionBar menu action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_add_roommate:
                startActivity(IntentBuilder.buildIntent(this, EditRoommateActivity.class));
                return true;
            case R.id.b_refresh_roommate:
                RefreshRequest request = new RefreshRequest();
                request.execute();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * display / hidden the refresh icon animation
     */
    private void displayRefreshIcon(boolean display) {

        if (menu.findItem(R.id.b_refresh_roommate) != null) {
            if (display) {

                // create animation and add to the refresh item
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ImageView iv = (ImageView) inflater.inflate(R.layout.loading_icon, null);
                menu.findItem(R.id.b_refresh_roommate).setActionView(iv);
                iv.startAnimation(refreshAnimation);
            } else {
                if (menu.findItem(R.id.b_refresh_roommate).getActionView() != null) {
                    menu.findItem(R.id.b_refresh_roommate).getActionView().clearAnimation();
                    menu.findItem(R.id.b_refresh_roommate).setActionView(null);
                }
            }
        }
    }

    /**
     * remove request
     */
    private class RemoveRequest extends AsyncTask<String, Void, Void> {

        private final RoommateDTO roommateDTO;
        private String errorMessage;

        private RemoveRequest(RoommateDTO roommateDTO) {
            this.roommateDTO = roommateDTO;
        }

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ResultDTO> webClient = new WebClient<>(getActivity(),RequestEnum.ROOMMATE_REMOVE, roommateDTO.getId(), ResultDTO.class);
            try {
                webClient.sendRequest();
            } catch (MyException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            displayRefreshIcon(false);

            if (errorMessage != null) {
                DialogConstructor.displayErrorMessage(getActivity(),errorMessage);
            } else {
                Storage.removeRoommate(roommateDTO);
                refreshList();
            }
        }

        @Override
        protected void onPreExecute() {
            displayRefreshIcon(true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //display
        }

    }

    /**
     * refresh the list request
     * call the server, load the roommate list and insert this list into Storage
     */
    private class RefreshRequest extends AsyncTask<String, Void, Void> {

        private ListRoommateDTO listRoommateDTO = null;
        private String errorMessage = null;


        @Override
        protected Void doInBackground(String... params) {

            WebClient<ListRoommateDTO> webClient = new WebClient<>(getActivity(),RequestEnum.ROOMMATE_GET, ListRoommateDTO.class);
            try {
                listRoommateDTO = webClient.sendRequest();

            } catch (MyException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            displayRefreshIcon(false);

            if (errorMessage != null) {
                DialogConstructor.displayErrorMessage(getActivity(), errorMessage);
            } else {
                Storage.setRoommate(listRoommateDTO.getList());
                refreshList();
            }
        }

        @Override
        protected void onPreExecute() {
            displayRefreshIcon(true);
            adapter.clear();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //display
        }

    }
}
