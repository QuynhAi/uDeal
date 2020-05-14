package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.udeal.MainActivity;
import edu.tacoma.uw.udeal.R;

public class ItemDisplay implements Serializable {

    private int myItemID;
    private int myMemberID;
    private String myURL;
    private String myTitle;
    private String myLocation;
    private String myDescription;
    private String myCategory;
    private double myPrice;
    private boolean myListed;
    private String myDatePosted;
    private int myIndex;
    private transient MainActivity.SimpleItemRecyclerViewAdapter myAdapter;
    private transient Bitmap myBitmap;
    private byte[] myBitmapArray = {};
    private String myUsername;
    private boolean myLiked;
    private transient JSONObject mArguments;
    private int myLikerID;

    public static final String ITEM_ID = "item_id";
    public static final String MEMBER_ID = "member_id";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public static final String PRICE = "price";
    public static final String LISTED = "listed";
    public static final String DATE_POSTED ="date_posted";
    public static final String USERNAME = "username";
    public static final String LIKED = "liked";
    public static final String LIKED_ITEM_ID = "liked_item_id";
    public static final String LIKER_ID = "liker_id";

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

    public int getMyItemID() {
        return myItemID;
    }

    public void setMyItemID(int myItemID) {
        this.myItemID = myItemID;
    }

    public int getMyMemberID() {
        return myMemberID;
    }

    public void setMyMemberID(int myMemberID) {
        this.myMemberID = myMemberID;
    }

    public String getMyURL() {
        return myURL;
    }

    public void setMyURL(String myURL) {
        this.myURL = "https://udeal-app-services-backend.herokuapp.com/download?myfilename=" + myURL;
    }

    public String getMyTitle() {
        return myTitle;
    }

    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public String getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(String myLocation) {
        this.myLocation = myLocation;
    }

    public String getMyDescription() {
        return myDescription;
    }

    public void setMyDescription(String myDescription) {
        this.myDescription = myDescription;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    public double getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(double myPrice) {
        this.myPrice = myPrice;
    }

    public boolean getMyListed() {
        return myListed;
    }

    public void setMyListed(boolean myListed) {
        this.myListed = myListed;
    }

    public String getMyDatePosted() {
        return myDatePosted;
    }

    public void setMyDatePosted(String myDatePosted) {
        this.myDatePosted = myDatePosted;
    }

    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    public byte[] getMyBitmapArray() {
        return myBitmapArray;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public int getMyLikerID() {
        return myLikerID;
    }

    public void setMyLikerID(int myLikerID) {
        this.myLikerID = myLikerID;
    }

    public boolean getmyLiked() {
        return myLiked;
    }

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

    private class ImageTask extends AsyncTask<String, Void, String> {
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

    private class DeleteAsyncTask extends AsyncTask<String, Void, String> {
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

    private class LikedAsyncTask extends AsyncTask<String, Void, String> {
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
