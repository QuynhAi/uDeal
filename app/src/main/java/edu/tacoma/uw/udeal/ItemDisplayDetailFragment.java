package edu.tacoma.uw.udeal;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import model.ItemDisplay;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link }
 * in two-pane mode (on tablets) or a
 */
public class ItemDisplayDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemDisplay mItemDisplay;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDisplayDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItemDisplay = (ItemDisplay) getArguments().getSerializable(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItemDisplay.getMyTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_display_detail, container, false);

        // Show the course content as text in a TextView.
        if (mItemDisplay != null) {
            byte[] tmp = mItemDisplay.getMyBitmapArray();
            ((ImageView) rootView.findViewById(R.id.item_image_id)).setImageBitmap(BitmapFactory.decodeByteArray(tmp, 0, tmp.length));
            ((TextView) rootView.findViewById(R.id.item_detail_id)).setText(mItemDisplay.getMyCategory());
            ((TextView) rootView.findViewById(R.id.item_detail_short_desc)).setText(mItemDisplay.getMyLocation());
            ((TextView) rootView.findViewById(R.id.item_detail_long_desc)).setText(mItemDisplay.getMyDescription());
            ((TextView) rootView.findViewById(R.id.item_detail_prereqs)).setText("Posted by " + mItemDisplay.getMyUsername());
        }

        return rootView;
    }
}
