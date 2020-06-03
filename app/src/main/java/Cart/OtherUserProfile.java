package Cart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class OtherUserProfile extends AppCompatActivity {


    /** The profile image for the user. */
    private ImageView myProfileImage;

    /** The text view for the name. */
    private TextView myName;

    /** The text view for the username. */
    private TextView myUsername;

    /** The load limit for recycler view. */
    private static int LOAD_LIMIT = 5;

    /** The number of items to load initially. */
    private static int INITIAL_LOAD = 5;

    /** The list of item displays. */
    private List<Review> mItemList;

    /** The recycler view for the items. */
    private RecyclerView mRecyclerView;

    /** The adapter for the recycler view. */
    public OtherUserProfile.SimpleItemRecyclerViewAdapter mAdapter;

    /** Boolean whether to load more items. */
    private boolean loading = true;

    /** Count of past visible items. */
    private int pastVisiblesItems;

    /** Count of visible items. */
    private int visibleItemCount;

    /** Count of total items. */
    private int totalItemCount;

    /** The linear layout manager for the recycler view. */
    private LinearLayoutManager mLayoutManager;

    /** The rating bar for the user. */
    private RatingBar mRatingBar;

    /** The member username. */
    private String mUsername;

    /** The member ID. */
    private int mMemberID;

    /**
     * Sets up the recycler view and starts an async task to retrieve item information
     * from the database.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        mMemberID = b.getInt("memberID");
        mUsername = b.getString("username");

        setTitle(mUsername +"'s Profile");

        mItemList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        assert mRecyclerView != null;
        initialRecyclerView((RecyclerView) mRecyclerView);
        new OtherUserProfile.GetReviewsAsyncTask().execute(getString(R.string.get_my_reviews) + mUsername + "&limit=" + INITIAL_LOAD + "&offset=" + 0);

        myProfileImage = (ImageView) findViewById(R.id.profile_pic);

        // Get the text from inputs
        myName = (TextView) findViewById(R.id.profile_name);
        new GetFullName().execute(getString(R.string.get_specific_user_name) + mUsername);
        myUsername = (TextView) findViewById(R.id.profile_location);
        myUsername.setText("@" + mUsername);

        mRatingBar = (RatingBar) findViewById(R.id.stars);
        new GetRatingBarAsyncTask().execute(getString(R.string.average_rating) + mUsername);

        new GetImageURLAsyncTask().execute(getString(R.string.get_profile_url) + mUsername);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    /**
     * Initializes the recycler view.
     *
     * @param recyclerView The recycler view to be initalized
     */
    private void initialRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new OtherUserProfile.SimpleItemRecyclerViewAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
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
                            new OtherUserProfile.GetReviewsAsyncTask().execute(getString(R.string.get_my_reviews) + mUsername + "&limit=" + LOAD_LIMIT + "&offset=" + totalItemCount);
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
            extends RecyclerView.Adapter<OtherUserProfile.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final OtherUserProfile mParentActivity;
        private final List<Review> mValues;

        /**
         * Sets up the recycler view adapter.
         *
         * @param parent The parent activity
         * @param items The list of items
         */
        SimpleItemRecyclerViewAdapter(OtherUserProfile parent,
                                      List<Review> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public OtherUserProfile.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_item, parent, false);
            return new OtherUserProfile.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        /**
         * Sets the appropriate text and image for the item. Also handles the heart icon and
         * the progress bar display.
         *
         * @param holder The recycler view holder
         * @param position The position of the item
         */
        @Override
        public void onBindViewHolder(final OtherUserProfile.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mNameRoleView.setText(mValues.get(position).getMyReviewer() + ": " + mValues.get(position).getMyRole());
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
                Toast.makeText(getApplicationContext(), "Unable to get items information: " + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    JSONArray myJSONArray = jsonObject.getJSONArray("names");
                    for(int i = 0; i < myJSONArray.length(); i++) {
                        if(getApplicationContext() != null) {
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
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
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
                Toast.makeText(getApplicationContext(), "Unable to get items information: " + s, Toast.LENGTH_SHORT)
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
                        new OtherUserProfile.ImageTask().execute(getImageURL);
                    }
                }
            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
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
     * The async task to get the user first and last name.
     */
    private class GetFullName extends AsyncTask<String, Void, String> {
        /**
         * Retrieves the full name from the database
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
                    response = "Unable to get full name, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * If successful, the full name is set
         *
         * @param s The response from the async task
         * @throws JSONException if the JSONObject cannot be created
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to get full name information: " + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    String first = jsonObject.getJSONArray("names").getJSONObject(0).getString("first_name");
                    String last = jsonObject.getJSONArray("names").getJSONObject(0).getString("last_name");
                    String fullname = first + " " + last;
                    myName.setText(fullname);
                }
            } catch (JSONException e) {
                Log.d("myTag", "Error when processing names");
                //Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                //        Toast.LENGTH_LONG).show();
            }
        }
    }
}
