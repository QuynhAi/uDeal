package edu.tacoma.uw.udeal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import model.ItemDisplay;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link
 */
public class ItemDisplayDetailActivity extends AppCompatActivity {

    private JSONObject mItemJSON;

    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemDisplay mItemDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display_detail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            if(getIntent().getSerializableExtra(ARG_ITEM_ID) != null) {
                arguments.putSerializable(ARG_ITEM_ID,
                        getIntent().getSerializableExtra(ARG_ITEM_ID));
                if (arguments.containsKey(ARG_ITEM_ID)) {
                    mItemDisplay = (ItemDisplay) arguments.getSerializable(ARG_ITEM_ID);

                    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(mItemDisplay.getMyTitle());
                    }
                }
            }
        }

        // Show the Up button in the action bar.
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
            ((TextView) findViewById(R.id.item_price)).setText("$" + mItemDisplay.getMyPrice());
            ((TextView) findViewById(R.id.item_location)).setText(mItemDisplay.getMyLocation());
            ((TextView) findViewById(R.id.item_seller)).setText("Seller: " + mItemDisplay.getMyUsername());
            ((TextView) findViewById(R.id.item_description)).setText("Description: " + mItemDisplay.getMyDescription());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
