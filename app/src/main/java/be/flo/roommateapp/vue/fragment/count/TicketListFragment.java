package be.flo.roommateapp.vue.fragment.count;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ListTicketDTO;
import be.flo.roommateapp.model.dto.ResultDTO;
import be.flo.roommateapp.model.dto.TicketDTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.activity.edit.EditTicketActivity;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.technical.IntentBuilder;
import be.flo.roommateapp.vue.listAdapter.TicketListAdapter;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 2/11/14.
 */
public class TicketListFragment extends Fragment {

    private TicketListAdapter adapter = null;
    private View view;
    private Animation refreshAnimation;
    private Menu menu;
    //private List<TicketDTO> ticketDTOList;


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

    /**
     * create view, called the first time
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        //force the menu refresh
        getActivity().invalidateOptionsMenu();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_count_ticket_list, container, false);

        //create adapter
        //ticketDTOList = Storage.getTicketList();
        adapter = new TicketListAdapter(this.getActivity());


        ((Spinner) view.findViewById(R.id.spinner_order)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortList(SortEnum.DATE);
                } else if (position == 1) {
                    sortList(SortEnum.CATEGORY);
                } else if (position == 2) {
                    sortList(SortEnum.ROOMMATE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //recover list
        ListView listView = (ListView) view.findViewById(R.id.list_insertion);

        //add adapter
        listView.setAdapter(adapter);

        //add id
        listView.setId(R.id.text1);

        //add the contextual menu
        this.registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();

        adapter.addAll(Storage.getTicketList());

        //notify that the model changed
        adapter.notifyDataSetChanged();

        //sort
        sortList();
    }

    private enum SortEnum {
        DATE, CATEGORY, ROOMMATE
    }

    private void sortList() {
        int position = ((Spinner) view.findViewById(R.id.spinner_order)).getSelectedItemPosition();
        if (position == 0) {
            sortList(SortEnum.DATE);
        } else if (position == 1) {
            sortList(SortEnum.CATEGORY);
        } else if (position == 2) {
            sortList(SortEnum.ROOMMATE);
        }
    }

    private void sortList(SortEnum sortEnum) {

        List<TicketDTO> list = new ArrayList<>();
        for (TicketDTO ticketDTO : Storage.getTicketList()) {
            boolean added = false;
            for (int i = 0; i < list.size(); i++) {
                TicketDTO dto = list.get(i);
                if ((sortEnum == SortEnum.DATE && dto.getDate().compareTo(ticketDTO.getDate()) < 0) ||
                        (sortEnum == SortEnum.CATEGORY && (ticketDTO.getCategory() == null || (dto.getCategory() != null && dto.getCategory().toLowerCase().compareTo(ticketDTO.getCategory().toLowerCase()) > 0))) ||
                        (sortEnum == SortEnum.ROOMMATE && dto.getPayerId().compareTo(ticketDTO.getPayerId()) > 0)) {
                    list.add(i, ticketDTO);
                    added = true;
                    break;
                }
            }
            if (!added) {
                list.add(ticketDTO);
            }
        }

        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }


    /**
     * create the list of tickets
     */
    private void refreshList() {

        Log.w("test", "je suis refreshList: " + Storage.getTicketList());

        adapter.addAll(Storage.getTicketList());

        //notify that the model changed
        adapter.notifyDataSetChanged();

        //order
        sortList();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_count_resume, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.b_edit_from_count_resume:

                Intent intent = IntentBuilder.buildIntent(this, EditTicketActivity.class);
                intent.putExtra(EditTicketActivity.TICKET_ID, adapter.getItem(info.position).getId());
                startActivity(intent);

                return true;
            case R.id.b_remove_count_resume:

                //refresh the roommate list
                RemoveRequest request = new RemoveRequest(adapter.getItem(info.position));
                request.execute();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    /**
     * prepared the menu
     *
     * @param menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        assert getActivity().getActionBar() != null;

        getActivity().getActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        this.menu = menu;

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_count_ticket_list, menu);
    }

    /**
     * add event on menu item
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_add_ticket:
                startActivity(IntentBuilder.buildIntent(this, EditTicketActivity.class));
                return true;
            case R.id.b_refresh_ticket:
                RefreshRequest request = new RefreshRequest();
                request.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayRefreshIcon(boolean display) {

        if (menu.findItem(R.id.b_refresh_ticket) != null) {
            if (display) {

                // create animation and add to the refresh item
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ImageView iv = (ImageView) inflater.inflate(R.layout.loading_icon, null);
                menu.findItem(R.id.b_refresh_ticket).setActionView(iv);
                iv.startAnimation(refreshAnimation);
            } else {
                if (menu.findItem(R.id.b_refresh_ticket).getActionView() != null) {
                    menu.findItem(R.id.b_refresh_ticket).getActionView().clearAnimation();
                    menu.findItem(R.id.b_refresh_ticket).setActionView(null);
                }
            }
        }
    }

    /**
     * remove request
     */
    private class RemoveRequest extends AsyncTask<String, Void, Void> {

        private final TicketDTO ticketDTO;
        private String errorMessage;

        private RemoveRequest(TicketDTO ticketDTO) {
            this.ticketDTO = ticketDTO;
        }

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ResultDTO> webClient = new WebClient<>(getActivity(), RequestEnum.TICKET_REMOVE, ticketDTO.getId(), ResultDTO.class);
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
                DialogConstructor.displayErrorMessage(getActivity(), errorMessage);
            } else {
                adapter.remove(ticketDTO);
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

        private ListTicketDTO listTicketDTO = null;
        private String errorMessage = null;

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ListTicketDTO> webClient = new WebClient<>(getActivity(), RequestEnum.TICKET_GET, ListTicketDTO.class);
            try {
                listTicketDTO = webClient.sendRequest();

            } catch (MyException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.w("test", "je suis onPostExecute : " + listTicketDTO);

            displayRefreshIcon(false);

            if (errorMessage != null) {
                DialogConstructor.displayErrorMessage(getActivity(), errorMessage);
            } else {
                Storage.setTickets(listTicketDTO.getList());
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
