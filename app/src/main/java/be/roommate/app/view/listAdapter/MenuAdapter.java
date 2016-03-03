package be.roommate.app.view.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import be.roommate.app.R;
import be.roommate.app.view.technical.navigation.MenuManager;

/**
 * Created by florian on 18/02/15.
 */
public class MenuAdapter extends ArrayAdapter<MenuManager.MenuElement> {

    public MenuAdapter(Context themedContext, MenuManager.MenuElement[] s) {
        super(themedContext, R.layout.list_element_menu,s);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MenuManager.MenuElement dto = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.list_element_menu, parent, false);

        //set text
        ((TextView) convertView.findViewById(R.id.text1)).setText(dto.getName());
        ((ImageView) convertView.findViewById(R.id.image1)).setImageResource(dto.getIcon());

        return convertView;
    }
}
