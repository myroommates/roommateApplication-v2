package be.roommate.app.view.widget;

import android.os.Bundle;

import java.lang.annotation.Annotation;

/**
 * Created by florian on 15/10/15.
 */
public interface InputField {

    public void setValue(Object value);

    Object getValue(Class<?> type);

    void setEnabled(boolean enabled);

    String control(Annotation[] declaredAnnotations);

    void saveToInstanceState(Integer name, Bundle savedInstanceState);

    void restoreFromInstanceState(Integer name, Bundle savedInstanceState);

    void addFocusListener(Field.HasFocusInterface hasFocusInterface, int order);

    void addFocusListener(Field.HasFocusInterface hasFocusInterface);

    void clear();
}
