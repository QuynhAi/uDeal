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
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.udeal.MainActivity;

/**
 * This class handles the item that will be displayed
 * on the home page of the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ItemDisplay implements Serializable {

    /** Used to retrieve item ID. */
    private static final String ITEM_ID = "item_id";
    /** Used to retrieve member ID. */
    private static final String MEMBER_ID = "member_id";
    /** Used to retrieve URL. */
    private static final String URL = "url";
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
    /** Used to retrieve the listed boolean. */
    private static final String LISTED = "listed";
    /** Used to retrieve the date posted. */
    private static final String DATE_POSTED ="date_posted";
    /** Used to retrieve the username. */
    private static final String USERNAME = "username";
    /** Used to retrieve the liked boolean. */
    private static final String LIKED = "liked";
    /** Used to retrieve the liked item id. */
    private static final String LIKED_ITEM_ID = "liked_item_id";
    /** Used to retrieve the liker id. */
    private static final String LIKER_ID = "liker_id";

    /** The URL for the photo. */
    private String myURL;
    /** The title of the item. */
    private String myTitle;
    /** The location of the item. */
    private String myLocation;
    /** The description of the item. */
    private String myDescription;
    /** The category of the item. */
    private String myCategory;
    /** The date the item was posted. */
    private String myDatePosted;
    /** The username of the person who posted the item. */
    private String myUsername;
    /** The ID of the item. */
    private int myItemID;
    /** The ID of the member. */
    private int myMemberID;
    /** The index that represents where item is located in recycler view. */
    private int myIndex;
    /** The ID of the person who liked the item. */
    private int myLikerID;
    /** The price of the item. */
    private double myPrice;
    /** True if item is listed; false if item is not listed. */
    private boolean myListed;
    /** True if the current user liked this item; false otherwise. */
    private boolean myLiked;
    /** The bitmap array of the item. */
    private byte[] myBitmapArray = {};
    /** The arguments for the JSON object. */
    private transient JSONObject mArguments;
    /** The adapter for the recycler view. */
    private transient MainActivity.SimpleItemRecyclerViewAdapter myAdapter;
    /** The bitmap that represents the photo of the item. */
    private transient Bitmap myBitmap;

    /**
     * Initializes all fields and calls an async task to
     * retrieve the photo from S3.
     *
     * @param myItemID The item ID
     * @param myMemberID The member ID
     * @param myURL The URL of the photo
     * @param myTitle The title of the item
     * @param myLocation The location of the item
     * @param myDescription The description of the item
     * @param myCategory The category of the item
     * @param myPrice The price of the item
     * @param myListed The boolean whether the is listed
     * @param myDatePosted The date the item was posted
     * @param myIndex The index of the item
     * @param myAdapter The adapter for the item
     * @param myUsername The username of the poster of the item
     * @param myLiked The boolean whether or not this item is liked
     * @param myLikerID The member ID of the liker
     */
    public ItemDisplay(int myItemID, int myMemberID, String myURL, String myTitle, String myLocation,
                       String myDescription, String myCategory, double myPrice, boolean myListed,
                       String myDatePosted, int myIndex, MainActivity.SimpleItemRecyclerViewAdapter myAdapter,
                       String myUsername, boolean myLiked, int myLikerID) {
        this.myItemID = myItemID;
        this.myMemberID = myMemberID;
        this.myURL = "https://udeal-app-services-backend.herokuapp.com/download?myfilename=" + myURL;
        // TODO: UNCOMMENT BELOW FOR SUBMISSION
        //new ImageTask().execute(this.myURL);
        Log.d("myTag", "This is one instance of loading the image");
        this.myTitle = myTitle;
        this.myLocation = myLocation;
        this.myDescription = myDescription;
        this.myCategory = myCategory;
        this.myPrice = myPrice;
        this.myListed = myListed;
        this.myDatePosted = myDatePosted;
        this.myIndex = myIndex;
        this.myAdapter = myAdapter;
        this.myUsername = myUsername;
        this.myLiked = myLiked;
        this.myLikerID = myLikerID;
    }

    /**
     * Gets item ID.
     *
     * @return The item ID
     */
    public int getMyItemID() {
        return myItemID;
    }

    /**
     * Sets the item ID.
     *
     * @param myItemID The item ID
     */
    public void setMyItemID(int myItemID) {
        this.myItemID = myItemID;
    }

    /**
     * Gets the member ID.
     *
     * @return The member ID
     */
    public int getMyMemberID() {
        return myMemberID;
    }

    /**
     * Sets the member ID.
     *
     * @param myMemberID The member ID
     */
    public void setMyMemberID(int myMemberID) {
        this.myMemberID = myMemberID;
    }

    /**
     * Gets the URL.
     *
     * @return The URL
     */
    public String getMyURL() {
        return myURL;
    }

    /**
     * Sets the URL.
     *
     * @param myURL The URL
     */
    public void setMyURL(String myURL) {
        this.myURL = "https://udeal-app-services-backend.herokuapp.com/download?myfilename=" + myURL;
    }

    /**
     * Gets the title.
     *
     * @return The title
     */
    public String getMyTitle() {
        return myTitle;
    }

    /**
     * Sets the title.
     *
     * @param myTitle The title
     */
    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    /**
     * Gets the location.
     *
     * @return The location
     */
    public String getMyLocation() {
        return myLocation;
    }

    /**
     * Sets the location.
     *
     * @param myLocation The location
     */
    public void setMyLocation(String myLocation) {
        this.myLocation = myLocation;
    }

    /**
     * Gets the description.
     *
     * @return The description
     */
    public String getMyDescription() {
        return myDescription;
    }

    /**
     * Sets the description
     *
     * @param myDescription The description
     */
    public void setMyDescription(String myDescription) {
        this.myDescription = myDescription;
    }

    /**
     * Gets the category.
     *
     * @return The category
     */
    public String getMyCategory() {
        return myCategory;
    }

    /**
     * Sets the category.
     *
     * @param myCategory The category
     */
    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    /**
     * Gets the price.
     *
     * @return The price
     */
    public double getMyPrice() {
        return myPrice;
    }

    /**
     * Sets the price.
     *
     * @param myPrice The price
     */
    public void setMyPrice(double myPrice) {
        this.myPrice = myPrice;
    }

    /**
     * Gets the listed boolean.
     *
     * @return True if the item is listed; false otherwise
     */
    public boolean getMyListed() {
        return myListed;
    }

    /**
     * Sets the listed boolean.
     *
     * @param myListed The boolean whether the item is listed or not
     */
    public void setMyListed(boolean myListed) {
        this.myListed = myListed;
    }

    /**
     * Gets the date posted of the item.
     *
     * @return The date the item was posted
     */
    public String getMyDatePosted() {
        return myDatePosted;
    }

    /**
     * Sets the date posted of the item.
     *
     * @param myDatePosted The date the item was posted
     */
    public void setMyDatePosted(String myDatePosted) {
        this.myDatePosted = myDatePosted;
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
     * Gets the username.
     *
     * @return The username
     */
    public String getMyUsername() {
        return myUsername;
    }

    /**
     * Sets the username.
     *
     * @param myUsername The username
     */
    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    /**
     * Gets the liker ID.
     *
     * @return The liker ID
     */
    public int getMyLikerID() {
        return myLikerID;
    }

    /**
     * Sets the liker ID.
     *
     * @param myLikerID The liker ID
     */
    public void setMyLikerID(int myLikerID) {
        this.myLikerID = myLikerID;
    }

    /**
     * Gets boolean whether or not this item is liked.
     *
     * @return The liked boolean
     */
    public boolean getmyLiked() {
        return myLiked;
    }

    /**
     * Sets whether or not this item is liked. If it is liked, this method will call an async
     * task to add this information to the 'Like' database. If it has been unliked, this method
     * will can an async task to remove this information from the 'Like' database.
     *
     * @param myLiked True if the item is liked; false otherwise
     */
    public void setMyLiked(boolean myLiked) {
        this.myLiked = myLiked;
        if(this.myLiked) {
            StringBuilder urlURL = new StringBuilder("https://udeal-app-services-backend.herokuapp.com/addlikes");
            mArguments = new JSONObject();
            try {
                mArguments.put(ItemDisplay.LIKED_ITEM_ID, myItemID);
                mArguments.put(ItemDisplay.LIKER_ID, myLikerID);
                mArguments.put(ItemDisplay.LIKED, this.myLiked);
                new LikedAsyncTask().execute(urlURL.toString());
            } catch(JSONException e) {
                Log.d("myTag", "INSERTION LIKE: Error in JSON creation");
            }
        } else {
            StringBuilder urlURL = new StringBuilder("https://udeal-app-services-backend.herokuapp.com/deleteLike");
            mArguments = new JSONObject();
            try {
                mArguments.put(ItemDisplay.LIKED_ITEM_ID, myItemID);
                mArguments.put(ItemDisplay.LIKER_ID, myLikerID);
                new DeleteAsyncTask().execute(urlURL.toString());
            } catch(JSONException e) {
                Log.d("myTag", "DELETION LIKE: Error in JSON creation");
            }
        }
        myAdapter.notifyItemChanged(myIndex);
    }

    /**
     * Parse the JSONObject and converts it into an ItemDisplayBuyingFrag object.
     *
     * @param itemJSON The JSONObject to be parsed
     * @param myIndex The index where item is located in recycler view
     * @param myAdapter The adapter for the recycler view
     * @param myID The ID of the current user
     * @return The ItemDisplayBuyingFrag object created from the JSONObject
     * @throws JSONException If there is an issue parsing the JSONObject
     */
    public static ItemDisplay parseItemJson(JSONObject itemJSON, int myIndex, MainActivity.SimpleItemRecyclerViewAdapter myAdapter, int myID) throws JSONException {
        JSONObject obj = itemJSON;
        ItemDisplay item = new ItemDisplay(
                obj.getInt(ItemDisplay.ITEM_ID),
                obj.getInt(ItemDisplay.MEMBER_ID),
                obj.getString(ItemDisplay.URL),
                obj.getString(ItemDisplay.TITLE),
                obj.getString(ItemDisplay.LOCATION),
                obj.getString(ItemDisplay.DESCRIPTION),
                obj.getString(ItemDisplay.CATEGORY),
                obj.getDouble(ItemDisplay.PRICE),
                obj.getBoolean(ItemDisplay.LISTED),
                obj.getString(ItemDisplay.DATE_POSTED),
                myIndex,
                myAdapter,
                obj.getString(ItemDisplay.USERNAME),
                obj.getBoolean(ItemDisplay.LIKED),
                myID);
        return item;
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
                    myAdapter.notifyItemChanged(myIndex);
            Log.d("myTag", "We have notified the adapter to insert me: ");
                }

            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
            }
        }
    }

    /**
     * This class handles the async task that deletes the liked
     * information from the database.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class DeleteAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Deletes the liked information from the database.
         *
         * @param urls The URL to delete.
         * @return The response from the connection
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("DELETE");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.i("myTag", mArguments.toString());
                    wr.write(mArguments.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add the like to postgres sql, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, the database is updated.
         *
         * @param result The response from the async task
         * @throws JSONException if the JSONObject cannot be created
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Log.d("myTag", "Success on post execute adding like to DB");
                } else {
                    Log.d("myTag", "Error on post execute adding like to DB");
                }
            } catch (JSONException e) {
                Log.d("myTag", "Catch JSON Exception e: " + e.getMessage());
            }

        }
    }

    /**
     * This class handles the async task that adds the liked information
     * to the database.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class LikedAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Adds the liked information to the database.
         *
         * @param urls The URL to add the liked information
         * @return The response from the connection
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.i("myTag", mArguments.toString());
                    wr.write(mArguments.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add the like to postgres sql, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, the database is updated.
         *
         * @param result The response from the async task
         * @throws JSONException if the JSONObject cannot be created
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success") == true) {
                    Log.d("myTag", "Success on post execute adding like to DB");
                } else {
                    Log.d("myTag", "Error on post execute adding like to DB");
                }
            } catch (JSONException e) {
                Log.d("myTag", "Catch JSON Exception e: " + e.getMessage());
            }

        }
    }

}
