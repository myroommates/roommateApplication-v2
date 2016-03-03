package be.roommate.app.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 15/10/15.
 */
public class FieldCheckBox extends CheckBox implements InputField {

    private List<Field.HasFocusInterface> hasFocusInterfaces = new ArrayList<>();

    public FieldCheckBox(Context context) {
        this(context, null);
    }

    public FieldCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                for (Field.HasFocusInterface hasFocusInterface : hasFocusInterfaces) {
                    hasFocusInterface.hasFocus(hasFocus);
                }

            }
        });
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            setChecked(Boolean.parseBoolean(value.toString()));
        }
    }

    @Override
    public Object getValue(Class<?> type) {
        return isChecked();
    }

    @Override
    public String control(Annotation[] declaredAnnotations) {
        return null;
    }

    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
        savedInstanceState.putBoolean(name.toString(), this.isChecked());
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            this.setValue(savedInstanceState.getBoolean(name.toString()));
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setActivated(enabled);
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
