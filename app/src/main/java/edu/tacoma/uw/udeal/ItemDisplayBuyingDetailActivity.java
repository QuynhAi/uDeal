package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import inbox.ChatActivity;
import java.io.IOException;
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

    /**
     * Creates the activity by setting text of the views and the image of the item.
     * Sets up the Google Map with the correct location.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display_detail);

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
                            mItemDisplay.getMyURL(), mItemDisplay.getMyURL());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
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

        if (mItemDisplay != null) {
            byte[] tmp = mItemDisplay.getMyBitmapArray();
            ((ImageView) findViewById(R.id.item_image_id)).setImageBitmap(BitmapFactory.decodeByteArray(tmp, 0, tmp.length));
            ((TextView) findViewById(R.id.item_title)).setText(mItemDisplay.getMyTitle());
            ((TextView) findViewById(R.id.item_category)).setText("Category: " + mItemDisplay.getMyCategory());
            ((TextView) findViewById(R.id.item_price)).setText("$" + mItemDisplay.getMyPrice());
            ((TextView) findViewById(R.id.item_location)).setText(mItemDisplay.getMyLocation());
            ((TextView) findViewById(R.id.item_seller)).setText("Seller: " + mItemDisplay.getMyUsername());
            ((TextView) findViewById(R.id.item_description)).setText("Description: " + mItemDisplay.getMyDescription());
        }
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
            navigateUpTo(new Intent(this, MainActivity.class));
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

        LatLng coordinates = myCoordinates.get(0);
        mMap.addMarker(new MarkerOptions().position(coordinates).title("Marker"));
        mMap.addCircle(new CircleOptions().center(coordinates).radius(10000).strokeColor(Color.RED).fillColor(0x220000FF).strokeWidth(5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10));
    }
}