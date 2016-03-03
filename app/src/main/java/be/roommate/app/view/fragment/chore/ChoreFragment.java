package be.roommate.app.view.fragment.chore;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import be.roommate.app.R;
import be.roommate.app.model.ChoreFrequencyEnum;
import be.roommate.app.model.dto.ChoreDTO;
import be.roommate.app.model.dto.ResultDTO;
import be.roommate.app.model.dto.RoommateDTO;
import be.roommate.app.model.dto.list.ListChoreDTO;
import be.roommate.app.model.dto.list.ListRoommateDTO;
import be.roommate.app.model.dto.list.ListTicketDTO;
import be.roommate.app.model.dto.post.ChoreFrequencyDTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.RequestActionInterface;
import be.roommate.app.view.activity.edit.EditChoreActivity;
import be.roommate.app.view.activity.edit.EditRoommateActivity;
import be.roommate.app.view.activity.edit.EditTicketActivity;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.listAdapter.ChoreListAdapter;
import be.roommate.app.view.listAdapter.RoommateListAdapter;
import be.roommate.app.view.technical.ExpandableHeightListView;
import be.roommate.app.view.technical.IntentBuilder;
import be.roommate.app.view.technical.navigation.MenuManager;
import be.roommate.app.view.util.Tools;

/**
 * Created by flo on 03/03/16.
 */
public class ChoreFragment extends Fragment {

    private ChoreListAdapter adapter = null;
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
        view = inflater.inflate(R.layout.fragment_chore_chore, container, false);

        //create the adapter
        adapter = new ChoreListAdapter(this.getActivity());

        //recover the list
        ExpandableHeightListView listView = (ExpandableHeightListView) view.findViewById(R.id.list_insertion);

        //add the adapter
        listView.setAdapter(adapter);

        //add the contextual menu to the list
        this.registerForContextMenu(listView);

        //help
        ((TextView) view.findViewById(R.id.help)).setText(Tools.getHelp(getActivity(), R.string.help_chore));

        //change frequency
        int index = 0;
        for (ChoreFrequencyEnum choreFrequencyEnum : ChoreFrequencyEnum.values()) {
            if (choreFrequencyEnum.toString().equals(Storage.getHome().getChoreFrequency())) {
                ((Spinner) view.findViewById(R.id.spinner_frequency)).setSelection(index);
            }
            index++;
        }

        ((Spinner) view.findViewById(R.id.spinner_frequency)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChoreFrequencyEnum choreFrequencyEnum = ChoreFrequencyEnum.values()[position];
                String currentFrequency = Storage.getHome().getChoreFrequency();
                if (!choreFrequencyEnum.toString().equals(currentFrequency)) {
                    new ChangeFrequencyRequest(choreFrequencyEnum).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new RefreshRequest().execute();
    }

    /**
     * create the menu for each menu item
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_chore, menu);
    }

    /**
     * context menu action
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.b_chore_edit:

                Intent intent = IntentBuilder.buildIntent(this, EditChoreActivity.class);
                intent.putExtra(EditChoreActivity.CHORE_ID, adapter.getItem(info.position).getId());
                startActivity(intent);

                return true;
            case R.id.b_chore_remove:

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

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        this.menu = menu;

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_chore, menu);
    }

    /**
     * ActionBar menu action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_add_chore:
                startActivity(IntentBuilder.buildIntent(this, EditChoreActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * display / hidden the refresh icon animation
     */
    private void displayRefreshIcon(boolean display) {
        /*
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
        */
    }

    /**
     * remove request
     */
    private class RemoveRequest extends AsyncTask<String, Void, Void> {

        private final ChoreDTO choreDTO;
        private String errorMessage;

        private RemoveRequest(ChoreDTO choreDTO) {
            this.choreDTO = choreDTO;
        }

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ResultDTO> webClient = new WebClient<>(RequestEnum.CHORE_REMOVE, ResultDTO.class);
            webClient.setParams("choreId", choreDTO.getId() + "");
            try {
                webClient.sendRequest(ChoreFragment.this.getActivity());
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
                RefreshRequest refreshRequest = new RefreshRequest();
                refreshRequest.execute();
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
     * remove request
     */
    private class ChangeFrequencyRequest extends AsyncTask<String, Void, Void> {

        private final ChoreFrequencyEnum choreFrequencyEnum;
        private String errorMessage;

        private ChangeFrequencyRequest(ChoreFrequencyEnum choreFrequencyEnum) {
            this.choreFrequencyEnum = choreFrequencyEnum;
        }

        @Override
        protected Void doInBackground(String... params) {

            ChoreFrequencyDTO choreFrequencyDTO = new ChoreFrequencyDTO();
            choreFrequencyDTO.setFrequency(choreFrequencyEnum.toString());

            WebClient<ResultDTO> webClient = new WebClient<>(RequestEnum.CHORE_FREQUENCY_EDIT, choreFrequencyDTO, ResultDTO.class);
            try {
                webClient.sendRequest(getActivity());
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
                Storage.getHome().setChoreFrequency(choreFrequencyEnum.toString());
                RefreshRequest refreshRequest = new RefreshRequest();
                refreshRequest.execute();
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
     * Refresh task.
     */
    private class RefreshRequest extends AsyncTask<String, Void, Void> {

        private ListChoreDTO listChoreDTO = null;
        private String errorMessage = null;

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ListChoreDTO> webClient = new WebClient<>(RequestEnum.CHORE_GET, ListChoreDTO.class);
            try {
                listChoreDTO = webClient.sendRequest(getActivity());

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
                Storage.setChores(listChoreDTO.getList());
                adapter.clear();

                List<ChoreDTO> chores = Storage.getChores();
                adapter.addAll(chores);

                //notify that the model changed
                adapter.notifyDataSetChanged();
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
