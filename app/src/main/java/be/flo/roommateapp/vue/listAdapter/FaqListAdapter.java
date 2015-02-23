package be.flo.roommateapp.vue.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.FaqDTO;

/**
 * Created by florian on 18/02/15.
 */
public class FaqListAdapter extends ArrayAdapter<FaqDTO> {

    private Context context;


    public FaqListAdapter(Context context) {
        super(context, R.layout.list_element_faq);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FaqDTO dto = getItem(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_element_faq, parent, false);

        //set text
        ((TextView) convertView.findViewById(R.id.faq_question)).setText(dto.getQuestion());
        ((TextView) convertView.findViewById(R.id.faq_answer)).setText(dto.getAnswer());

        return convertView;
    }
}
