package be.flo.roommateapp.vue.activity.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.*;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.UserIcon;
import be.flo.roommateapp.model.util.externalRequest.Request;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.dialog.CalculatorEventInterface;
import be.flo.roommateapp.vue.dialog.DialogConstructor;
import be.flo.roommateapp.vue.spinner.SelectionWithOpenFieldSpinner;
import be.flo.roommateapp.vue.widget.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class EditTicketActivity extends AbstractEditActivity<TicketDTO> {

    //intent constant
    public static final String TICKET_ID = "ticketId";
    public static final String TICKET_DESC = "ticket_desc";
    public static final String TICKET_CATEGORY = "ticket_category";
    public static final String TICKET_LIST_ID_SHOPPING_ITEM = "ticket_category";
    public static final String TICKET_LIST_ID_SHOPPING_ITEM_SEPARATION_SYMBOL = ",";

    //field
    private EditText valueTotalField;
    private CheckBox ckEqualRepartition;
    private boolean initialize = true;

    private boolean edit;
    private TicketDTO ticket;
    private List<ShoppingItemDTO> shoppingItemList;

    private List<Debtor> debtors = new ArrayList<>();
    private Field dateField;
    private Field descriptionField;
    private SelectionWithOpenFieldSpinner categorySpinner;
    private ImageButton calculatorTotal;


    private class Debtor {
        private RoommateDTO roommateDTO;
        private CheckBox checkBox;
        private EditText editText;
        private ImageButton calculator;

        public Debtor(RoommateDTO roommateDTO) {
            this.roommateDTO = roommateDTO;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);

        try {
            builder();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void builder() throws NoSuchFieldException {

        //recover intent
        Intent intent = getIntent();

        // load ticket
        if (intent.getLongExtra(TICKET_ID, -1L) != -1L) {
            ticket = Storage.getTicket(intent.getLongExtra(TICKET_ID, -1L));
            edit = true;
        } else {
            ticket = new TicketDTO();
            ticket.setDate(new Date());

            if (intent.getStringExtra(TICKET_DESC) != null) {
                ticket.setDescription(intent.getStringExtra(TICKET_DESC));
            }
            if (intent.getStringExtra(TICKET_CATEGORY) != null) {
                ticket.setCategory(intent.getStringExtra(TICKET_CATEGORY));
            }
            if (intent.getStringExtra(TICKET_LIST_ID_SHOPPING_ITEM) != null) {
                shoppingItemList = new ArrayList<>();
                String[] shoppingItemIds = intent.getStringExtra(TICKET_LIST_ID_SHOPPING_ITEM).split(TICKET_LIST_ID_SHOPPING_ITEM_SEPARATION_SYMBOL);

                //load shoppingItems and build description
                String desc = getResources().getString(R.string.edit_ticket_desc_shopping) + " : ";
                boolean first = true;
                for (String shoppingItemId : shoppingItemIds) {
                    if (first) {
                        first = false;
                    } else {
                        desc += ", ";
                    }
                    ShoppingItemDTO shoppingItem = Storage.getShoppingItem(Long.parseLong(shoppingItemId));
                    desc += shoppingItem.getDescription();
                    shoppingItemList.add(shoppingItem);
                }

                //complete field
                ticket.setDescription(desc);
                ticket.setCategory(getResources().getString(R.string.edit_ticket_category_shopping));
            }
        }


        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.insert_point);

        //build date
        dateField = new Field(this, new Field.FieldProperties(TicketDTO.class.getDeclaredField("date"), R.string.g_date), ticket.getDate());
        insertPoint.addView(dateField, 0);

        //description
        descriptionField = new Field(this, new Field.FieldProperties(TicketDTO.class.getDeclaredField("description"), R.string.g_desc), ticket.getDescription());
        insertPoint.addView(descriptionField, 1);

        //category
        LinearLayout categoryContainer = (LinearLayout) findViewById(R.id.category_container);
        categorySpinner = new SelectionWithOpenFieldSpinner(this, Storage.getCategoryList());
        categoryContainer.addView(categorySpinner);

        //equal repartition
        ckEqualRepartition = (CheckBox) findViewById(R.id.equal_repartition);
        ckEqualRepartition.setChecked(true);

        //total
        valueTotalField = (EditText) findViewById(R.id.value);
        valueTotalField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        valueTotalField.setEnabled(ckEqualRepartition.isChecked());


        //insert details value
        for (final RoommateDTO roommateDTO : Storage.getRoommateList()) {

            LayoutInflater inflater = this.getLayoutInflater();
            View lineLayout = inflater.inflate(R.layout.activity_edit_ticket_debtor_line, insertPoint, false);
            insertPoint.addView(lineLayout);

            final Debtor debtor = new Debtor(roommateDTO);
            debtors.add(debtor);

            //value edit text
            final EditText valueDebtorField = (EditText) lineLayout.findViewById(R.id.debtor_value);

            debtor.editText = valueDebtorField;
            debtor.editText.setEnabled(!ckEqualRepartition.isChecked());
            debtor.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!ckEqualRepartition.isChecked()) {
                        computeValues();
                    }
                }
            });

            //calculator
            ImageButton calculator = (ImageButton) lineLayout.findViewById(R.id.b_calculator_debtor);

            debtor.calculator=calculator;
            debtor.calculator.setEnabled(!ckEqualRepartition.isChecked());
            calculator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Double defaultValue = null;
                    try {
                        defaultValue = Double.parseDouble(debtor.editText.getText().toString().replace(",", "."));
                    } catch (NumberFormatException e) {
                    }
                    DialogConstructor.dialogCalculator(EditTicketActivity.this, new CalculatorEventInterface() {

                        @Override
                        public void setResult(double value) {
                            debtor.editText.setText(String.format("%.2f", value));
                        }
                    }, defaultValue).show();
                }
            });

            //checkbox
            CheckBox checkBox = (CheckBox) lineLayout.findViewById(R.id.debtor_select);
            checkBox.setChecked(true);
            debtor.checkBox = checkBox;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    debtor.editText.setEnabled(isChecked && !ckEqualRepartition.isChecked());
                    debtor.calculator.setEnabled(isChecked && !ckEqualRepartition.isChecked());
                    computeValues();
                }
            });

            //value

            //add icon
            ((ViewGroup) lineLayout.findViewById(R.id.debtor_icon)).addView(UserIcon.generateUserIcon(this, roommateDTO));
        }


        ckEqualRepartition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Debtor debtor : debtors) {
                    debtor.editText.setEnabled(debtor.checkBox.isChecked() && !ckEqualRepartition.isChecked());
                    debtor.calculator.setEnabled(debtor.checkBox.isChecked() && !ckEqualRepartition.isChecked());
                }
                valueTotalField.setEnabled(isChecked);
                calculatorTotal.setEnabled(isChecked);

            }
        });

        //add listener to valueTotalField
        //add this listener AFTER set text
        valueTotalField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (ckEqualRepartition.isChecked()) {
                    computeValues();
                }
            }
        });

        //add calculator
        calculatorTotal = (ImageButton) findViewById(R.id.b_calculator_value_total);
        calculatorTotal.setEnabled(ckEqualRepartition.isChecked());
        calculatorTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double defaultValue = null;
                try {
                    defaultValue = Double.parseDouble(valueTotalField.getText().toString().replace(",", "."));
                } catch (NumberFormatException e) {
                }

                DialogConstructor.dialogCalculator(EditTicketActivity.this, new CalculatorEventInterface() {
                    @Override
                    public void setResult(double value) {
                        valueTotalField.setText(String.format("%.2f", value));
                    }
                }, defaultValue).show();
            }
        });

        //set data into field
        insertValues();

        //finish initialize
        initialize = false;


    }

    private void insertValues() {


        //category
        if (ticket.getCategory() != null) {
            categorySpinner.setSelection(ticket.getCategory());
        }

        //total and value
        double total = 0.0;
        Double test = null;
        if (ticket.getDebtorList() != null) {
            for (Debtor debtor : debtors) {
                boolean founded = false;
                for (TicketDebtorDTO ticketDebtor : ticket.getDebtorList()) {
                    if (debtor.roommateDTO.getId().equals(ticketDebtor.getRoommateId())) {

                        founded = true;
                        total += ticketDebtor.getValue();
                        debtor.editText.setText(String.format("%.2f", ticketDebtor.getValue()));
                        if (test == null) {
                            test = ticketDebtor.getValue();
                        } else if (!test.equals(ticketDebtor.getValue())) {
                            ckEqualRepartition.setChecked(false);
                        }
                        break;
                    }
                }
                if (!founded) {
                    debtor.checkBox.setChecked(false);
                }
            }
            valueTotalField.setText(String.format("%.2f", total));
        }
    }


    private void computeValues() {

        if (!initialize) {
            if (ckEqualRepartition.isChecked()) {

                int total = 0;

                //nb selected
                for (Debtor debtor : debtors) {
                    if (debtor.checkBox.isChecked()) {
                        total++;
                    }
                }
                double valueByDebtor;
                try {
                    valueByDebtor = Double.parseDouble(valueTotalField.getText().toString().replace(",", ".")) / total;
                } catch (NumberFormatException e) {
                    valueByDebtor = 0.0;
                }

                for (Debtor debtor : debtors) {
                    if (debtor.checkBox.isChecked()) {
                        debtor.editText.setText(String.format("%.2f", valueByDebtor));
                    } else {
                        debtor.editText.setText("");
                    }
                }
            } else {
                double total = 0.0;
                for (Debtor debtor : debtors) {
                    if (debtor.checkBox.isChecked()) {

                        try {
                            total += Double.parseDouble(debtor.editText.getText().toString().replace(",", "."));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                valueTotalField.setText(String.format("%.2f", total));
            }
        }
    }

    protected int getActivityTitle() {
        if (edit) {
            return R.string.edit_ticket_edit;
        } else {
            return R.string.edit_ticket_create;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        assert getActionBar() != null;

        this.getActionBar().setTitle(getActivityTitle());

        menu.clear();
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected WebClient<TicketDTO> getWebClient() {

        final WebClient<TicketDTO> webClient;
        if (ticket.getId() == null) {

            edit = false;
            webClient = new WebClient<>(RequestEnum.TICKET_CREATE,
                    ticket,
                    TicketDTO.class);
        } else {

            edit = true;
            webClient = new WebClient<>(RequestEnum.TICKET_EDIT,
                    ticket,
                    ticket.getId(),
                    TicketDTO.class);
        }
        return webClient;
    }

    @Override
    protected void save() {

        if (descriptionField.control() && dateField.control()) {

            //set date into dto

            //setDate
            ticket.setDate((Date) dateField.getValue());

            //set description
            ticket.setDescription((String) descriptionField.getValue());

            //set category
            ticket.setCategory(categorySpinner.getSelectedItem());

            //set debtor
            ticket.setDebtorList(null);
            for (Debtor debtor : debtors) {
                if (debtor.checkBox.isChecked()) {
                    TicketDebtorDTO ticketDebtor = new TicketDebtorDTO();
                    ticket.addTicketDebtor(ticketDebtor);
                    ticketDebtor.setRoommateId(debtor.roommateDTO.getId());
                    try {
                        ticketDebtor.setValue(Double.parseDouble(debtor.editText.getText().toString().replace(",", ".")));
                    } catch (NumberFormatException e) {
                    }
                }
            }

            //send request
            Request request = new Request(this, getWebClient());

            //execute request
            request.execute();

            //send bought request
            String listShoppingItemBoughtIds = "";
            if (shoppingItemList != null) {
                for (ShoppingItemDTO shoppingItemDTO : shoppingItemList) {
                    listShoppingItemBoughtIds += shoppingItemDTO.getId() + TICKET_LIST_ID_SHOPPING_ITEM_SEPARATION_SYMBOL;
                }

                //send request for bought article
                Request requestForBought = new Request(this, getWebClientForShoppingItemBought(listShoppingItemBoughtIds));

                //execute request
                requestForBought.execute();
            }
        }
    }

    protected WebClient<ResultDTO> getWebClientForShoppingItemBought(String listShoppingItemBoughtIds) {

        final WebClient<ResultDTO> webClient;
        webClient = new WebClient<>(RequestEnum.SHOPPING_ITEM_BOUGHT,
                null,
                listShoppingItemBoughtIds,
                ResultDTO.class);
        return webClient;
    }

    boolean ticketReceive = false;
    boolean shoppingItemReceive = false;

    @Override
    public void successAction(DTO successDTO) {
        if (successDTO instanceof TicketDTO) {
            //for ticket
            if (edit) {
                Storage.editTicket((TicketDTO) successDTO);
            } else {
                Storage.addTicket((TicketDTO) successDTO);
            }
            ticketReceive = true;
        } else {
            //for shopping item
            for (ShoppingItemDTO shoppingItem : shoppingItemList) {
                shoppingItem.setWasBought(true);
            }
            shoppingItemReceive = true;
        }
        if ((shoppingItemList == null || shoppingItemReceive) && ticketReceive) {
            backToMainActivity();
        }
    }
}
