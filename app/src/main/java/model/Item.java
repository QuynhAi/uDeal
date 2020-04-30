package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private int mItemID;
    private int mMemberID;
    private String mTitle;
    private String mLocation;
    private String mDescription;
    private String mCategory;
    private double mPrice;

    public static final String ITEM_ID = "item_id";
    public static final String MEMBER_ID = "member_id";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public static final String PRICE = "price";

    public Item(int theMemberID, String theTitle, String theLocation, String theDescription, String theCategory, double thePrice) {
  //      mItemID = theItemID;
        mMemberID = theMemberID;
        mTitle = theTitle;
        mLocation = theLocation;
        mDescription = theDescription;
        mCategory = theCategory;
        mPrice = thePrice;
    }

    public int getmItemID() {return mItemID;}

    public int getmMemberID() {return mMemberID;}

    public String getmTitle() {return mTitle;}

    public String getmLocation() {return mLocation;}

    public String getmDescription() {return mDescription;}

    public String getmCategory() {return mCategory;}

    public double getmPrice() {return mPrice;}

    public void setmItemID(int theItemID) {mItemID = theItemID;}

    public void setmMemberID(int theMemberID) {mMemberID = theMemberID;}

    public void setmTitle(String theTitle) {mTitle = theTitle;}

    public void setmLocation(String theLocation) {mLocation = theLocation;}

    public void setmDescription(String theDescription) {mDescription = theDescription;}

    public void setmCategory(String theCategory) {mCategory = theCategory;}

    public void setmPrice(double thePrice) {mPrice = thePrice;}

    public static List<Item> parsesItemJson(String itemJson) throws JSONException {
        List<Item> itemList = new ArrayList<>();
        if (itemJson != null) {

            JSONArray arr = new JSONArray(itemJson);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Item userItem = new Item(obj.getInt(Item.MEMBER_ID),
                        obj.getString(Item.TITLE),
                        obj.getString(Item.LOCATION),
                        obj.getString(Item.DESCRIPTION),
                        obj.getString(Item.CATEGORY),
                        obj.getDouble(Item.PRICE));
                userItem.setmItemID(obj.getInt(Item.ITEM_ID));
                // we have to set item id after to account for the fact that we dont have access to
                // the id when creating the item object
                // MAKE SURE TO GET ITEM ID AFTER POST, AND SAVE
                itemList.add(userItem);
            }
        }
        return itemList;
    }


}
