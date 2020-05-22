package edu.tacoma.uw.udeal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import model.ItemDisplayBuyingFrag;


/**
 * The Buying fragment that is displayed when the user navigates to the shopping
 * cart tab. This is the  fragment that is shown when the second tab is clicked.
 * This holds the recycler view of items that the user can scroll through.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class BuyingFrag extends Fragment {

    /** The load limit for recycler view. */
    private static int LOAD_LIMIT = 3;

    /** The number of items to load initially. */
    private static int INITIAL_LOAD = 3;

    /** The list of item displays. */
    private List<ItemDisplayBuyingFrag> mItemList;

    /** The recycler view for the items. */
    private RecyclerView mRecyclerView;

    /** The adapter for the recycler view. */
    public BuyingFrag.SimpleItemRecyclerViewAdapter mAdapter;

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

    /**
     * Sets up the recycler view and starts an async task to retrieve item information
     * from the database.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buying, container, false);

        mItemList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        assert mRecyclerView != null;
        initialRecyclerView((RecyclerView) mRecyclerView);
        SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int theID = settings.getInt(getString(R.string.member_id), 0);
        new BuyingFrag.DisplayItemsAsyncTask().execute(getString(R.string.load_limited_specific) + "?limit=" + INITIAL_LOAD + "&offset=" + 0  + "&theLikerID=" + theID);

        return view;
    }

    /**
     * Initializes the recycler view.
     *
     * @param recyclerView The recycler view to be initalized
     */
    private void initialRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new BuyingFrag.SimpleItemRecyclerViewAdapter((CartActivity) getActivity(), mItemList);
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
                            int theID = settings.getInt(getString(R.string.member_id), 0);
                            new BuyingFrag.DisplayItemsAsyncTask()
                                    .execute(getString(R.string.load_limited_specific) + "?limit=" + LOAD_LIMIT + "&offset=" + totalItemCount + "&theLikerID=" + theID);
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
            extends RecyclerView.Adapter<BuyingFrag.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CartActivity mParentActivity;
        private final List<ItemDisplayBuyingFrag> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            /**
             * Displays the item when the item is clicked on the recycler view.
             *
             * @param view The view
             */
            @Override
            public void onClick(View view) {
                ItemDisplayBuyingFrag item = (ItemDisplayBuyingFrag) view.getTag();
                Bitmap temp = item.getMyBitmap();
                item.resetBitmaps();
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDisplayBuyingDetailActivity.class);
                intent.putExtra(ItemDisplayBuyingDetailActivity.ARG_ITEM_ID, item);
                context.startActivity(intent);
                item.setMyBitmap(temp);
            }
        };

        /**
         * Sets up the recycler view adapter.
         *
         * @param parent The parent activity
         * @param items The list of items
         */
        SimpleItemRecyclerViewAdapter(CartActivity parent,
                                      List<ItemDisplayBuyingFrag> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public BuyingFrag.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new BuyingFrag.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        /**
         * Sets the appropriate text and image for the item. Also handles the heart icon and
         * the progress bar display.
         *
         * @param holder The recycler view holder
         * @param position The position of the item
         */
        @Override
        public void onBindViewHolder(final BuyingFrag.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getMyTitle());
            holder.mImageView.setImageBitmap(mValues.get(position).getMyBitmap());
            if(mValues.get(position).getMyBitmap() != null) {
                holder.mImageView.setVisibility(holder.mImageView.VISIBLE);
                holder.mPBar.setVisibility(holder.mPBar.GONE);
            } else {
                holder.mImageView.setVisibility(holder.mImageView.GONE);
                holder.mPBar.setVisibility(holder.mPBar.VISIBLE);
            }
            final ItemDisplayBuyingFrag temp = mValues.get(position);
            holder.mLikeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp.setMyLiked(!temp.getmyLiked());
                }
            });
            if(mValues.get(position).getmyLiked()) {
                holder.mLikeImage.setImageResource(R.drawable.heart_icon_pressed);

            } else {
                holder.mLikeImage.setImageResource(R.drawable.heart_icon);
            }
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * The ViewHolder for the recycler view.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            /** The title text view. */
            final TextView mIdView;
            /** The image view. */
            final ImageView mImageView;
            /** The heart icon image. */
            final ImageView mLikeImage;
            /** The progress bar. */
            final ProgressBar mPBar;

            /**
             * Initializes fields accordingly with their correct ID.
             *
             * @param view The view
             */
            ViewHolder(View view) {
                super(view);
                mPBar = (ProgressBar) view.findViewById(R.id.pBar);
                mIdView = (TextView) view.findViewById(R.id.textViewTitle);
                mImageView = (ImageView) view.findViewById(R.id.imageViewImage);
                mLikeImage = (ImageView) view.findViewById(R.id.heartImage);
            }
        }
    }

    /**
     * The async task to display items.
     */
    private class DisplayItemsAsyncTask extends AsyncTask<String, Void, String> {
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
                        int temp = mItemList.size();
                        if(getActivity() != null) {
                            Log.d("myLog", "onPostExecute: Success");
                            SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                            int theID = settings.getInt(getString(R.string.member_id), 0);
                            mItemList.add(ItemDisplayBuyingFrag.parseItemJson(myJSONArray.getJSONObject(i), temp, mAdapter, theID));
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
