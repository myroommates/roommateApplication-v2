package be.flo.roommateapp.model.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import be.flo.roommateapp.model.dto.*;
import be.flo.roommateapp.model.service.AccountService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 * Store data
 */
public class Storage {

    private final static long MAX_DELAY = 30 * 60 * 1000;

    private static Date lastLoading;
    private static RoommateDTO currentRoommate;
    private static List<RoommateDTO> roommateList;
    private static HomeDTO home;
    private static List<TicketDTO> ticketList;
    private static List<ShoppingItemDTO> shoppingItemList;
    private static String authenticationKey;


    public static void store(Context context, LoginSuccessDTO loginSuccessDTO) {

        currentRoommate = loginSuccessDTO.getCurrentRoommate();
        setRoommate(loginSuccessDTO.getRoommates());
        home = loginSuccessDTO.getHome();
        ticketList = loginSuccessDTO.getTickets();
        shoppingItemList = loginSuccessDTO.getShoppingItems();

        authenticationKey = loginSuccessDTO.getAuthenticationKey();
        if (ticketList == null) {
            ticketList = new ArrayList<>();
        }

        if (shoppingItemList == null) {
            shoppingItemList = new ArrayList<>();
        }

        //compute color for icon
        for (RoommateDTO roommateDTO : roommateList) {

            computeColorForRoommate(roommateDTO);
        }

        AccountService.storeService(context, loginSuccessDTO);
        lastLoading = new Date();


    }

    private static void computeColorForRoommate(RoommateDTO roommateDTO) {
        roommateDTO.setIconColorTop(toRGB(roommateDTO.getIconColor(), 0.8F, 0.6F, 1F));
        roommateDTO.setIconColorBottom(toRGB(roommateDTO.getIconColor(), 0.8F, 0.3F, 1F));
    }

    public static boolean isConnected() {
        return currentRoommate != null;
    }

    public static void clean(Context context) {
        currentRoommate = null;
        roommateList = null;
        home = null;
        ticketList = null;
        authenticationKey = null;
        AccountService.clearKey(context);
    }

    /*
     * authentication key
     */
    public static String getAuthenticationKey() {
        return authenticationKey;
    }


    public static void setAuthenticationKey(String authenticationKey) {
        Storage.authenticationKey = authenticationKey;
    }
    
    
    /*
     * home
     */

    public static HomeDTO getHome() {
        return home;
    }
    
    
    /*
     * category
     */

    public static List<String> getCategoryList() {
        List<String> categoryList = new ArrayList<String>();

        for (TicketDTO ticketDTO : ticketList) {
            if (ticketDTO.getCategory() != null && !categoryList.contains(ticketDTO.getCategory())) {
                categoryList.add(ticketDTO.getCategory());
            }
        }
        return categoryList;
    }


    /*
     * roommate
     */
    public static RoommateDTO getCurrentRoommate() {
        return currentRoommate;
    }

    public static List<RoommateDTO> getRoommates(List<Long> result) {
        List<RoommateDTO> list = new ArrayList<RoommateDTO>();
        for (Long id : result) {
            list.add(getRoommate(id));
        }
        return list;
    }

    public static List<RoommateDTO> getRoommateList() {
        return roommateList;
    }

    public static void addRoommate(RoommateDTO roommateDTO) {
        computeColorForRoommate(roommateDTO);
        roommateList.add(roommateDTO);
    }

    public static RoommateDTO getRoommate(Long id) {
        for (RoommateDTO roommateDTO : roommateList) {
            if (roommateDTO.getId().equals(id)) {
                return roommateDTO;
            }
        }
        return null;
    }

    public static void editRoommate(RoommateDTO receiveDTO) {
        for (int i = 0; i < roommateList.size(); i++) {
            if (roommateList.get(i).getId().equals(receiveDTO.getId())) {
                roommateList.remove(i);
                roommateList.add(i, receiveDTO);
                break;
            }
        }
    }

