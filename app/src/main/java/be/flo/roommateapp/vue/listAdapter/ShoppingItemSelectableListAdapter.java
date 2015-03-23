package be.flo.roommateapp.vue.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ShoppingItemDTO;
import be.flo.roommateapp.vue.activity.CommentLogActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 4/12/14.
 */
public class ShoppingItemSelectableListAdapter extends ArrayAdapter<ShoppingItemSelectableListAdapter.Model> {

    private Context context;

    public ShoppingItemSelectableListAdapter(Context context, List<ShoppingItemDTO> shoppingItemDTOs) {
        super(context, R.layout.list_element_count_balance, convert(shoppingItemDTOs));
        this.context = context;
    }

    private static List<Model> convert(List<ShoppingItemDTO> shoppingItemDTOs) {
        List<Model> items = new ArrayList<>();
        for (ShoppingItemDTO shoppingItemDTO : shoppingItemDTOs) {
            items.add(new Model(shoppingItemDTO));
        }
        return items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //load dto
        final Model model = this.getItem(position);

        //load inflater
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        //load layout
        convertView = inflater.inflate(R.layout.list_element_welcome_shopping_item_selectable, parent, false);

        //build checkbox
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.shopping_item_bought);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.selected = isChecked;
            }
        });

        //build name
        TextView textName = (TextView) convertView.findViewById(R.id.text_desc);
        textName.setText(model.dto.getDescription());

        //add comment button
        if (model.dto.getComments() != null && model.dto.getComments().size() > 0) {
            if (model.dto.getHasNewComment()) {
                ((ImageButton) convertView.findViewById(R.id.btn_comment)).setImageResource(R.drawable.comment_full_not_consulted);
            } else {
                ((ImageButton) convertView.findViewById(R.id.btn_comment)).setImageResource(R.drawable.comment_full);
            }
        } else {
            ((ImageButton) convertView.findViewById(R.id.btn_comment)).setImageResource(R.drawable.comment_empty);
        }

        convertView.findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentLogActivity.class);
                intent.putExtra(CommentLogActivity.COMMENT_TYPE, CommentLogActivity.CommentTypeEnum.SHOPPING_ITEM.name());
                intent.putExtra(CommentLogActivity.COMMENT_ASSOCIATE_ID, model.getDto().getId());

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public static class Model {
        private ShoppingItemDTO dto;
        private boolean selected = false;

        public Model(ShoppingItemDTO dto) {
            this.dto = dto;
        }

        public ShoppingItemDTO getDto() {
            return dto;
        }

        public void setDto(ShoppingItemDTO dto) {
            this.dto = dto;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
