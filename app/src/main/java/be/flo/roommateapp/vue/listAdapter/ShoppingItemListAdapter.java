package be.flo.roommateapp.vue.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ShoppingItemDTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.UserIcon;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemListAdapter extends ArrayAdapter<ShoppingItemDTO> {

    private Context context;

    public ShoppingItemListAdapter(Context context, List<ShoppingItemDTO> items) {
        super(context, R.layout.list_element_count_balance, items);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ShoppingItemDTO dto = getItem(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_element_shopping_item, parent, false);

        ((TextView) convertView.findViewById(R.id.text1)).setText(dto.getDescription());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ((TextView) convertView.findViewById(R.id.text2)).setText(format.format(dto.getCreationDate()));


        //generate icon
        View userIconView = UserIcon.generateUserIcon(context, Storage.getRoommate(dto.getCreatorId()));

        ((ViewGroup) convertView.findViewById(R.id.icon_content)).addView(userIconView, 0);

        return convertView;
    }
}
