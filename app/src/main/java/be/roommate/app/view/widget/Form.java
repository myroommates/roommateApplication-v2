package be.roommate.app.view.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import be.roommate.app.model.dto.technical.DTO;
import be.roommate.app.model.util.StringUtil;
import be.roommate.app.model.util.exception.MyException;


/**
 * Created by florian on 17/11/14.
 */
public class Form extends LinearLayout {//implements AdapterView.OnItemSelectedListener {

    private DTO dto;
    private final List<Field> fields = new ArrayList<Field>();
    private final Activity activity;
    private Field.FieldProperties[] fieldProperties;
    private boolean placeholder;
    //private EditText inputView=null;
    //private final String field;
    //private final View view;

    public Form(final Activity activity, DTO dto, Field.FieldProperties... fields) throws MyException {
        this(activity, dto, false, fields);
    }


    public Form(final Activity activity, DTO dto, boolean placeholder, Field.FieldProperties... fieldProperties) throws MyException {
        super(activity);

        //initialize fields
        this.dto = dto;
        this.placeholder = placeholder;

        //activity
        this.activity = activity;

        this.fieldProperties = fieldProperties;
    }

    public void intialize() {

        //build field
        try {
            buildFields(fieldProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildFields(final Field.FieldProperties... fields) throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //build layout
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //create inflater
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//(LinearLayout) findViewById(R.id.ppp);



        // **** BUILD WIDGET ****
        for (Field.FieldProperties fieldProperties : fields) {
            fieldProperties.setPlaceholder(placeholder);


            Object invoke = null;
            //set value
            if (fieldProperties.getField() != null) {
                Method method = dto.getClass().getMethod("get" + StringUtil.toFirstLetterUpper(fieldProperties.getField().getName()));
                if (method == null) {
                    method = dto.getClass().getSuperclass().getMethod("get" + StringUtil.toFirstLetterUpper(fieldProperties.getField().getName()));

                }
                invoke = method.invoke(dto);
            }
            Field field = new Field(activity, fieldProperties, invoke);

            addView(field);


            this.fields.add(field);
        }

    }

    public void refreshFromDTO() throws MyException {
        try {
            for (Field field : fields) {
                Object invoke = null;
                //set value
                if (field.getFieldProperties().getField() != null) {
                    Method method = dto.getClass().getMethod("get" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()));
                    if (method == null) {
                        method = dto.getClass().getSuperclass().getMethod("get" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()));

                    }
                    invoke = method.invoke(dto);
                    field.setValue(invoke);
                }
            }
            control();
        } catch (Exception e) {
            throw new MyException(e.getMessage());
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

            if (field.getFieldProperties().getField() != null) {
                Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()), field.getFieldProperties().getField().getType());
                method.invoke(dto, field.getValue());
            }
        }

        return dto;
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        for (Field field : fields) {
//
//            if (field.getFieldProperties().getInputView() instanceof Spinner &&
//                    field.getFieldProperties().getInputView().equals(parent)) {
//
//                //build DTO
////                try {
////                    if (field.getFieldProperties().getListEnum() == Field.ListEnum.CATEGORY) {
////                        /* ??
////                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(fieldProperties.columnName), String.class);
////                        method.invoke(dto, ((CategoryDTO) view.getTag()).getId());
////                        */
////                    } else if (field.getFieldProperties().getListEnum() == Field.ListEnum.ROOMMATE) {
////                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()), field.getFieldProperties().getField().getType());
////                        method.invoke(dto, ((RoommateDTO) view.getTag()).getId());
////                    }
////                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        for (Field field : fields) {
//
//            if (field.getFieldProperties().getInputView() instanceof Spinner &&
//                    field.getFieldProperties().getInputView().equals(parent)) {
//
////                //build DTO
////                try {
////                    if (field.getFieldProperties().getListEnum() == Field.ListEnum.CATEGORY) {
////                        /*
////                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(fieldProperties.columnName), Long.class);
////                        method.invoke(dto, null);
////                        */
////                    } else if (field.getFieldProperties().getListEnum() == Field.ListEnum.ROOMMATE) {
////                        Method method = dto.getClass().getMethod("set" + StringUtil.toFirstLetterUpper(field.getFieldProperties().getField().getName()), field.getFieldProperties().getField().getType());
////                        method.invoke(dto, null);
////                    }
////                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//    }

    public void setEnabled(boolean enabled) {

        for (Field field : fields) {
            field.setEnabled(enabled);
        }
    }

    public Field getField(int translationName) {
        for (Field field : fields) {
            if (field.getFieldProperties().getTranslationId() == translationName) {
                return field;
            }
        }
        return null;
    }

    public void saveToInstanceState(Bundle savedInstanceState) {
        for (Field field : fields) {
            field.saveToInstanceState(savedInstanceState);
        }

    }

    public void restoreFromInstanceState(Bundle savedInstanceState) {
        for (Field field : fields) {
            field.restoreFromInstanceState(savedInstanceState);
        }
    }

    public void setDto(DTO dto) {
        if (this.dto.getClass().equals(dto.getClass())) {
            this.dto = dto;
        }
    }

    public void clearFields() {
        for (Field field : fields) {
            field.clear();
        }
    }
}
