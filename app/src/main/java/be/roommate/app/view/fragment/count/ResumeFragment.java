package be.roommate.app.view.fragment.count;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.roommate.app.R;
import be.roommate.app.model.dto.list.ListTicketDTO;
import be.roommate.app.model.dto.RoommateDTO;
import be.roommate.app.model.dto.TicketDTO;
import be.roommate.app.model.dto.TicketDebtorDTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.StringUtil;
import be.roommate.app.model.util.UserIcon;
import be.roommate.app.model.util.exception.MyException;
import be.roommate.app.model.util.externalRequest.RequestEnum;
import be.roommate.app.model.util.externalRequest.WebClient;
import be.roommate.app.view.dialog.DialogConstructor;
import be.roommate.app.view.technical.navigation.MenuManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 2/11/14.
 */
public class ResumeFragment extends Fragment {

    private View view;
    private Animation refreshAnimation;
    ViewGroup container;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //load animation for refresh button
        refreshAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotation);
        refreshAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;

        setHasOptionsMenu(true);

        getActivity().invalidateOptionsMenu();

        //build layout
        view = inflater.inflate(R.layout.fragment_count_resume, container, false);

        draw();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        clear();
        draw();
    }

    private void draw() {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        //create the adapter
        LinearLayout listContent = (LinearLayout) view.findViewById(R.id.list_roommate_insertion);
        for (RoommateDTO roommate : Storage.getRoommateList()) {
            listContent.addView(generateRoommateView(roommate, inflater, container));
        }

        computeResult(inflater);

    }

    private void clear() {
        ((LinearLayout) view.findViewById(R.id.how_to_resolve_container)).removeAllViews();
        ((LinearLayout) view.findViewById(R.id.list_roommate_insertion)).removeAllViews();
    }

    private View generateRoommateView(RoommateDTO roommate, LayoutInflater inflater, ViewGroup container) {

        View convertView = inflater.inflate(R.layout.list_element_count_balance, container, false);

        Values values = computeBalance(roommate);

        //set text
        ((TextView) convertView.findViewById(R.id.text3)).setText(StringUtil.toDouble(values.balance) + Storage.getHome().getMoneySymbol());
        if (values.balance > 0) {
            ((TextView) convertView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.positive_value));
        } else if (values.balance < 0) {
            ((TextView) convertView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.negative_value));
        }

        ((TextView) convertView.findViewById(R.id.text1)).setText(StringUtil.toDouble(values.payed) + Storage.getHome().getMoneySymbol());
        ((TextView) convertView.findViewById(R.id.text2)).setText(StringUtil.toDouble(values.dept) + Storage.getHome().getMoneySymbol());


        //generate icon
        View userIconView = UserIcon.generateUserIcon(getActivity(), roommate);

        ((ViewGroup) convertView.findViewById(R.id.content)).addView(userIconView, 0);

        return convertView;
    }

    private Values computeBalance(RoommateDTO roommateDTO) {

        double payed = 0.0, mustPay = 0.0;

        for (TicketDTO ticketDTO : Storage.getTicketList()) {
            if (ticketDTO.getDebtorList() != null) {
                for (TicketDebtorDTO ticketDebtorDTO : ticketDTO.getDebtorList()) {
                    if (ticketDTO.getPayerId().equals(roommateDTO.getId())) {
                        payed += ticketDebtorDTO.getValue();
                    }
                    if (ticketDebtorDTO.getRoommateId().equals(roommateDTO.getId())) {
                        mustPay += ticketDebtorDTO.getValue();
                    }
                }
            } else {
                //??
                Log.e("ERROR " + this.getClass().getName(), "ticket " + ticketDTO.getId() + " doesn't have any debtor !! ");
            }
        }
        return new Values(payed, mustPay);
    }

    private static class Values {
        private double payed;
        private double dept;
        private double balance;

        public Values(double payed, double dept) {
            this.payed = payed;
            this.dept = dept;
            balance = payed - dept;
        }
    }

    private void computeResult(LayoutInflater inflater) {

        List<ResultDetail> resultDetailList = new ArrayList<>();

        for (RoommateDTO roommateDTO : Storage.getRoommateList()) {

            ResultDetail resultDetail = new ResultDetail(roommateDTO);

            for (TicketDTO ticketDTO : Storage.getTicketList()) {
                if (ticketDTO.getDebtorList() != null) {
                    for (TicketDebtorDTO ticketDebtorDTO : ticketDTO.getDebtorList()) {

                        if (ticketDTO.getPayerId().equals(roommateDTO.getId())) {
                            resultDetail.payed += ticketDebtorDTO.getValue();
                        }
                        if (ticketDebtorDTO.getRoommateId().equals(roommateDTO.getId())) {
                            resultDetail.mustPay += ticketDebtorDTO.getValue();
                        }
                    }
                } else {
                    //??
                    Log.e("ERROR " + this.getClass().getName(), "ticket " + ticketDTO.getId() + " doesn't have any debtor !! ");
                }
            }
            resultDetail.resetToReceive = resultDetail.payed - resultDetail.mustPay;

            resultDetailList.add(resultDetail);
        }

        List<Balance> balanceList = new ArrayList<>();

        for (ResultDetail resultDetail : resultDetailList) {
            double toPay = resultDetail.payed - resultDetail.mustPay;
            if (toPay < 0) {
                //I must to pay
                for (ResultDetail resultDetail2 : resultDetailList) {
                    if (resultDetail2.resetToReceive > 0) {
                        Balance balance = null;
                        for (Balance balanceToFind : balanceList) {
                            if (balanceToFind.from.equals(resultDetail.getRoommateDTO()) &&
                                    balanceToFind.to.equals(resultDetail2.getRoommateDTO())) {
                                balance = balanceToFind;
                            }
                        }
                        if (balance == null) {
                            balance = new Balance();
                            balance.from = resultDetail.getRoommateDTO();
                            balance.to = resultDetail2.getRoommateDTO();
                        }
                        balanceList.add(balance);
                        if (resultDetail2.resetToReceive >= Math.abs(toPay)) {
                            balance.value += Math.abs(toPay);
                            resultDetail2.resetToReceive -= Math.abs(toPay);
                            break;
                        } else {
                            balance.value += resultDetail2.resetToReceive;
                            toPay += resultDetail2.resetToReceive;
                            resultDetail2.resetToReceive = 0;
                        }
                    }
                }
            }
        }

        for (Balance balance : balanceList) {
            //for each field : create a view and insert it
            View listElement = inflater.inflate(R.layout.list_element_count_how_to_resolve, null);

            ((ViewGroup) listElement.findViewById(R.id.roommate_from)).addView(UserIcon.generateUserIcon(getActivity(), balance.from, false));
            ((ViewGroup) listElement.findViewById(R.id.roommate_to)).addView(UserIcon.generateUserIcon(getActivity(), balance.to, false));
            ((TextView) listElement.findViewById(R.id.text2)).setText(StringUtil.toDouble(balance.value) + "" + Storage.getHome().getMoneySymbol());


            ((LinearLayout) view.findViewById(R.id.how_to_resolve_container)).addView(listElement);
        }

    }

    public static class ResultDetail {
        private final RoommateDTO roommateDTO;

        private double payed = 0;
        private double mustPay = 0;
        private double resetToReceive = 0;

        public ResultDetail(RoommateDTO roommateDTO) {
            this.roommateDTO = roommateDTO;
        }

        public RoommateDTO getRoommateDTO() {
            return roommateDTO;
        }

    }

    private static class Balance {
        private RoommateDTO from;
        private RoommateDTO to;
        private Double value = 0.0;

        @Override
        public String toString() {
            return "Balance{" +
                    "from=" + from +
                    ", to=" + to +
                    ", value=" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Balance &&
                    ((Balance) o).from.equals(this.from) &&
                    ((Balance) o).to.equals(this.to);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MenuManager.SubMenuElement.getByClass(this.getClass()).getName());

        menu.clear();

        this.menu = menu;

        MenuInflater menuInflater = this.getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_count_resume, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.b_refresh_roommate:
                RefreshRequest request = new RefreshRequest();
                request.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
     * Refresh task.
     */
    private class RefreshRequest extends AsyncTask<String, Void, Void> {

        private ListTicketDTO listTicketDTO = null;
        private String errorMessage = null;

        @Override
        protected Void doInBackground(String... params) {

            WebClient<ListTicketDTO> webClient = new WebClient<>(RequestEnum.TICKET_GET, ListTicketDTO.class);
            try {
                listTicketDTO = webClient.sendRequest(getActivity());

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
                Storage.setTickets(listTicketDTO.getList());
                draw();
            }
        }

        @Override
        protected void onPreExecute() {
            displayRefreshIcon(true);
            clear();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //display
        }

    }
}


