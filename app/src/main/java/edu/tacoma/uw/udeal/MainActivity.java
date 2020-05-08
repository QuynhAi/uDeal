package edu.tacoma.uw.udeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

import model.ItemDisplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import inbox.InboxListActivity;
import model.ItemDisplay;



public class MainActivity extends AppCompatActivity {

    private List<ItemDisplay> mItemList;

    private RecyclerView mRecyclerView;

    public SimpleItemRecyclerViewAdapter mAdapter;

    private static int LOAD_LIMIT = 3;

    private static int INITIAL_LOAD = 3;

    private int loadCount;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCount = 0;

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        setTitle("Home");
        mItemList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        assert mRecyclerView != null;
        initialRecyclerView((RecyclerView) mRecyclerView);
        new DisplayItemsAsyncTask().execute(getString(R.string.load_limited) + "?limit=" + INITIAL_LOAD + "&offset=" + 0);
    }

    private void initialRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new SimpleItemRecyclerViewAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            new DisplayItemsAsyncTask().execute(getString(R.string.load_limited) + "?limit=" + LOAD_LIMIT + "&offset=" + totalItemCount);
                          //  loading = true;
                        }
                    }
                }
            }
        });
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MainActivity mParentActivity;
        private final List<ItemDisplay> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDisplay item = (ItemDisplay) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDisplayDetailActivity.class);
                intent.putExtra(ItemDisplayDetailFragment.ARG_ITEM_ID, item);

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(MainActivity parent,
                                      List<ItemDisplay> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getMyTitle());
            holder.mContentView.setText(mValues.get(position).getMyPrice() + "");
            holder.mImageView.setImageBitmap(mValues.get(position).getMyBitmap());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mImageView;


            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.textViewTitle);
                mContentView = (TextView) view.findViewById(R.id.textViewPrice);
                mImageView = (ImageView) view.findViewById(R.id.imageViewImage);
            }
        }
    }

    private class DisplayItemsAsyncTask extends AsyncTask<String, Void, String> {
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
                        mItemList.add(ItemDisplay.parseItemJson(myJSONArray.getJSONObject(i), i, mAdapter ));
                        mAdapter.notifyItemInserted(mItemList.size() - 1);
                        loading = true;
                    }
                }
            } catch (JSONException e) {
                Log.d("myTag", "FAILURE");
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(MainActivity.this, PostActivity.class);
                            startActivity(p);
                            break;
                        case R.id.nav_inbox:
                            Intent i = new Intent(MainActivity.this, MessageInboxActivity.class);
                            startActivity(i);
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(MainActivity.this, CartActivity.class);
                            startActivity(c);
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(np);
                            break;
                    }
                    return false;
                }
            };
}


