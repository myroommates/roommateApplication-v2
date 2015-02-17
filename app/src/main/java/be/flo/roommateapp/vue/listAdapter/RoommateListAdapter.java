package be.flo.roommateapp.vue.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.util.UserIcon;

import java.util.List;

/**
 * Created by florian on 18/11/14.
 * build a roommate
 */
public class RoommateListAdapter extends ArrayAdapter<RoommateDTO> {

    private Context context;


    public RoommateListAdapter(Context context) {
        super(context, R.layout.list_element_admin_roommate);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RoommateDTO dto = getItem(position);

        MenuElement element = new MenuElement(dto);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_element_admin_roommate, parent, false);

        //set text
        ((TextView) convertView.findViewById(R.id.text_email)).setText(dto.getEmail());
        ((TextView) convertView.findViewById(R.id.text_name)).setText(dto.getName());

        //generate icon
        View userIconView = UserIcon.generateUserIcon(context, dto);

        ((ViewGroup) convertView.findViewById(R.id.icon_content)).addView(userIconView, 0);

        element.setContent(convertView.findViewById(R.id.icon_content));

        return convertView;
    }

    public static class MenuElement {
        private RoommateDTO dto;
        private View content;

        public MenuElement(RoommateDTO dto) {
            this.dto = dto;
        }

        public View getContent() {
            return content;
        }

        public void setContent(View content) {
            this.content = content;
        }

        public RoommateDTO getDto() {
            return dto;
        }

        public void setDto(RoommateDTO dto) {
            this.dto = dto;
        }
    }


}
