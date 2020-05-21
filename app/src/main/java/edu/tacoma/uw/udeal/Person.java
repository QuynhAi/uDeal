package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.ItemDisplay;

/**
 * Fragment for display the user person.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class Person extends Fragment {

    /** The profile image for the user. */
    private ImageView myProfileImage;

    /** The text view for the name. */
    private TextView myName;

    /** The text view for the username. */
    private TextView myUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view and sets up the image capture ability for the user photo.
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The saved instance state
     * @return The view
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Account");

        View view = inflater.inflate(R.layout.activity_person, container, false);

        myProfileImage = (ImageView) view.findViewById(R.id.profile_pic);

        // Get the text from inputs
        myName = (TextView) view.findViewById(R.id.profile_name);
        SharedPreferences settings = getActivity().getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
        String fullName = settings.getString(getString(R.string.fullname), "");
        myName.setText(fullName);
        String userName = settings.getString(getString(R.string.username), "");
        myUsername = (TextView) view.findViewById(R.id.profile_location);
        myUsername.setText("@" +  userName);

        new GetImageURLAsyncTask().execute(getString(R.string.get_profile_url) + userName);
        return view;
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
                    Log.d("myTag", "SUCCESFULLY LOADED IMAGE");
                    JSONArray values = jsonObject.getJSONObject("values").getJSONObject("Body").getJSONArray("data");
                    Bitmap bitmap = null;
                    byte[] tmp = new byte[values.length()];
                    for (int i = 0; i < values.length(); i++) {
                        tmp[i] = (byte) (((int) values.get(i)) & 0xFF);
                    }
                    bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
                    myProfileImage.setImageBitmap(bitmap);
                }

            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
            }
        }
    }

    /**
     * The async task to get the image URL.
     */
    private class GetImageURLAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Retrieves the profile image URL from the database.
         *
         * @param urls The URL to get the information from the database.
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

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to get user profile URL, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * If successful, the infomation is retrieved and the information
         * is proccessed.
         *
         * @param s The response from the async task
         * @throws JSONException if the JSONObject cannot be created
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getActivity(), "Unable to get items information: " + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    String myURL = jsonObject.getJSONArray("names").getJSONObject(0).getString("profile_image");
                    if(myURL.equals("default-profile-image")) {
                        Log.d("myTag", "We have the default profile image." + myURL);
                    } else {
                        String getImageURL = "https://udeal-app-services-backend.herokuapp.com/download?myfilename=" + myURL;
                        Log.d("myTag", getImageURL);
                        new ImageTask().execute(getImageURL);
                    }
                }
            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
                Toast.makeText(getActivity(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
