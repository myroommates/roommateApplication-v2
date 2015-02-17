package be.flo.roommateapp.vue.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.dto.TicketDTO;
import be.flo.roommateapp.model.dto.TicketDebtorDTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.StringUtil;
import be.flo.roommateapp.model.util.UserIcon;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by florian on 18/11/14.
 * build a roommate
 */
public class TicketListAdapter extends ArrayAdapter<TicketDTO> {

    private Context context;

    public TicketListAdapter(Context context) {
        super(context, R.layout.list_element_count_ticket);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TicketDTO dto = getItem(position);

        MenuElement element = new MenuElement(dto);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_element_count_ticket, parent, false);

        //Compute global value
        double globalValue = 0.0;

        for (TicketDebtorDTO ticketDebtor : dto.getDebtorList()) {
            globalValue += ticketDebtor.getValue();
        }

        ((TextView) convertView.findViewById(R.id.text1)).setText(dto.getDescription());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ((TextView) convertView.findViewById(R.id.text2)).setText(format.format(dto.getDate()));
        ((TextView) convertView.findViewById(R.id.text3)).setText(dto.getCategory());
        ((TextView) convertView.findViewById(R.id.text_value)).setText(StringUtil.toDouble(globalValue)+ "" + Storage.getHome().getMoneySymbol());

        if (dto.getDebtorList() != null) {
            Collections.sort(dto.getDebtorList());
            for (TicketDebtorDTO ticketDebtor : dto.getDebtorList()) {
                RoommateDTO debtor = Storage.getRoommate(ticketDebtor.getRoommateId());
                View userIconView = UserIcon.generateUserIcon(context, debtor, true);
                ((LinearLayout) convertView.findViewById(R.id.prayers_content)).addView(userIconView, 0);
            }
        }

        //generate icon
        View userIconView = UserIcon.generateUserIcon(context, Storage.getRoommate(dto.getPayerId()));

        ((LinearLayout) convertView.findViewById(R.id.icon_content)).addView(userIconView, 0);

        //add icon content to element
        element.setContent(convertView.findViewById(R.id.icon_content));


        return convertView;
    }

    public static class MenuElement {
        private TicketDTO dto;
        private View content;

        public MenuElement(TicketDTO dto) {
            this.dto = dto;
        }

        public View getContent() {
            return content;
        }

        public void setContent(View content) {
            this.content = content;
        }

        public TicketDTO getDto() {
            return dto;
        }

        public void setDto(TicketDTO dto) {
            this.dto = dto;
        }
    }


}
