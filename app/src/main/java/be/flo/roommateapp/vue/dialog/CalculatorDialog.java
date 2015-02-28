package be.flo.roommateapp.vue.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import be.flo.roommateapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 15/02/15.
 */
public class CalculatorDialog extends Dialog {

    //private String currentNumber = "";
    private EditText output;
    private List<Operation> operationList = new ArrayList<>();
    private CalculatorEventInterface calculatorEventInterface;
    private Double defaultValue;

    public CalculatorDialog(Context context, CalculatorEventInterface calculatorEventInterface, Double defaultValue) {
        super(context);
        this.calculatorEventInterface = calculatorEventInterface;

        this.defaultValue = defaultValue;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calculator);

        output = (EditText) findViewById(R.id.calculator_output);

        findViewById(R.id.b_calculator_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("0");
            }
        });
        findViewById(R.id.b_calculator_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("1");
            }
        });
        findViewById(R.id.b_calculator_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("2");
            }
        });
        findViewById(R.id.b_calculator_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("3");
            }
        });
        findViewById(R.id.b_calculator_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("4");
            }
        });
        findViewById(R.id.b_calculator_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("5");
            }
        });
        findViewById(R.id.b_calculator_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("6");
            }
        });
        findViewById(R.id.b_calculator_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("7");
            }
        });
        findViewById(R.id.b_calculator_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("8");
            }
        });
        findViewById(R.id.b_calculator_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber("9");
            }
        });
        findViewById(R.id.b_calculator_decimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNumber(".");
            }
        });

        findViewById(R.id.b_calculator_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


        findViewById(R.id.b_calculator_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(Operator.ADD);
            }
        });


        findViewById(R.id.b_calculator_less).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(Operator.SUB);
            }
        });


        findViewById(R.id.b_calculator_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(Operator.MUL);
            }
        });

        findViewById(R.id.b_calculator_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorDialog.this.cancel();
            }
        });

        findViewById(R.id.b_calculator_valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result();
                if (operationList.size() > 0 && operationList.get(0).hasNumber() && operationList.get(0).number.length() > 0) {
                    try {
                        calculatorEventInterface.setResult(Double.parseDouble(operationList.get(0).number));
                    } catch (NumberFormatException ignored) {

                    }
                }
                CalculatorDialog.this.cancel();
            }
        });


        findViewById(R.id.b_calculator_divise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation(Operator.DIV);
            }
        });
        findViewById(R.id.b_calculator_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result();
            }
        });
        findViewById(R.id.b_calculator_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c();
            }
        });
        findViewById(R.id.b_calculator_ce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ce();
            }
        });

        //insert first element
        if (defaultValue != null) {
            operationList.add(new Operation(defaultValue.toString()));
            write();
        } else {
            operationList.add(new Operation(""));
        }
    }

    private void result() {

        if (operationList.size() > 1) {
            double value = Double.parseDouble(operationList.get(0).number);

            for (int i = 1; i < operationList.size(); i += 2) {
                if (operationList.size() >= i + 2) {


                    Operator operator = operationList.get(i).operator;
                    double val = Double.parseDouble(operationList.get(i + 1).number);

                    switch (operator) {

                        case ADD:
                            value += val;
                            break;
                        case SUB:
                            value -= val;
                            break;
                        case MUL:
                            value = value * val;
                            break;
                        case DIV:
                            value = value / val;
                            break;
                    }
                }
            }
            operationList.clear();
            operationList.add(new Operation(value + ""));
            write();
        }
    }

    public void operation(Operator operator) {
        if (operationList.get(operationList.size() - 1).hasNumber()) {
            operationList.add(new Operation(operator));
        } else if (operationList.size() > 0 && operationList.get(operationList.size() - 1).operator != null) {
            operationList.get(operationList.size() - 1).operator = operator;
        }
        write();
    }

    public void write() {
        String content = "";
        for (Operation operation : operationList) {
            if (operation.operator != null) {
                content += " " + operation.operator.s + " ";
            } else {
                content += operation.number;
            }
        }
        output.setText(content);
    }

    public void ce() {
        operationList.clear();
        operationList.add(new Operation(""));
        write();
    }

    public void c() {
        if (operationList.size() > 1) {
            operationList.remove(operationList.size() - 1);
        } else {
            operationList.clear();
            operationList.add(new Operation(""));
        }
        write();
    }

    public void back() {
        if (operationList.get(operationList.size() - 1).hasNumber()) {
            operationList.get(operationList.size() - 1).number = operationList.get(operationList.size() - 1).number.substring(0, operationList.get(operationList.size() - 1).number.length() - 1);
            if (operationList.get(operationList.size() - 1).number.length() == 0 && operationList.size() > 1) {
                operationList.remove(operationList.size() - 1);
            }

        } else if (operationList.size() > 1) {
            operationList.remove(operationList.size() - 1);
        }
        write();
    }


    public void writeNumber(String val) {
        if (operationList.get(operationList.size() - 1).operator != null) {
            operationList.add(new Operation(""));
        }
        String tps = operationList.get(operationList.size() - 1).number;
        if (val == "." && tps.length() == 0) {
            tps="0.";
        } else {
            tps = tps + val;
        }
        try {
            Double.parseDouble(tps);
            operationList.get(operationList.size() - 1).number = tps;
            write();
        } catch (NumberFormatException ignored) {

        }
    }

    public class Operation {


        Operator operator;
        String number;

        public Operation(Operator operator) {
            this.operator = operator;
        }

        public Operation(String number) {
            this.number = number;
        }

        public boolean hasNumber() {
            return number != null && number.length() > 0;
        }
    }

    public enum Operator {
        ADD("+"),
        SUB("-"),
        MUL("*"),
        DIV("/"),;

        private String s;

        Operator(String s) {
            this.s = s;
        }
    }

}
