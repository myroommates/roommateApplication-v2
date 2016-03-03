package be.roommate.app.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import be.roommate.app.R;
import be.roommate.app.model.util.exception.MyException;


/**
 * Created by florian on 15/10/15.
 */
public class FieldEditText extends RelativeLayout implements InputField {

    private Integer inputType = null;
    private List<Field.HasFocusInterface> hasFocusInterfaces =new ArrayList<>();
    private boolean isValid=false;

    public FieldEditText(Context context) {
        this(context, (AttributeSet) null);
    }

    public FieldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ((AutoCompleteTextView) findViewById(R.id.text_field)).setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                if (inputType != null) {
                    ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(inputType);
                }
                TransformationMethod transformationMethod = ((AutoCompleteTextView) findViewById(R.id.text_field)).getTransformationMethod();
                if (((AutoCompleteTextView) findViewById(R.id.text_field)).getTransformationMethod() instanceof PasswordTransformationMethod) {

                    findViewById(R.id.field_text_show_password).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            findViewById(R.id.field_text_show_password).setVisibility(GONE);
                            findViewById(R.id.field_text_hide_password).setVisibility(VISIBLE);
                            ((AutoCompleteTextView) findViewById(R.id.text_field)).setTransformationMethod(null);
                        }
                    });
                    findViewById(R.id.field_text_hide_password).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            findViewById(R.id.field_text_hide_password).setVisibility(GONE);
                            findViewById(R.id.field_text_show_password).setVisibility(VISIBLE);
                            ((AutoCompleteTextView) findViewById(R.id.text_field)).setTransformationMethod(new PasswordTransformationMethod());
                        }
                    });
                }

                findViewById(R.id.text_field).setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        for (Field.HasFocusInterface hasFocusInterface : hasFocusInterfaces) {
                            hasFocusInterface.hasFocus(hasFocus);
                        }

                        if (((AutoCompleteTextView) findViewById(R.id.text_field)).getTransformationMethod() instanceof PasswordTransformationMethod) {
                            if (hasFocus) {
                                findViewById(R.id.field_text_show_password).setVisibility(VISIBLE);
                            } else {
                                findViewById(R.id.field_text_show_password).setVisibility(GONE);
                            }
                        }
                    }
                });
            }
        });
    }

    public void addFocusListener(Field.HasFocusInterface hasFacousInterface,int order){
        this.hasFocusInterfaces.add(order, hasFacousInterface);
    }

    public void addFocusListener(Field.HasFocusInterface hasFacousInterface){
        this.hasFocusInterfaces.add(hasFacousInterface);
    }

    @Override
    public void clear() {
        ((AutoCompleteTextView) findViewById(R.id.text_field)).setText(null);
    }


    public FieldEditText(Context context, final Integer inputType) {
        super(context, null);
        this.inputType = inputType;
    }

    @Override
    public void setEnabled(boolean enabled) {
        findViewById(R.id.text_field).setEnabled(enabled);
        findViewById(R.id.text_field).setActivated(enabled);
        findViewById(R.id.text_field).setFocusableInTouchMode(enabled);
        if(!enabled) {
            findViewById(R.id.text_field).setFocusable(false);
        }
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            ((AutoCompleteTextView) findViewById(R.id.text_field)).setText(value.toString());
        } else {
            ((AutoCompleteTextView) findViewById(R.id.text_field)).setText("");
        }
    }

    @Override
    public Object getValue(Class<?> type) {
        Editable content = ((AutoCompleteTextView) findViewById(R.id.text_field)).getText();
        if (content != null && content.length()>0) {

            if (type == String.class) {
                return content.toString();
            } else if (type == Long.class) {
                return Long.parseLong(content.toString());
            } else if (type == Integer.class) {
                return Integer.parseInt(content.toString());
            } else if (type == Double.class) {
                return Double.parseDouble(content.toString());
            } else if (type == Date.class) {
                return ((DateView) content).getDate();
            }
        }
        return null;
    }

    @Override
    public String control(Annotation[] declaredAnnotations) {

        //edit
        if (((AutoCompleteTextView) findViewById(R.id.text_field)).getText() != null && inputType != null && inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
            String t = ((AutoCompleteTextView) findViewById(R.id.text_field)).getText().toString();
            if(t.length() > 0){
                if(t.charAt(t.length() - 1) == ' ') {
                    t = t.substring(0,t.length() - 1);
                }
                if(t.charAt(0) == ' '){
                    t = t.substring(1,t.length());
                }
                ((AutoCompleteTextView) findViewById(R.id.text_field)).setText(t);
            }

        }

        //brows annotation
        String error = null;
        if (declaredAnnotations != null) {
            for (Annotation annotation : declaredAnnotations) {

                // order by priority
                if (error == null && annotation instanceof NotNull) {
                    //control
                    if (((AutoCompleteTextView) findViewById(R.id.text_field)).getText() == null) {
                        error = getStringResourceByName(getContext(), ((NotNull) annotation).message());
                    }
                }
                if (error == null && annotation instanceof Size) {
                    int min = ((Size) annotation).min();
                    int max = ((Size) annotation).max();
                    Editable text = ((AutoCompleteTextView) findViewById(R.id.text_field)).getText();
                    if ((min > 0 && (text == null || text.toString().length() < min)) ||
                            text != null && text.toString().length() > max) {
                        error = getStringResourceByName(getContext(), ((Size) annotation).message());
                        error = error.replace("{0}",min+"");
                        error = error.replace("{1}",max+"");
                    }
                }
                if (error == null && annotation instanceof Pattern) {
                    Editable text = ((AutoCompleteTextView) findViewById(R.id.text_field)).getText();
                    String textInString = "";
                    if (text != null) {
                        textInString = text.toString();
                    }
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(((Pattern) annotation).regexp());
                    if (!pattern.matcher(textInString).find()) {
                        error = getStringResourceByName(getContext(), ((Pattern) annotation).message());
                    }
                }
                if (error == null && annotation instanceof Min) {
                    Editable text = ((AutoCompleteTextView) findViewById(R.id.text_field)).getText();
                    try {
                        Double number = Double.parseDouble(text.toString());
                        if (number == null || number < ((Min) annotation).value()) {
                            error = getStringResourceByName(getContext(), ((Min) annotation).message());
                        }
                    } catch (NumberFormatException e) {
                        error = getStringResourceByName(getContext(), ((Min) annotation).message());
                    }
                }
                if (error == null && annotation instanceof Max) {
                    Editable text = ((AutoCompleteTextView) findViewById(R.id.text_field)).getText();
                    try {
                        Double number = Double.parseDouble(text.toString());
                        if (number == null || number > ((Max) annotation).value()) {
                            error = getStringResourceByName(getContext(), ((Max) annotation).message());
                        }
                    } catch (NumberFormatException e) {
                        error = getStringResourceByName(getContext(), ((Max) annotation).message());
                    }
                }
            }
        }

        this.isValid = error == null || error.length() == 0;


        return error;
    }


    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
        if (findViewById(R.id.text_field)!=null && ((AutoCompleteTextView) findViewById(R.id.text_field)).getText() != null && !((AutoCompleteTextView) findViewById(R.id.text_field)).getText().toString().equals("")) {
            savedInstanceState.putString(name.toString(), ((AutoCompleteTextView) findViewById(R.id.text_field)).getText().toString());
        }
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            setValue(savedInstanceState.getString(name.toString()));
        }
    }

    //TODO ??
    public void setAutoCompleteValues(List<String> list) {
        String[] values = new String[list.size()];
        values = list.toArray(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, values);
        ((AutoCompleteTextView) findViewById(R.id.text_field)).setAdapter(adapter);
    }

    public void addSetInputs(Integer inputType, Class<?> type, boolean multiline) throws MyException {
        //add param to editText
        if (inputType != null) {
            ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(inputType);
        } else {
            if (type.isAssignableFrom(Integer.class)) {
                ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (type.isAssignableFrom(Long.class) ||
                    type.isAssignableFrom(Double.class)) {
                ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            } else if (type.isAssignableFrom(String.class)) {
                if (multiline) {
                    ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    ((AutoCompleteTextView) findViewById(R.id.text_field)).setLines(3);
                    setGravity(Gravity.TOP);
                } else {
                    ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(InputType.TYPE_CLASS_TEXT);
                }
            } else {
                throw new MyException("cannot threat type " + type);
            }
        }
    }

    public void setHint(int hint) {
        ((AutoCompleteTextView) findViewById(R.id.text_field)).setHint(hint);
    }


    public void setInputType(int inputType) {
        ((AutoCompleteTextView) findViewById(R.id.text_field)).setInputType(inputType);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    private String getStringResourceByName(Context ctx, String aString) {
        String packageName = ctx.getPackageName();
        int resId = ctx.getResources().getIdentifier(aString, "string", packageName);
        return ctx.getString(resId);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        if(inputType!=null && inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS){
            throw new RuntimeException("TextChangedListener is not compatible with InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS");
        }
        ((AutoCompleteTextView) findViewById(R.id.text_field)).addTextChangedListener(textWatcher);
    }
}