    public static void setRoommate(List<RoommateDTO> list) {
        Storage.roommateList = list;
        for (RoommateDTO roommateDTO : roommateList) {
            computeColorForRoommate(roommateDTO);
        }

    }

    /*
     * tickets
     */
    public static TicketDTO getTicket(long id) {
        for (TicketDTO ticket : ticketList) {
            if (ticket.getId().equals(id)) {
                return ticket;
            }
        }
        return null;
    }


    public static void editTicket(TicketDTO ticketDTO) {
        for (int i = 0; i < ticketList.size(); i++) {
            if (ticketList.get(i).getId().equals(ticketDTO.getId())) {
                ticketList.remove(i);
                ticketList.add(i, ticketDTO);
                break;
            }
        }
    }

    public static void setTickets(List<TicketDTO> list) {
        Storage.ticketList = list;
    }

    public static List<TicketDTO> getTicketList() {
        return ticketList;
    }

    public static void addTicket(TicketDTO ticketDTO) {
        ticketList.add(ticketDTO);
    }

    /*
     * shopping items
     */


    /*
     * shopping item
     */
    public static ShoppingItemDTO getShoppingItem(long id) {
        for (ShoppingItemDTO shoppingItem : shoppingItemList) {
            if (shoppingItem.getId().equals(id)) {
                return shoppingItem;
            }
        }
        return null;
    }


    public static void editShoppingItem(ShoppingItemDTO shoppingItemDTO) {
        for (int i = 0; i < ticketList.size(); i++) {
            if (shoppingItemList.get(i).getId().equals(shoppingItemDTO.getId())) {
                shoppingItemList.remove(i);
                shoppingItemList.add(i, shoppingItemDTO);
                break;
            }
        }
    }

    public static void setShoppingItems(List<ShoppingItemDTO> list) {
        Storage.shoppingItemList = list;
    }

    public static List<ShoppingItemDTO> getShoppingItemList() {
        return shoppingItemList;
    }

    public static List<ShoppingItemDTO> getShoppingItemNotBoughtList() {
        List<ShoppingItemDTO> l = new ArrayList<>();
        if (shoppingItemList != null) {
            for (ShoppingItemDTO shoppingItemDTO : shoppingItemList) {
                if (!shoppingItemDTO.isWasBought()) {
                    l.add(shoppingItemDTO);
                }
            }
        }
        return l;

    }

    public static void addShoppingItem(ShoppingItemDTO shoppingItemDTO) {
        shoppingItemList.add(shoppingItemDTO);
    }

    public static boolean testStorage() {
        if (currentRoommate == null) {
            return false;
        }
        /*
        if (new Date().getTime() > lastLoading.getTime() + MAX_DELAY) {
            return false;
        }*/
        return true;
    }


    private static int toRGB(float h, float s, float l, float alpha) {

        //convert h
        h = h / 360;

        if (s < 0.0f || s > 1.0f) {
            String message = "Color parameter outside of expected range - Saturation (" + s + ")";
            throw new IllegalArgumentException(message);
        }

        if (l < 0.0f || l > 1.0f) {
            String message = "Color parameter outside of expected range - Luminance (" + l + ")";
            throw new IllegalArgumentException(message);
        }

        if (alpha < 0.0f || alpha > 1.0f) {
            String message = "Color parameter outside of expected range - Alpha (" + alpha + ")";
            throw new IllegalArgumentException(message);
        }

        float q = 0;

        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);
        float p = 2 * l - q;

        int r = (int) (255 * Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f))));
        int g = (int) (255 * Math.max(0, HueToRGB(p, q, h)));
        int b = (int) (255 * Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f))));

        int alphaInt = (int) (255 * alpha);
        return Color.rgb(r, g, b);

        //return (alphaInt << 24) + (r << 16) + (g << 8) + (b);
    }

    private static float HueToRGB(float p, float q, float h) {
        if (h < 0) h += 1;
        if (h > 1) h -= 1;
        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }
        if (2 * h < 1) {
            return q;
        }
        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }
        return p;
    }
}
