package be.flo.roommateapp.model.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.RoommateDTO;


/**
 * Created by florian on 20/11/14.
 * Build a user icon
 */
public class UserIcon {


    public static View generateUserIcon(Context context, RoommateDTO roommateDTO) {
        return generateUserIcon(context, roommateDTO, false);
    }

    public static View generateUserIcon(Context context, RoommateDTO roommateDTO, boolean little) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view;
        if (little) {
            view = inflater.inflate(R.layout.user_icon_little, null);
        } else {
            view = inflater.inflate(R.layout.user_icon, null);
        }

        TextView textView = ((TextView) view.findViewById(R.id.acronym));
        textView.setText(roommateDTO.getNameAbrv());
        GradientDrawable grad = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{roommateDTO.getIconColorTop(), roommateDTO.getIconColorBottom()}
        );

        textView.setBackground(grad);

        return view;
    }


}
