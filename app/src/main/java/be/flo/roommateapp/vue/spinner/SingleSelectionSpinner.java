package be.flo.roommateapp.vue.spinner;

/**
 * Created by florian on 22/11/14.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.Writable;

import java.util.ArrayList;
import java.util.List;

public class SingleSelectionSpinner<T extends Writable> extends Spinner implements OnMultiChoiceClickListener {

    private final List<T> _items = new ArrayList<T>();
    private T selected = null;

    ArrayAdapter<String> simple_adapter;

    public SingleSelectionSpinner(Context context) {
        this(context, null);
    }

    public SingleSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);

        super.setAdapter(simple_adapter);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selected = _items.get(which);
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(itemString(), this);

        //call layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_with_open_field_layout, null);

        //build view
        builder.setView(view);

        return true;
    }

    private String[] itemString() {
        String[] listString = new String[_items.size()];

        for (int i = 0; i < _items.size(); i++) {
            listString[i] = _items.get(i).getString();
        }

        return listString;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(List<T> items) {
        _items.clear();
        _items.addAll(items);
        simple_adapter.clear();
    }

    public void setSelection(int index) {
        if (index >= 0 && index < _items.size()) {
            selected = _items.get(index);
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public T getSelectedItem() {
        return selected;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (T _item : _items) {
            if (selected.equals(_item)) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_item.getString());
            }
        }
        return sb.toString();
    }

}
