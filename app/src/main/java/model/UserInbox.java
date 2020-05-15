package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the user inbox.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class UserInbox implements Serializable {

    /** The current username string. */
    public static final String CURRENT_USER_NAME = "currentUserName";

    /** The current username string. */
    public static final String USER_NAME = "username";

    /** The profile picture string. */
    public static final String PROFILE_PICTURE = "profile";

    /** The item picture string. */
    public static final String ITEM_PICTURE = "item";

    /** The username. */
    private String username;

    /** The profile picture. */
    private String profilePicture;

    /** The item picture. */
    private String itemPicture;

    /** The current username. */
    private String currentUserName;


    /**
     * Initializes the fields in the user inbox.
     *
     * @param currentUserName The current user name
     * @param username The username
     * @param profilePicture The profile picture
     * @param itemPicture The item picture
     */
    public UserInbox(String currentUserName, String username, String profilePicture, String itemPicture) {
        this.currentUserName = currentUserName;
        this.itemPicture = itemPicture;
        this.profilePicture = profilePicture;
        this.username = username;
    }

    /**
     * Parses the string that has the user inbox information.
     *
     * @param userInboxJson The user inbox JSON string
     * @return The list of user inboxes
     * @throws JSONException if the JSONObject cannot be created
     */
    public static List<UserInbox> parseUserInboxJson(String userInboxJson) throws JSONException{
        List<UserInbox> userInboxList = new ArrayList<>();
        if(userInboxJson != null){
            JSONArray arr = new JSONArray(userInboxJson);
            for (int i = 0; i< arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                UserInbox userInbox = new UserInbox(obj.getString(UserInbox.USER_NAME), obj.getString(UserInbox.USER_NAME),
                        obj.getString(UserInbox.USER_NAME), obj.getString(UserInbox.USER_NAME));
                userInboxList.add(userInbox);

            }
        }
        return userInboxList;
    }

    /**
     * Gets the current user name.
     *
     * @return The current username.
     */
    public String getCurrentUserName(){
        return currentUserName;
    }

    /**
     * Gets the other username.
     *
     * @return The other username.
     */
    public String getOtherUserName() {
        return username;
    }

    /**
     * Gets the profile picture.
     *
     * @return The profile picture.
     */
    public String getProfilePicture(){
        return profilePicture;
    }

    /**
     * Gets the item picture.
     *
     * @return The item picture.
     */
    public String getItemPicture(){
        return itemPicture;
    }

}