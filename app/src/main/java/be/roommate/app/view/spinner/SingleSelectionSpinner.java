package be.roommate.app.view.spinner;

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

import java.util.ArrayList;
import java.util.List;

import be.roommate.app.R;
import be.roommate.app.model.dto.Writable;

public class SingleSelectionSpinner<T extends Writable> extends Spinner implements OnMultiChoiceClickListener {

    private final List<T> _items = new ArrayList<T>();
    private T selected = null;
    private SingleSelectionSpinnerChangeListener<T> listener;

    ArrayAdapter<String> simple_adapter;

    public SingleSelectionSpinner(Context context) {
        this(context, null);
    }

    public SingleSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);//, R.style.input);


//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = (View)inflater.inflate(R.layout.single_selection_spinner, null);
//        addView(view);

        simple_adapter = new ArrayAdapter<String>(context, R.layout.simple_spinner);

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

        final AlertDialog alertDialog = builder.show();

        alertDialog.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });


        return true;
    }

    private String[] itemString() {
        String[] listString = new String[_items.size()];

        for (int i = 0; i < _items.size(); i++) {
            if(_items.get(i).getString() instanceof String){
                listString[i] = (String) _items.get(i).getString();
            }
            else if(_items.get(i).getString() instanceof Integer){
                listString[i] = getContext().getResources().getString((Integer) _items.get(i).getString());
            }

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

    public void setSelection(T selected) {
        for (T t : _items) {
            if (selected.equals(t)) {
                this.selected = t;
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
        if (listener != null) {
            listener.change(selected);
        }
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
        if (listener != null) {
            listener.change(selected);
        }
    }

    public T getSelectedItem() {
        return selected;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.size(); ++i) {
            if (selected!=null && selected.equals(_items.get(i))) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                if(_items.get(i).getString() instanceof String){
                    String string = (String) _items.get(i).getString();
                    sb.append(string);
                }
                else if(_items.get(i).getString() instanceof Integer){
                    sb.append(getContext().getResources().getString((Integer) _items.get(i).getString()));
                }
            }
        }
        String s = sb.toString();
        return s;
    }

    public String getSelectedItemsAsString() {
        return buildSelectedItemString();
    }

    public SingleSelectionSpinnerChangeListener<T> getListener() {
        return listener;
    }

    public void setListener(SingleSelectionSpinnerChangeListener<T> listener) {
        this.listener = listener;
    }

    public interface SingleSelectionSpinnerChangeListener<T extends Writable> {
        void change(T t);
    }
}
