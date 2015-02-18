package be.flo.roommateapp.vue.fragment.shopping;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ListShoppingItemDTO;
import be.flo.roommateapp.model.dto.ResultDTO;
import be.flo.roommateapp.model.dto.ShoppingItemDTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.activity.edit.EditShoppingItemActivity;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.technical.IntentBuilder;
import be.flo.roommateapp.vue.listAdapter.ShoppingItemListAdapter;
import be.flo.roommateapp.vue.technical.navigation.MenuManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemListFragment extends Fragment {

    private ShoppingItemListAdapter adapter = null;
    private View view;
    private Animation refreshAnimation;
    private Menu menu;
    private List<ShoppingItemDTO> shoppingItemDTOList;

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
        view = inflater.inflate(R.layout.fragment_shopping, container, false);

        //create adapter
        shoppingItemDTOList = Storage.getShoppingItemNotBoughtList();
        adapter = new ShoppingItemListAdapter(this.getActivity(), shoppingItemDTOList);


        ((Spinner) view.findViewById(R.id.spinner_order)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortList(SortEnum.DATE);
                } else if (position == 1) {
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
        refreshList();
    }

    private enum SortEnum {
        DATE, ROOMMATE
    }

    private void sortList() {
        int position = ((Spinner) view.findViewById(R.id.spinner_order)).getSelectedItemPosition();
        if (position == 0) {
            sortList(SortEnum.DATE);
        } else if (position == 1) {
            sortList(SortEnum.ROOMMATE);
        }
    }

    private void sortList(SortEnum sortEnum) {

        List<ShoppingItemDTO> list = new ArrayList<>();
        for (ShoppingItemDTO shoppingItem : shoppingItemDTOList) {
            boolean added = false;
            for (int i = 0; i < list.size(); i++) {
                ShoppingItemDTO dto = list.get(i);
                if ((sortEnum == SortEnum.DATE && dto.getCreationDate().compareTo(shoppingItem.getCreationDate()) < 0) ||
                        (sortEnum == SortEnum.ROOMMATE && dto.getCreatorId().compareTo(shoppingItem.getCreatorId()) > 0)) {
                    list.add(i, shoppingItem);
                    added = true;
                    break;
                }
            }
            if (!added) {
                list.add(shoppingItem);
            }
        }

        shoppingItemDTOList.clear();
        shoppingItemDTOList.addAll(list);

        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * create the list of shopping item
     */
    private void refreshList() {

        adapter.clear();

        adapter.addAll(Storage.getShoppingItemNotBoughtList());

        //notify that the model changed
        adapter.notifyDataSetChanged();

        //sort
        sortList();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_shopping, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.b_shopping_edit:

                Intent intent = IntentBuilder.buildIntent(this,EditShoppingItemActivity.class);
                intent.putExtra(EditShoppingItemActivity.SHOPPING_ITEM_ID, adapter.getItem(info.position).getId());
                startActivity(intent);

                return true;
            case R.id.b_shopping_remove:
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
        menuInflater.inflate(R.menu.menu_shopping_item_list, menu);
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
            case R.id.b_add_shopping_item:

                startActivity(IntentBuilder.buildIntent(this,EditShoppingItemActivity.class));
                return true;
            case R.id.b_refresh_shopping_item:
                RefreshRequest request = new RefreshRequest();
                request.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayRefreshIcon(boolean display) {

        if (menu.findItem(R.id.b_refresh_shopping_item) != null) {
            if (display) {

                // create animation and add to the refresh item
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ImageView iv = (ImageView) inflater.inflate(R.layout.loading_icon, null);
                menu.findItem(R.id.b_refresh_shopping_item).setActionView(iv);
                iv.startAnimation(refreshAnimation);
            } else {
                if (menu.findItem(R.id.b_refresh_shopping_item).getActionView() != null) {
                    menu.findItem(R.id.b_refresh_shopping_item).getActionView().clearAnimation();
                    menu.findItem(R.id.b_refresh_shopping_item).setActionView(null);
                }
            }
        }
    }

    /**
     * remove request
     */
    private class RemoveRequest extends AsyncTask<String, Void, Void> {

        private final ShoppingItemDTO shoppingItemDTO;
        private String errorMessage;

        private RemoveRequest(ShoppingItemDTO shoppingItemDTO) {
            this.shoppingItemDTO = shoppingItemDTO;
        }

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ResultDTO> webClient = new WebClient<>(getActivity(),RequestEnum.SHOPPING_ITEM_REMOVE, shoppingItemDTO.getId(), ResultDTO.class);
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
                adapter.remove(shoppingItemDTO);
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

        private ListShoppingItemDTO listShoppingItemDTO = null;
        private String errorMessage = null;

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ListShoppingItemDTO> webClient = new WebClient<>(getActivity(),RequestEnum.SHOPPING_ITEM_GET, ListShoppingItemDTO.class);
            try {
                listShoppingItemDTO = webClient.sendRequest();

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
                Storage.setShoppingItems(listShoppingItemDTO.getList());
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
