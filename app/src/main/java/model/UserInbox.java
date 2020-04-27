package model;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserInbox {

    private String username;
    private String profilePicture;
    private String itemPicture;


    public static final String USER_NAME = "username";
    public static final String PROFILE_PICTURE = "profile";
    public static final String ITEM_PICTURE = "item";

    public UserInbox(String username, String profilePicture, String itemPicture) {
        this.itemPicture = itemPicture;
        this.profilePicture = profilePicture;
        this.username = username;
    }
    public static List<UserInbox> parseUserInboxJson(String userInboxJson) throws JSONException{
        List<UserInbox> userInboxList = new ArrayList<>();
        if(userInboxJson != null){
            JSONArray arr = new JSONArray(userInboxJson);
            for (int i = 0; i< arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                UserInbox userInbox = new UserInbox(obj.getString(UserInbox.USER_NAME),
                        obj.getString(UserInbox.USER_NAME), obj.getString(UserInbox.USER_NAME));
                userInboxList.add(userInbox);

            }
        }
        return userInboxList;
    }

    public String getUserName() {
        return username;
    }
    public String getProfilePicture(){return profilePicture;}
    public String getItemPicture(){return itemPicture;}

}
