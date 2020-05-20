package model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class represents the user inbox.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class UserInbox implements Serializable {

    /** The current username string. */
    public static final String CURRENT_USER_NAME = "currentuser";

    /** The seller username string. */
    public static final String SELLER = "seller";

    /** The profile picture string. */
    public static final String PROFILE_PICTURE = "profileurl";

    /** The item picture string. */
    public static final String ITEM_PICTURE = "itemurl";

    /** The username. */
    private String seller;

    /** The profile picture. */
    private String profilePicture;

    /** The item picture. */
    private String itemPicture;

    /** The current username. */
    private String currentUserName;

    /** The bitmap array of the item. */
    private byte[] myBitmapArray = {};

    /** The bitmap that represents the photo of the item. */
    private transient Bitmap myBitmap;


    /**
     * Initializes the fields in the user inbox.
     * @param currentUserName The current user name
     * @param seller The seller user name
     * @param profilePicture The profile picture
     * @param itemPicture The item picture
     */
    public UserInbox(String currentUserName, String seller, String profilePicture, String itemPicture) throws ExecutionException, InterruptedException {
        this.currentUserName = currentUserName;
        this.itemPicture = itemPicture;
        new ImageTask().execute(this.itemPicture).get();
        this.profilePicture = profilePicture;
        this.seller = seller;
        //Log.e("bitMap" , String.valueOf(myBitmap));
    }

    /**
     * Parses the string that has the user inbox information.
     *
     * @param userInboxJson The user inbox JSON string
     * @return The list of user inboxes
     * @throws JSONException if the JSONObject cannot be created
     */
    public static List<UserInbox> parseUserInboxJson(String userInboxJson) throws JSONException, ExecutionException, InterruptedException {
        List<UserInbox> userInboxList = new ArrayList<>();
        if(userInboxJson != null){
            JSONArray arr = new JSONArray(userInboxJson);
            for (int i = 0; i< arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);

                UserInbox userInbox = new UserInbox(obj.getString(UserInbox.CURRENT_USER_NAME), obj.getString(UserInbox.SELLER),
                        obj.getString(UserInbox.PROFILE_PICTURE), obj.getString(UserInbox.ITEM_PICTURE));
                if (!userInboxList.contains(userInbox)){
                    userInboxList.add(userInbox);
                }

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
    public String getSellerName() {
        return seller;
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
    /**
     * Gets the bitmap of the item.
     *
     * @return Bitmap of the item
     */
    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    /**
     * Gets the byte array for the bitmap of the item.
     *
     * @return The byte array for the bitmap
     */
    public byte[] getMyBitmapArray() {
        return myBitmapArray;
    }

    /**
     * This class handles the async task that retrives the image
     * from S3.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class ImageTask extends AsyncTask<String, Void, String> {
        /**
         * Retrieves the image from S3.
         *
         * @param urls The URL to review the image.
         * @return The response from the connection
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            //Log.e("Are you here?" , "are you here?");
            for (String url : urls) {
                try {
                    java.net.URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the image, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                if (jsonObject.getBoolean("success")) {
//                    JSONArray values = jsonObject.getJSONObject("values").getJSONObject("Body").getJSONArray("data");
//                    Bitmap bitmap = null;
//                    byte[] tmp = new byte[values.length()];
//                    for (int i = 0; i < values.length(); i++) {
//                        tmp[i] = (byte) (((int) values.get(i)) & 0xFF);
//                    }
//                    bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
//                    myBitmapArray = tmp;
//                    myBitmap = bitmap;
//                    //Log.e("myTag", String.valueOf(myBitmap));
//                }
//
//            } catch (JSONException e) {
//                Log.d("myTag", "FAILURE");
//            }
            return response;
        }

        /**
         * If successful, the bitmap of the item is updated. If unsuccessful,
         * the method returns.
         *
         * @param s The response from the async task
         * @throws JSONException if the JSONObject cannot be created
         */
        @Override
        public void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Log.d("myTag", s);
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    JSONArray values = jsonObject.getJSONObject("values").getJSONObject("Body").getJSONArray("data");
                    Bitmap bitmap = null;
                    byte[] tmp = new byte[values.length()];
                    for (int i = 0; i < values.length(); i++) {
                        tmp[i] = (byte) (((int) values.get(i)) & 0xFF);
                    }
                    bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
                    myBitmapArray = tmp;
                    myBitmap = bitmap;
                    Log.e("myTag", String.valueOf(myBitmap));
                }

            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
            }
        }
    }
}