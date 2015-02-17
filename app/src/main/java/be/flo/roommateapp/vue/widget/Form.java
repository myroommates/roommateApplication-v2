package be.flo.roommateapp.vue.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.flo.roommateapp.model.dto.RoommateDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.StringUtil;
import be.flo.roommateapp.model.util.exception.MyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 17/11/14.
 */
public class Form extends LinearLayout implements AdapterView.OnItemSelectedListener {

    private final DTO dto;
    private final List<Field> fields = new ArrayList<>();
    private final Activity activity;
    //private EditText inputView=null;
    //private final String field;
    //private final View view;

    public Form(final Activity activity, DTO dto, Field.FieldProperties... fields) throws MyException {
        super(activity);

        //initialize fields
        this.dto = dto;

        //activity
        this.activity = activity;

        //build field
        try {
            buildField(fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildField(Field.FieldProperties... fields) throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //build layout
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //create inflater
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//(LinearLayout) findViewById(R.id.ppp);

        // **** BUILD WIDGET ****
        for (Field.FieldProperties fieldProperties : fields) {

            Method method = dto.getClass().getMethod("get" + StringUtil.toFirstLetterUpper(fieldProperties.getField().getName()));
            Field field = new Field(activity, fieldProperties, method.invoke(dto));

            addView(field);

            this.fields.add(field);
        }

    }

    public DTO control() throws Exception {

        boolean valid = true;

        // **** CONTROL ****
        for (Field field : fields) {
            if (!field.control()) {
                valid = false;
            }
        }

        if (!valid) {
            return null;
        }

        // **** INSERT DATA ****
        for (Field field : fields) {

            Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()), field.getFieldProperties().getField().getType());
            method.invoke(dto, field.getValue());
        }

        return dto;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        for (Field field : fields) {

            if (field.getFieldProperties().getInputView() instanceof Spinner &&
                    field.getFieldProperties().getInputView().equals(parent)) {

                //build DTO
                try {
                    if (field.getFieldProperties().getListEnum() == Field.ListEnum.CATEGORY) {
                        /* ??
                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(fieldProperties.columnName), String.class);
                        method.invoke(dto, ((CategoryDTO) view.getTag()).getId());
                        */
                    } else if (field.getFieldProperties().getListEnum() == Field.ListEnum.ROOMMATE) {
                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()), field.getFieldProperties().getField().getType());
                        method.invoke(dto, ((RoommateDTO) view.getTag()).getId());
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        for (Field field : fields) {

            if (field.getFieldProperties().getInputView() instanceof Spinner &&
                    field.getFieldProperties().getInputView().equals(parent)) {

                //build DTO
                try {
                    if (field.getFieldProperties().getListEnum() == Field.ListEnum.CATEGORY) {
                        /*
                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(fieldProperties.columnName), Long.class);
                        method.invoke(dto, null);
                        */
                    } else if (field.getFieldProperties().getListEnum() == Field.ListEnum.ROOMMATE) {
                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()), field.getFieldProperties().getField().getType());
                        method.invoke(dto, null);
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setEnabled(boolean enabled) {

        for (Field field : fields) {
            field.setEnabled(enabled);
        }
    }

    public Field getField(java.lang.reflect.Field name) {
        for (Field field : fields) {
            if (field.getFieldProperties().getField().equals(name)) {
                return field;
            }
        }
        return null;
    }
}
