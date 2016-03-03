package be.roommate.app.view.fragment.Welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import be.roommate.app.R;
import be.roommate.app.model.dto.TicketDTO;
import be.roommate.app.model.dto.TicketDebtorDTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.StringUtil;
import be.roommate.app.view.activity.edit.EditShoppingItemActivity;
import be.roommate.app.view.activity.edit.EditTicketActivity;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.technical.ExpandableHeightListView;
import be.roommate.app.view.technical.IntentBuilder;
import be.roommate.app.view.listAdapter.ShoppingItemSelectableListAdapter;
import be.roommate.app.view.util.Tools;

/**
 * Created by florian on 14/01/15.
 */
public class WelcomeFragment extends Fragment {

    private View view;
    private ExpandableHeightListView listShoppingItem;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        view = inflater.inflate(R.layout.fragment_welcome, container, false);

        //add button
        Button addTicketBtn = (Button) view.findViewById(R.id.welcome_add_ticket_btn);
        Button addShoppingItemBtn = (Button) view.findViewById(R.id.welcome_add_shopping_item_btn);
        Button addBoughtBtn = (Button) view.findViewById(R.id.welcome_bought_btn);

        addTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IntentBuilder.buildIntent(WelcomeFragment.this, EditTicketActivity.class));
            }
        });

        addShoppingItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(IntentBuilder.buildIntent(WelcomeFragment.this, EditShoppingItemActivity.class));
            }
        });

        addBoughtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shoppingItemString = "";

                for (int i = 0; i < listShoppingItem.getCount(); i++) {
                    if (((ShoppingItemSelectableListAdapter.Model) listShoppingItem.getItemAtPosition(i)).isSelected()) {
                        shoppingItemString += ((ShoppingItemSelectableListAdapter.Model) listShoppingItem.getItemAtPosition(i)).getDto().getId() + EditTicketActivity.TICKET_LIST_ID_SHOPPING_ITEM_SEPARATION_SYMBOL;
                    }
                }

                if (shoppingItemString.length() == 0) {

                    //display warning message
                    DialogConstructor.dialogWarning(WelcomeFragment.this.getActivity(), getString(R.string.welcome_bought_shopping_warning)).show();

                } else {

                    Intent intent = IntentBuilder.buildIntent(WelcomeFragment.this, EditTicketActivity.class);
                    intent.putExtra(EditTicketActivity.TICKET_LIST_ID_SHOPPING_ITEM, shoppingItemString);
                    startActivity(intent);
                }
            }
        });

        //display sold
        computeMyDebt();

        //send help
        ((TextView)view.findViewById(R.id.help)).setText(Tools.getHelp(getActivity(), R.string.help_welcome_fragment));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //chore
        if(Storage.getCurrentChore()!=null){
            view.findViewById(R.id.welcome_chore_container).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.welcome_chore_name)).setText(Storage.getCurrentChore().getName());
        }
        else{
            view.findViewById(R.id.welcome_chore_container).setVisibility(View.GONE);
        }

        //display shopping item list
        //create adapter
        final ShoppingItemSelectableListAdapter adapter = new ShoppingItemSelectableListAdapter(this.getActivity(), Storage.getShoppingItemList());

        //add adapter
        listShoppingItem = (ExpandableHeightListView) view.findViewById(R.id.list_insertion);

        listShoppingItem.setAdapter(adapter);

        //compute brough item visibility
        if(Storage.getShoppingItemList().size()>0){
            view.findViewById(R.id.welcome_bought_btn).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.welcome_bought_btn).setVisibility(View.GONE);
        }

        computeMyDebt();

    }

    private void computeMyDebt() {
        long mySelfId = Storage.getCurrentRoommate().getId();

        double mustPay = 0.0;
        double payed = 0.0;

        for (TicketDTO ticketDTO : Storage.getTicketList()) {
                if (ticketDTO.getDebtorList() != null) {

                for (TicketDebtorDTO ticketDebtorDTO : ticketDTO.getDebtorList()) {
                    if (ticketDTO.getPayerId().equals(mySelfId)) {
                        payed += ticketDebtorDTO.getValue();
                    }
                    if (ticketDebtorDTO.getRoommateId().equals(mySelfId)) {
                        mustPay += ticketDebtorDTO.getValue();
                    }
                }
            } else {
                //??
                Log.e("ERROR "+this.getClass().getName(),"ticket "+ticketDTO.getId()+" doesn't have any debtor !! ");
            }
        }
        double debt =  payed - mustPay;
        TextView currentSold = (TextView) view.findViewById(R.id.current_sold);
        currentSold.setText(StringUtil.toDouble(debt) + "" + Storage.getHome().getMoneySymbol());
        if (debt > 0) {
            currentSold.setTextColor(getResources().getColor(R.color.positive_value));
        } else if (debt < 0) {
            currentSold.setTextColor(getResources().getColor(R.color.negative_value));
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.g_welcome);

        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
