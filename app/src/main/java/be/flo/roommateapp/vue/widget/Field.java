package be.flo.roommateapp.vue.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.StringUtil;
import be.flo.roommateapp.model.util.annotation.NotNull;
import be.flo.roommateapp.model.util.annotation.Pattern;
import be.flo.roommateapp.model.util.annotation.Size;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.vue.spinner.MultiSelectionSpinner;
import be.flo.roommateapp.vue.spinner.SelectionWithOpenFieldSpinner;
import be.flo.roommateapp.vue.spinner.SingleSelectionSpinner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 17/11/14.
 */
public class Field extends LinearLayout {

    private final Activity activity;
    private FieldProperties fieldProperties;

    public Field(final Activity activity, FieldProperties fieldProperties) {
        this(activity, fieldProperties, null);
    }

    public Field(final Activity activity, FieldProperties fieldProperties, Object defaultValue) {
        super(activity);

        //activity
        this.activity = activity;
        this.fieldProperties = fieldProperties;

        //build field
        try {
            buildField(fieldProperties);
            if(defaultValue!=null){
                setValue(defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setValue(Object value) {

        if (fieldProperties.listEnum != null) {

            switch (fieldProperties.listEnum) {
                case ROOMMATE:
                    if (fieldProperties.listMultipleResponse) {

                        //insert
                        if (value != null) {
                            ((MultiSelectionSpinner<RoommateDTO>) fieldProperties.getInputView()).setSelection(Storage.getRoommates(((List<Long>) value)));
                        } else {
                            ((MultiSelectionSpinner<RoommateDTO>) fieldProperties.getInputView()).setSelection(null);
                        }

                    } else {
                        //nothing
                    }
                    break;
                case CATEGORY:
                    if (fieldProperties.listMultipleResponse) {
                        //nothing
                    } else {
                        if (value != null) {
                            ((SelectionWithOpenFieldSpinner) fieldProperties.getInputView()).setSelection(((String) value));
                        } else {
                            ((SelectionWithOpenFieldSpinner) fieldProperties.getInputView()).setSelection(null);
                        }
                    }
                    break;
            }
        } else {

            // ** DATE **
            if (fieldProperties.field.getType().isAssignableFrom(Date.class)) {
                //try to insert dto values
                ((DateView) fieldProperties.getInputView()).setDate(((Date) value));

            } else if (fieldProperties.field.getType().isAssignableFrom(Boolean.class)) {

                //try to insert dto values
                if(value!=null) {
                    ((CheckBox) fieldProperties.getInputView()).setChecked(Boolean.parseBoolean(value.toString()));
                }
            } else {

                //try to insert dto values
                if (value != null) {
                    ((EditText) fieldProperties.getInputView()).setText(value.toString());
                }
                else{
                    ((EditText) fieldProperties.getInputView()).setText("");
                }
            }
        }
    }

    private void buildField(FieldProperties fieldProperties) throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //build layout
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        //create inflater
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//(LinearLayout) findViewById(R.id.ppp);

        // **** BUILD WIDGET ****

        //for each field : create a view and insert it
        View view = inflater.inflate(R.layout.form_line, null);
        addView(view);
        View errorView = view.findViewById(R.id.field_error_message);
        //hide the errorView message
        errorView.setVisibility(GONE);

        ViewGroup contentView = (ViewGroup) view.findViewById(R.id.insert_point);

        //text view
        TextView labelText = (TextView) view.findViewById(R.id.label);//(TextView) view.findViewById(R.id.label));
        labelText.setText(fieldProperties.translationId);



        // ** LIST (for roommate and category) **
        if (fieldProperties.listEnum != null) {

            Spinner spinner = new Spinner(activity);

            switch (fieldProperties.listEnum) {
                case ROOMMATE:
                    if (fieldProperties.listMultipleResponse) {
                        spinner = new MultiSelectionSpinner<RoommateDTO>(activity);
                        ((MultiSelectionSpinner<RoommateDTO>) spinner).setItems(Storage.getRoommateList());

                    } else {
                        spinner = new SingleSelectionSpinner<RoommateDTO>(activity);
                        ((SingleSelectionSpinner<RoommateDTO>) spinner).setItems(Storage.getRoommateList());
                    }
                    break;
                case CATEGORY:
                    if (fieldProperties.listMultipleResponse) {
                        //nothing
                    } else {
                        spinner = new SelectionWithOpenFieldSpinner(activity, Storage.getCategoryList());
                    }
                    break;
            }

            fieldProperties.inputView = spinner;


        } else {

            // ** DATE **
            if (fieldProperties.field.getType().isAssignableFrom(Date.class)) {

                final DateView textView = new DateView(activity);
                fieldProperties.inputView = textView;

            } else if (fieldProperties.field.getType().isAssignableFrom(Boolean.class)) {

                //build the view
                View checkBox = new CheckBox(activity);

                //add view
                fieldProperties.inputView = checkBox;
            } else {

                // **** OTHER **** => use edit text
                //build edit text
                View editText = inflater.inflate(R.layout.field_text, null);//new EditText(activity,null,R.style.input);
                editText.getBackground().setColorFilter(R.color.t_background, PorterDuff.Mode.SRC_ATOP);

                //add param to editText
                if (fieldProperties.inputType != null) {
                    ((EditText) editText).setInputType(fieldProperties.inputType);
                } else {
                    if (fieldProperties.field.getType().isAssignableFrom(Integer.class)) {
                        ((EditText) editText).setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (fieldProperties.field.getType().isAssignableFrom(Long.class) ||
                            fieldProperties.field.getType().isAssignableFrom(Double.class)) {
                        ((EditText) editText).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    } else if (fieldProperties.field.getType().isAssignableFrom(String.class)) {
                        ((EditText) editText).setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        throw new MyException("cannot threat type " + fieldProperties.field.getType());
                    }
                }

                fieldProperties.inputView = editText;
            }


            //add to the map
            fieldProperties.errorView = errorView;
            fieldProperties.questionView = view;
        }

        contentView.addView(fieldProperties.inputView);
    }

    public boolean control() {

        boolean valid = true;

        // **** CONTROL ****

        if (fieldProperties.field.getType().isAssignableFrom(Date.class)) {
            // TODO
        } else {

            //brows annotation
            String error = null;
            if (fieldProperties.field.getDeclaredAnnotations() != null) {
                for (Annotation annotation : fieldProperties.field.getDeclaredAnnotations()) {

                    // order by priority
                    if (error == null && annotation instanceof NotNull) {
                        //control
                        if (((EditText) fieldProperties.inputView).getText() == null) {
                            error = ((NotNull) annotation).message();
                        }
                    }
                    if (error == null && annotation instanceof Size) {
                        int min = ((Size) annotation).min();
                        int max = ((Size) annotation).max();
                        Editable text = ((EditText) fieldProperties.inputView).getText();
                        if ((min > 0 && (text == null || text.toString().length() < min)) ||
                                text != null && text.toString().length() > max) {
                            error = ((Size) annotation).message();
                        }
                    }
                    if (error == null && annotation instanceof Pattern) {
                        Editable text = ((EditText) fieldProperties.inputView).getText();
                        String textInString = "";
                        if (text != null) {
                            textInString = text.toString();
                        }
                        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(((Pattern) annotation).regex());
                        if (!pattern.matcher(textInString).find()) {
                            error = ((Pattern) annotation).message();
                        }
                    }

                    if (error != null) {
                        ((TextView) fieldProperties.errorView.findViewById(R.id.field_error_message)).setText(error);
                        fieldProperties.errorView.findViewById(R.id.field_error_message).setVisibility(VISIBLE);
                        valid = false;
                    } else {
                        fieldProperties.errorView.findViewById(R.id.field_error_message).setVisibility(GONE);
                    }
                }
            }
        }

        return valid;
    }

    public Object getValue() {

        if (!control()) {
            return null;
        }

        // **** INSERT DATA ****

        if (fieldProperties.listEnum != null) {

            //build DTO
            if (fieldProperties.listMultipleResponse) {

                List<Long> listId = new ArrayList<Long>();
                switch (fieldProperties.listEnum) {

                    case ROOMMATE:
                        for (RoommateDTO roommateDTO : ((MultiSelectionSpinner<RoommateDTO>) fieldProperties.inputView).getSelectedItems()) {
                            listId.add(roommateDTO.getId());
                        }
                        break;
                    case CATEGORY:
                        //NOTHING
                        break;
                }
                return listId;

            } else {

                if (fieldProperties.listEnum.equals(ListEnum.CATEGORY)) {
                    if (((SelectionWithOpenFieldSpinner) fieldProperties.inputView).getSelectedItem() != null) {
                        return ((SelectionWithOpenFieldSpinner) fieldProperties.inputView).getSelectedItem();
                    } else {
                        // TODO method.invoke(dto, null);
                    }
                } else if (fieldProperties.listEnum.equals(ListEnum.ROOMMATE)) {
                    if (((SingleSelectionSpinner<RoommateDTO>) fieldProperties.inputView).getSelectedItem() != null) {
                        return ((SingleSelectionSpinner<RoommateDTO>) fieldProperties.inputView).getSelectedItem().getId();
                    } else {
                        // TODO method.invoke(dto, null);
                    }
                }

            }

        } else {
            if (fieldProperties.field.getType().isAssignableFrom(Date.class)) {
                //build DTO
                return ((DateView) fieldProperties.inputView).getDate();
            } else if (fieldProperties.field.getType().isAssignableFrom(Boolean.class)) {

                //build DTO
                return ((CheckBox) fieldProperties.inputView).isChecked();
            } else {
                Editable content = ((EditText) fieldProperties.inputView).getText();
                if (content != null) {

                    //build DTO
                    if (fieldProperties.field.getType() == String.class) {
                        return content.toString();
                    } else if (fieldProperties.field.getType() == Long.class) {
                        return Long.parseLong(content.toString());
                    } else if (fieldProperties.field.getType() == Integer.class) {
                        return Integer.parseInt(content.toString());
                    } else if (fieldProperties.field.getType() == Double.class) {
                        return Double.parseDouble(content.toString());
                    } else if (fieldProperties.field.getType() == Date.class) {
                        return ((DateView) content).getDate();
                    }
                }
            }
        }
        return null;
    }

    public void setEnabled(boolean enabled) {
        fieldProperties.inputView.setEnabled(enabled);
    }

    public FieldProperties getFieldProperties() {
        return fieldProperties;
    }


    public static class FieldProperties {

        private final java.lang.reflect.Field field;
        private final int translationId;
        private ListEnum listEnum;
        public boolean listMultipleResponse;


        //private elements
        private Integer inputType = null;
        private View questionView;
        private View errorView;
        private View inputView;


        public FieldProperties(java.lang.reflect.Field field, int translationId) {
            this.field = field;
            this.translationId = translationId;
        }

        public FieldProperties(java.lang.reflect.Field field, int translationId, Integer inputType) {
            this.field = field;
            this.translationId = translationId;
            this.inputType = inputType;
        }

        public FieldProperties(java.lang.reflect.Field field, int translationId, ListEnum listEnum, boolean listMultipleResponse) {
            this.field = field;
            this.translationId = translationId;
            this.listEnum = listEnum;
            this.listMultipleResponse = listMultipleResponse;
        }

        @Override
        public String toString() {
            return "FieldProperties{" +
                    ", translationId=" + translationId +
                    ", listEnum=" + listEnum +
                    ", listMultipleResponse=" + listMultipleResponse +
                    ", inputType=" + inputType +
                    ", questionView=" + questionView +
                    ", errorView=" + errorView +
                    ", inputView=" + inputView +
                    '}';
        }

        public java.lang.reflect.Field getField() {
            return field;
        }

        public View getInputView() {
            return inputView;
        }

        public ListEnum getListEnum() {
            return listEnum;
        }
    }

    public static enum ListEnum {
        ROOMMATE,
        CATEGORY
    }

}
