package be.roommate.app.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import be.roommate.app.R;
import be.roommate.app.model.dto.ChoreDTO;
import be.roommate.app.model.dto.RoommateDTO;
import be.roommate.app.model.util.Storage;
import be.roommate.app.model.util.UserIcon;

/**
 * Created by flo on 03/03/16.
 */
public class ChoreListAdapter extends ArrayAdapter<ChoreDTO> {

    private Context context;

    public ChoreListAdapter(Context context) {
        super(context, R.layout.list_element_chore);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChoreDTO dto = getItem(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_element_chore, parent, false);

        ((TextView) convertView.findViewById(R.id.text_name)).setText(dto.getName());

        if(dto.getDescription()!=null){
            ((TextView) convertView.findViewById(R.id.text_description)).setText(dto.getDescription());
        }

        if (dto.getCurrentExecutorId() != null) {

            RoommateDTO roommate = Storage.getRoommate(dto.getCurrentExecutorId());

            //generate icon
            View userIconView = UserIcon.generateUserIcon(context, roommate);

            ((ViewGroup) convertView.findViewById(R.id.icon_content)).addView(userIconView, 0);
        }

        return convertView;
    }
}
