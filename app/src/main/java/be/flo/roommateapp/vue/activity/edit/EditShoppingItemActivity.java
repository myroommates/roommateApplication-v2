package be.flo.roommateapp.vue.activity.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import be.flo.roommateapp.R;
import be.flo.roommateapp.model.dto.ShoppingItemDTO;
import be.flo.roommateapp.model.dto.technical.DTO;
import be.flo.roommateapp.model.util.Storage;
import be.flo.roommateapp.model.util.exception.MyException;
import be.flo.roommateapp.model.util.externalRequest.RequestEnum;
import be.flo.roommateapp.model.util.externalRequest.WebClient;
import be.flo.roommateapp.vue.widget.Field;
import be.flo.roommateapp.vue.widget.Form;

/**
 * Created by florian on 11/11/14.
 */
public class EditShoppingItemActivity extends AbstractEditActivity<ShoppingItemDTO> {

    //constant
    public static final String SHOPPING_ITEM_ID = "shoppingItemId";

    private boolean edit;
    private ShoppingItemDTO shoppingItemDTO;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //recover shopping item id
        Intent i = getIntent();

        //create shoppingItem
        if (i.getLongExtra(SHOPPING_ITEM_ID, -1L) != -1L) {
            shoppingItemDTO = Storage.getShoppingItem(i.getLongExtra(SHOPPING_ITEM_ID, -1L));
            edit = true;
        } else {
            shoppingItemDTO = new ShoppingItemDTO();
        }

        //build field
        try {

            //create field
            try {
                form = new Form(this, shoppingItemDTO,
                        new Field.FieldProperties(ShoppingItemDTO.class.getDeclaredField("description"), R.string.g_desc, InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
                        new Field.FieldProperties(ShoppingItemDTO.class.getDeclaredField("onlyForMe"), R.string.shopping_only_for_me));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            //and insert field into view
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (MyException e) {
            Log.e("error", e.getMessage());
            e.printStackTrace();
            displayErrorMessage(e.getMessage());
        }

    }

    @Override
    protected int getActivityTitle() {
        if (edit) {
            return R.string.edit_shopping_item_edit;
        } else {
            return R.string.edit_shopping_item_create;
        }
    }

    @Override
    protected WebClient<ShoppingItemDTO> getWebClient() {
        WebClient<ShoppingItemDTO> webClient;
        if (edit) {
            //create request
            webClient = new WebClient<>(RequestEnum.SHOPPING_ITEM_EDIT,
                    shoppingItemDTO,
                    shoppingItemDTO.getId(),
                    ShoppingItemDTO.class);
        } else {
            webClient = new WebClient<>(RequestEnum.SHOPPING_ITEM_CREATE,
                    shoppingItemDTO,
                    ShoppingItemDTO.class);
        }
        return webClient;
    }

    @Override
    public void successAction(DTO successDTO) {
        if (edit) {
            //update
            Storage.editShoppingItem(((ShoppingItemDTO) successDTO));
        } else {
            //add new entity
            Storage.addShoppingItem(((ShoppingItemDTO) successDTO));
        }

        backToMainActivity();
    }

}
