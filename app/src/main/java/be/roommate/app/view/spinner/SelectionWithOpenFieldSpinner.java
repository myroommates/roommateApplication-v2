package be.roommate.app.view.spinner;

/**
 * Created by florian on 22/11/14.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

import be.roommate.app.R;

public class SelectionWithOpenFieldSpinner extends Spinner implements OnMultiChoiceClickListener {

    private final List<String> _items;
    private String selected = null;
    private View view;

    ArrayAdapter<String> simple_adapter;

    public SelectionWithOpenFieldSpinner(Context context, List<String> items) {
        this(context, null, items);
    }

    public SelectionWithOpenFieldSpinner(Context context, AttributeSet attrs, List<String> items) {
        super(context, attrs);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        _items = items;

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);

        super.setAdapter(simple_adapter);
        simple_adapter.clear();
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selected = _items.get(which);
    }

    /**
     * opening the dialog
     *
     * @return
     */
    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(itemArray(), this);

        //call layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.select_with_open_field_layout, null);

        //build view
        builder.setView(view);

        //add close button
        Button button = (Button) view.findViewById(R.id.close);
        final AlertDialog alertDialog = builder.show();
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        //add valid button
        Button buttonValid = (Button) view.findViewById(R.id.valid);
        view.findViewById(R.id.valid).setEnabled(false);
        buttonValid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable text = ((EditText) view.findViewById(R.id.input)).getText();

                if (text != null && text.toString().length() > 0) {
                    selected = text.toString();
                    simple_adapter.clear();
                    simple_adapter.add(selected);
                    alertDialog.dismiss();
                }
            }
        });

        ((EditText) view.findViewById(R.id.input)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                view.findViewById(R.id.valid).setEnabled((s != null && s.length() > 0));
            }
        });

        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setSelection(String content) {
        selected = content;
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    @Override
    public void setSelection(int index) {
        selected = _items.get(index);
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    @Override
    public String getSelectedItem() {
        return selected;
    }

    private String buildSelectedItemString() {

        if (selected != null) {
            return selected;
        }

        return ((EditText) view.findViewById(R.id.input)).getText().toString();
    }


    private String[] itemArray() {
        String[] listString = new String[_items.size()];

        for (int i = 0; i < _items.size(); i++) {
            listString[i] = _items.get(i);
        }

        return listString;
    }
}