package Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.udeal.R;
import model.Review;

/**
 * Fragment for displaying the reviews written.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class ReviewsWrittenFragment extends Fragment {
    /** The load limit for recycler view. */
    private static int LOAD_LIMIT = 10;

    /** The number of reviews to load initially. */
    private static int INITIAL_LOAD = 10;

    /** The list of reviews. */
    private List<Review> mItemList;

    /** The recycler view for the reviews. */
    private RecyclerView mRecyclerView;

    /** The adapter for the recycler view. */
    public ReviewsWrittenFragment.SimpleItemRecyclerViewAdapter mAdapter;

    /** Boolean whether to load more reviews. */
    private boolean loading = true;

    /** Count of past visible reviews. */
    private int pastVisiblesItems;

    /** Count of visible reviews. */
    private int visibleItemCount;

    /** Count of total reviews. */
    private int totalItemCount;

    /** The linear layout manager for the recycler view. */
    private LinearLayoutManager mLayoutManager;

    /** The rating bar for the user. */
    private RatingBar mRatingBar;

    /**
     * Creates the view and sets up the recycler view.
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The saved instance state
     * @return The view
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_written, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Reviews I've Written");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });

        mItemList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        assert mRecyclerView != null;
        initialRecyclerView((RecyclerView) mRecyclerView);
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        String theUsername = settings.getString(getString(R.string.username), "");
        new ReviewsWrittenFragment.GetReviewsAsyncTask().execute(getString(R.string.get_my_written) + theUsername + "&limit=" + INITIAL_LOAD + "&offset=" + 0);

        return view;
    }


    /**
     * Initializes the recycler view.
     *
     * @param recyclerView The recycler view to be initalized
     */
    private void initialRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new ReviewsWrittenFragment.SimpleItemRecyclerViewAdapter((SettingsActivity) getActivity(), mItemList);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Allows for infinite scrolling on the recycler view.
             *
             * @param recyclerView The recycler view
             * @param dx The x coordinate
             * @param dy The y coordinate
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("hello", "Last Item Wow !");
                            SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                            String theUsername = settings.getString(getString(R.string.username), "");
                            new ReviewsWrittenFragment.GetReviewsAsyncTask().execute(getString(R.string.get_my_written) + theUsername + "&limit=" + LOAD_LIMIT + "&offset=" + totalItemCount);
                        }
                    }
                }
            }
        });
    }

    /**
     * The class that handles the recycler view adapter.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ReviewsWrittenFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final SettingsActivity mParentActivity;
        private final List<Review> mValues;

        /**
         * Sets up the recycler view adapter.
         *
         * @param parent The parent activity
         * @param items The list of items
         */
        SimpleItemRecyclerViewAdapter(SettingsActivity parent,
                                      List<Review> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ReviewsWrittenFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_item, parent, false);
            return new ReviewsWrittenFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        /**
         * Sets the appropriate values for the review.
         *
         * @param holder The recycler view holder
         * @param position The position of the item
         */
        @Override
        public void onBindViewHolder(final ReviewsWrittenFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mNameRoleView.setText("As a " + mValues.get(position).getMyRole().toLowerCase() + ", you reviewed \n" + mValues.get(position).getMyReviewed() );
            holder.mDescription.setText(mValues.get(position).getMyReview());
            holder.mDateView.setText(mValues.get(position).getMyDatePosted());
            holder.mRatingBar.setRating( (float) mValues.get(position).getMyRating());

            holder.itemView.setTag(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * The ViewHolder for the recycler view.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            /** The name and role text view */
            final TextView mNameRoleView;
            /** The date text view. */
            final TextView mDateView;
            /** The description text view. */
            final TextView mDescription;
            /** The star rating. */
            final RatingBar mRatingBar;

            /**
             * Initializes fields accordingly with their correct ID.
             *
             * @param view The view
             */
            ViewHolder(View view) {
                super(view);
                mNameRoleView = (TextView) view.findViewById(R.id.textViewTitle);
                mDateView = (TextView) view.findViewById(R.id.reviewDate);
                mDescription = (TextView) view.findViewById(R.id.reviewDescription);
                mRatingBar = (RatingBar) view.findViewById(R.id.stars);
            }
        }
    }

    /**
     * The async task to get the reviews.
     */
    private class GetReviewsAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * Retrieves the information from the database.
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
                    response = "Unable to get item information, Reason: "
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
                    JSONArray myJSONArray = jsonObject.getJSONArray("names");
                    for(int i = 0; i < myJSONArray.length(); i++) {
                        if(getActivity() != null) {
                            Log.d("myLog", "onPostExecute: Success");
                            mItemList.add(Review.parseItemJson(myJSONArray.getJSONObject(i)));
                            mAdapter.notifyItemInserted(mItemList.size() - 1);
                            loading = true;
                        } else {
                            Log.d("myLog", "onPostExecute: Get Activity is NULL");
                        }
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
