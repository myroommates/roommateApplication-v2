package be.roommate.app.view.widget;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 15/10/15.
 */
public class FieldDateView extends DateView implements InputField {

    private List<Field.HasFocusInterface> hasFocusInterfaces = new ArrayList<>();

    public FieldDateView(Activity activity) {
        this(activity, null);
    }

    public FieldDateView(Activity activity, AttributeSet attrs) {
        super(activity, attrs);

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
    public void setValue(Object value) {
        if (value != null) {
            setDate(((Date) value));
        }
    }

    @Override
    public Object getValue(Class<?> type) {
        return getDate();
    }

    @Override
    public String control(Annotation[] declaredAnnotations) {
        return null;
    }

    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
        if (getDate() != null) {
            savedInstanceState.putLong(name.toString(), getDate().getTime());
        }
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            setDate(new Date(savedInstanceState.getLong(name.toString())));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setActivated(enabled);
    }

    @Override
    public void addFocusListener(Field.HasFocusInterface hasFocusInterface,int order){
        this.hasFocusInterfaces.add(order, hasFocusInterface);
    }

    @Override
    public void addFocusListener(Field.HasFocusInterface hasFocusInterface) {
        hasFocusInterfaces.add(hasFocusInterface);
    }

    @Override
    public void clear() {

    }
}
