package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import inbox.ChatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import model.ItemDisplayBuyingFrag;
import model.UserInbox;


/**
 * An activity representing a single ItemDisplayBuyingFrag item. This is
 * used for the "Liked Items" tab on the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ItemDisplayBuyingDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    /** The item ID. */
    public static final String ARG_ITEM_ID = "item_id";

    /** The Google map to be displayed. */
    private GoogleMap mMap;

    /** The ItemDisplayBuyingFrag object used for retrieving the information. */
    private ItemDisplayBuyingFrag mItemDisplay;

    /** The rating bar. */
    private RatingBar mRatingBar;

    /**
     * Creates the activity by setting text of the views and the image of the item.
     * Sets up the Google Map with the correct location.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display_buying_detail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending a message to the seller", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SharedPreferences settings = getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
                String current = settings.getString(getString(R.string.username), "");
                // temporary, change to
                UserInbox item = null;
                try {
                    item = new UserInbox(current, mItemDisplay.getMyUsername(),
                            mItemDisplay.getMyTitle(), mItemDisplay.getMyURL(), false);
                }catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                StringBuilder check = new StringBuilder(getString(R.string.user_inbox));
                check.append("/check?currentuser=");
                check.append(current);
                check.append("&seller=");
                check.append(mItemDisplay.getMyUsername());
                check.append("&itemname=");
                check.append(mItemDisplay.getMyTitle());
                check.append("&itemurl=");
                check.append(mItemDisplay.getMyURL());
                new UserInboxTaskGet().execute(check.toString());
                //Log.e("ItemDisplayDetailActivi", String.valueOf(item.getSellerName()));

                Context context = view.getContext();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.ARG_ITEM_ID, item);
                context.startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            if(getIntent().getSerializableExtra(ARG_ITEM_ID) != null) {
                arguments.putSerializable(ARG_ITEM_ID,
                        getIntent().getSerializableExtra(ARG_ITEM_ID));
                if (arguments.containsKey(ARG_ITEM_ID)) {
                    mItemDisplay = (ItemDisplayBuyingFrag) arguments.getSerializable(ARG_ITEM_ID);
                    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(mItemDisplay.getMyTitle());
                    }
                }
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_action_bar));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        CardView user = (CardView) findViewById(R.id.cardviewholder);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, OtherUserProfile.class);
                intent.putExtra("memberID", mItemDisplay.getMyMemberID());
                intent.putExtra("username", mItemDisplay.getMyUsername());
                context.startActivity(intent);
            }
        });


        if (mItemDisplay != null) {
            new ImageTask().execute(mItemDisplay.getMyURL());
            ((TextView) findViewById(R.id.date_posted)).setText("Posted on " + mItemDisplay.getMyDatePosted());
            ((TextView) findViewById(R.id.item_title)).setText(mItemDisplay.getMyTitle());
            ((TextView) findViewById(R.id.item_category)).setText("Category: " + mItemDisplay.getMyCategory());
            ((TextView) findViewById(R.id.item_price)).setText("$" + String.format("%.2f", mItemDisplay.getMyPrice()));
            ((TextView) findViewById(R.id.item_location)).setText(mItemDisplay.getMyLocation());
            ((TextView) findViewById(R.id.item_seller)).setText(mItemDisplay.getMyUsername());
            ((TextView) findViewById(R.id.item_description)).setText(mItemDisplay.getMyDescription());
        }

        mRatingBar = findViewById(R.id.stars);
        new GetRatingBarAsyncTask().execute(getString(R.string.average_rating) + mItemDisplay.getMyUsername());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_button:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody="Take a look at this " + mItemDisplay.getMyTitle() + " sold by " + mItemDisplay.getMyUsername()
                        + ", selling for $" + String.format("%.2f", mItemDisplay.getMyPrice())
                        + " on uDeal. Download the uDeal app today to check it out.";
                String shareSubject="Take a look at this item on uDeal";

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

                startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be
     * used. Adds a marker to the location of the item, and also displays a location radius
     * around that maker. If Google Play services is not installed on the device, the user will be
     * prompted to install it inside the SupportMapFragment. This method will only be triggered once
     * the user has installed Google Play services and returned to the app.
     *
     * @param googleMap The GoogleMap to be displayed on the screen
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> myCoordinates = new ArrayList<>();
        if(Geocoder.isPresent()) {
            try{
                String location = mItemDisplay.getMyLocation();
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(location, 1);
                for(Address a : addresses) {
                    if(a.hasLatitude() && a.hasLongitude()) {
                        myCoordinates.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
            } catch (IOException e) {
                Log.d("myTag", "onMapReady: IO Exception");
            }
        }

        LatLng coordinates;
        try {
            coordinates = myCoordinates.get(0);
        } catch (IndexOutOfBoundsException e) {
            coordinates = new LatLng(0, 0);
        }
        mMap.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
        mMap.addCircle(new CircleOptions().center(coordinates).radius(5000).strokeColor(Color.RED).fillColor(0x220000FF).strokeWidth(5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 11));
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
                    if(mItemDisplay != null) {
                        ((ImageView) findViewById(R.id.item_image_id)).setImageBitmap(bitmap);
                        Log.d("myTag", "We have done it!");
                    }
                }

            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
            }
        }
    }

    /**
     * The async task to get the average rating.
     */
    private class GetRatingBarAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Retrieves the average rating from the database.
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
                    response = "Unable to get rating bar average, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * If successful, the average rating is set.
         *
         * @param s The response from the async task
         * @throws JSONException if the JSONObject cannot be created
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to get rating information: " + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    double myAverage = jsonObject.getJSONArray("names").getJSONObject(0).getDouble("avg");
                    mRatingBar.setRating((float) myAverage);
                }
            } catch (JSONException e) {
                Log.d("myTag", "Error when processing average rating or no reviews posted");
                //Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                //        Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * This class posts a message between two users.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class UserInboxTaskPost extends AsyncTask<String, Void, String> {
        /**
         * Posts the message between two users.
         *
         * @param urls The URL endpoints
         * @return The response from the async task
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            SharedPreferences settings = getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
            String current = settings.getString(getString(R.string.username), "");

            JSONObject mArguments = new JSONObject();
            try {
                mArguments.put(UserInbox.CURRENT_USER_NAME, current);
                mArguments.put(UserInbox.SELLER, mItemDisplay.getMyUsername());
                mArguments.put(UserInbox.ITEM_NAME, mItemDisplay.getMyTitle());
                mArguments.put(UserInbox.ITEM_PICTURE, mItemDisplay.getMyURL());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (String url : urls) {
                //Log.e("urConnection", String.valueOf(url));
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

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
                    response = e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.e("Response", String.valueOf(response));
            return response;
        }

        /**
         * If successful, we get the messages and add it to the list.
         *
         * @param s The response from the async task
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to download" + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success") == true) {
                    Toast.makeText(getApplicationContext(), "Success",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This class get all messages from two specific users
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    public class UserInboxTaskGet extends AsyncTask<String, Void, String> {
        /**
         * Retrives messages between two users.
         *
         * @param urls The URL endpoint
         * @return The response from the async task
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to check, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, we get the messages between the two users and add it to the list.
         *
         * @param s The response from the async task
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to download" + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success") == true) {
                    new UserInboxTaskPost().execute(getString(R.string.user_inbox));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}