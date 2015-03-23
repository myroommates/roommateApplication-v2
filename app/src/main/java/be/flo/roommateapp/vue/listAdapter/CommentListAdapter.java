package be.flo.roommateapp.vue.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.CommentDTO;
import be.flo.roommateapp.model.dto.FaqDTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.UserIcon;

/**
 * Created by florian on 18/02/15.
 */
public class CommentListAdapter extends ArrayAdapter<CommentDTO> {

    private Context context;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public CommentListAdapter(Context context) {
        super(context, R.layout.list_element_comment);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommentDTO dto = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(dto.getParentId()==null){
            convertView = inflater.inflate(R.layout.list_element_comment, parent, false);
        }
        else{
            convertView = inflater.inflate(R.layout.list_element_comment_child, parent, false);
        }

        //set text
        ((TextView) convertView.findViewById(R.id.text_message)).setText(dto.getComment());
        ((TextView) convertView.findViewById(R.id.text_date)).setText(format.format(dto.getDateCreation()));

        //generate icon
        View userIconView = UserIcon.generateUserIcon(context, Storage.getRoommate(dto.getCreatorId()));

        ((ViewGroup) convertView.findViewById(R.id.icon_content)).addView(userIconView, 0);

        return convertView;
    }
}

