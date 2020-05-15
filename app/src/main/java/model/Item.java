package model;

import java.io.Serializable;

/**
 * This class handles the item used when posting
 * the item to the database.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class Item implements Serializable {

    /** Used to retrieve item ID. */
    private static final String ITEM_ID = "item_id";
    /** Used to retrieve member ID. */
    private static final String MEMBER_ID = "member_id";
    /** Used to retrieve the title. */
    private static final String TITLE = "title";
    /** Used to retrieve the location. */
    private static final String LOCATION = "location";
    /** Used to retrieve the description. */
    private static final String DESCRIPTION = "description";
    /** Used to retrieve the category. */
    private static final String CATEGORY = "category";
    /** Used to retrieve the price. */
    private static final String PRICE = "price";

    /** The item ID. */
    private int mItemID;
    /** The member ID. */
    private int mMemberID;
    /** The title. */
    private String mTitle;
    /** The location of the item. */
    private String mLocation;
    /** The description of the item. */
    private String mDescription;
    /** The category of the item. */
    private String mCategory;
    /** The price of the item. */
    private double mPrice;


    /**
     * Initializes the fields of the itmem.
     *
     * @param theMemberID The member ID
     * @param theTitle The title
     * @param theLocation The location
     * @param theDescription The description
     * @param theCategory The category
     * @param thePrice The price
     */
    public Item(int theMemberID, String theTitle, String theLocation, String theDescription, String theCategory, double thePrice) {
        mMemberID = theMemberID;
        mTitle = theTitle;
        mLocation = theLocation;
        mDescription = theDescription;
        mCategory = theCategory;
        mPrice = thePrice;
    }

    /**
     * Gets the member ID.
     *
     * @return The member ID
     */
    public int getmMemberID() {
        return mMemberID;
    }

    /**
     * Gets the title.
     *
     * @return The title
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * Gets the location.
     *
     * @return The item location
     */
    public String getmLocation() {
        return mLocation;
    }

    /**
     * Gets the item ID.
     *
     * @return The item ID
     */
    public String getmDescription() {
        return mDescription;
    }

    /**
     * Gets the item description.
     *
     * @return The item description
     */
    public String getmCategory() {
        return mCategory;
    }

    /**
     * Gets the item price.
     *
     * @return The item price
     */
    public double getmPrice() {
        return mPrice;
    }
}
