package model;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInbox implements Serializable {

    private String username;
    private String profilePicture;
    private String itemPicture;
    private String currentUserName;

    public static final String CURRENT_USER_NAME = "currentUserName";
    public static final String USER_NAME = "username";
    public static final String PROFILE_PICTURE = "profile";
    public static final String ITEM_PICTURE = "item";

    public UserInbox(String currentUserName, String username, String profilePicture, String itemPicture) {
        this.currentUserName = currentUserName;
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
                UserInbox userInbox = new UserInbox(obj.getString(UserInbox.USER_NAME), obj.getString(UserInbox.USER_NAME),
                        obj.getString(UserInbox.USER_NAME), obj.getString(UserInbox.USER_NAME));
                userInboxList.add(userInbox);

            }
        }
        return userInboxList;
    }
    public String getCurrentUserName(){return currentUserName;}
    public String getOtherUserName() {
        return username;
    }
    public String getProfilePicture(){return profilePicture;}
    public String getItemPicture(){return itemPicture;}

}

//public class UserInbox implements Serializable {
//    private String currentUserName;
//    private String otherUserName;
//    private String profilePicture;
//    private String itemPicture;
//
//    public static final String CURRENT_USER_NAME = "current_user_name";
//    public static final String OTHER_USER_NAME = "other_user_name";
//    public static final String PROFILE_PICTURE = "profile";
//    public static final String ITEM_PICTURE = "item";
//
//    public UserInbox(String current, String username, String profilePicture, String itemPicture) {
//        this.currentUserName = current;
//        this.itemPicture = itemPicture;
//        this.profilePicture = profilePicture;
//        this.otherUserName = username;
//    }
//    public static List<UserInbox> parseUserInboxJson(String userInboxJson) throws JSONException{
//        List<UserInbox> userInboxList = new ArrayList<>();
//        if(userInboxJson != null){
//            JSONArray arr = new JSONArray(userInboxJson);
//            for (int i = 0; i< arr.length(); i++){
//                JSONObject obj = arr.getJSONObject(i);
//                UserInbox userInbox = new UserInbox(obj.getString(UserInbox.CURRENT_USER_NAME), obj.getString(UserInbox.OTHER_USER_NAME),
//                        obj.getString(UserInbox.PROFILE_PICTURE), obj.getString(UserInbox.ITEM_PICTURE));
//                userInboxList.add(userInbox);
//
//            }
//        }
//        return userInboxList;
//    }
//    public String getCurrentUserName(){return currentUserName;}
//    public String getOtherUserName() {
//        return otherUserName;
//    }
//    public String getProfilePicture(){return profilePicture;}
//    public String getItemPicture(){return itemPicture;}
//
//}
