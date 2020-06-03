package Cart;

import android.content.Context;
import android.content.Intent;
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

import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.tacoma.uw.udeal.R;
import UpdateItem.UpdateItemPictureActivity;
import UpdateItem.UpdateTextActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.ItemDisplaySellingFrag;

/**
 * An activity representing a single ItemDisplaySellingFrag item. This is
 * used for the "Posted Items" tab on the application.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ItemDisplaySellingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** The item ID. */
    public static final String ARG_ITEM_ID = "item_id";

    /** The Google map to be displayed. */
    private GoogleMap mMap;

    /** The ItemDisplaySellingFrag object used for retrieving the information. */
    private ItemDisplaySellingFrag mItemDisplay;

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
        setContentView(R.layout.activity_item_display_selling_detail);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            if(getIntent().getSerializableExtra(ARG_ITEM_ID) != null) {
                arguments.putSerializable(ARG_ITEM_ID,
                        getIntent().getSerializableExtra(ARG_ITEM_ID));
                if (arguments.containsKey(ARG_ITEM_ID)) {
                    mItemDisplay = (ItemDisplaySellingFrag) arguments.getSerializable(ARG_ITEM_ID);

                    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(mItemDisplay.getMyTitle());
                    }
                }
            }
        }

        Switch mSwitch = (Switch) findViewById((R.id.list_switch));

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                mItemDisplay.setMyListed(on);
                if (on) {
                    ((Switch) findViewById(R.id.list_switch)).setText("Status: Your item is posted and can be seen by other users.");
                } else {
                    ((Switch) findViewById(R.id.list_switch)).setText("Status: Your item has been archived and cannot be seen by other users.");
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_action_bar));


        Button replace = (Button) findViewById(R.id.replace);
        replace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateItemPictureActivity.class);
                intent.putExtra("ItemID", mItemDisplay.getMyItemID());
                intent.putExtra("MemberID", mItemDisplay.getMyMemberID());
                startActivity(intent);
            }
        });
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateTextActivity.class);
                intent.putExtra("ItemID", mItemDisplay.getMyItemID());
                intent.putExtra("MemberID", mItemDisplay.getMyMemberID());
                intent.putExtra("Title", mItemDisplay.getMyTitle());
                intent.putExtra("Location", mItemDisplay.getMyLocation());
                intent.putExtra("Price", mItemDisplay.getMyPrice());
                intent.putExtra("Category", mItemDisplay.getMyCategory());
                intent.putExtra("Description", mItemDisplay.getMyDescription());
                startActivity(intent);
            }
        });

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
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mItemDisplay.getMyTitle());
            }
            ((Switch) findViewById(R.id.list_switch)).setChecked(mItemDisplay.getMyListed());
            if(mItemDisplay.getMyListed()) {
                mSwitch.setText("Status: Your item is posted and can be seen by other users.");
            } else {
                mSwitch.setText("Status: Your item has been archived and cannot be seen by other users.");
            }
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

    /**
     * Allows the back button to be displayed based on boolean value.
     *
     * @param item The menu item.
     * @return Boolean value for options item selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, CartActivity.class));
            return true;
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
}