package be.roommate.app.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import be.roommate.app.model.dto.Writable;
import be.roommate.app.view.spinner.SingleSelectionSpinner;

/**
 * Created by florian on 15/10/15.
 */
public class FieldSelect<T extends Writable> extends SingleSelectionSpinner<T> implements InputField {

    private List<Field.HasFocusInterface> hasFocusInterfaces = new ArrayList<>();

    public FieldSelect(Context context,  AttributeSet attrs) {
        super(context, attrs);

        this.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                for (Field.HasFocusInterface hasFocusInterface : hasFocusInterfaces) {
                    hasFocusInterface.hasFocus(hasFocus);
                }

            }
        });

    }

    @Override
    public void setValue(Object key) {
        setSelection((T) key);
    }

    @Override
    public Object getValue(Class<?> type) {
        return getSelectedItem();
    }

    @Override
    public String control(Annotation[] declaredAnnotations) {
        return null;
    }

    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
//        if (super.getSelectedItemPosition() != 0) {
            savedInstanceState.putInt(name.toString(), super.getSelectedItemPosition());
//        }
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setActivated(enabled);
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            super.setSelection(savedInstanceState.getInt(name.toString()));
        }
    }

    @Override
    public void addFocusListener(Field.HasFocusInterface hasFacousInterface,int order){
        this.hasFocusInterfaces.add(order, hasFacousInterface);
    }

    @Override
    public void addFocusListener(Field.HasFocusInterface hasFocusInterface) {
        hasFocusInterfaces.add(hasFocusInterface);
    }

    @Override
    public void clear() {

    }
}
