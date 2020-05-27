package inbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.tacoma.uw.udeal.CartActivity;
import edu.tacoma.uw.udeal.MainActivity;
import edu.tacoma.uw.udeal.PostActivity;
import edu.tacoma.uw.udeal.ProfileActivity;
import edu.tacoma.uw.udeal.R;
import model.UserInbox;

/**
 * The activity inbox activity represents the message inbox.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class MessageInboxActivity extends AppCompatActivity {
    /** The two pane boolean. */
    private boolean mTwoPane;

    /** The list of the user inbox. */
    private List<UserInbox> mUserList;

    /** The recycler view. */
    private RecyclerView mRecyclerView;

    /** The column count. */
    private int mColumnCount = 1;

    /** The current string. */
    private String current;

    private SearchView searchView;

    /** The bitmap that represents the photo of the item. */
    private transient Bitmap myBitmap;

    private ProgressBar loadInbox;
    /**
     * Sets up the messages.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Inbox");
        setContentView(R.layout.activity_message_inbox);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        loadInbox = (ProgressBar) findViewById(R.id.loadInbox);

        SharedPreferences settings = getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
        current = settings.getString(getString(R.string.username), "");
        mRecyclerView = findViewById(R.id.fragment_container);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);

        if (findViewById(R.id.inbox_detail_container) != null) {
            mTwoPane = true;
        }
        if(mUserList == null){
            StringBuilder url = new StringBuilder(getString(R.string.user_inbox));
            url.append("?currentuser=");
            url.append(current);
            new UserInboxTask().execute(url.toString());
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mUserList == null){
            StringBuilder url = new StringBuilder(getString(R.string.user_inbox));
            url.append("?currentuser=");
            url.append(current);
            new UserInboxTask().execute(url.toString());
        }
    }

    /**
     * Sets up the recycler view.
     *
     * @param recyclerView The recycler view.
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mUserList != null){
            recyclerView.setAdapter(new MessageInboxActivity.SimpleItemRecyclerViewAdapter(this, mUserList, mTwoPane));
            Log.e("are you done?", "done3");
            mRecyclerView.setVisibility(RecyclerView.VISIBLE);
            loadInbox.setVisibility(ProgressBar.INVISIBLE);

        }
    }

    /**
     * This is the recycler view adapter.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MessageInboxActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        /** The parent activity. */
        private final MessageInboxActivity mParentActivity;

        /** The list of user inboxes. */
        private final List<UserInbox> mValues;

        /** The two pane boolean. */
        private final boolean mTwoPane;

        /** The view on click listener. */
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInbox item = (UserInbox) view.getTag();
                if (mTwoPane) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ChatActivity.ARG_ITEM_ID, item);
                    context.startActivity(intent);
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ChatActivity.ARG_ITEM_ID, item);
                    context.startActivity(intent);
                }
            }
        };

        /**
         * Initalizes the chat activity and the list of messages.
         *
         * @param parent The parent activity
         * @param items The list of user inboxes
         * @param twoPane Boolean two pane
         */
        SimpleItemRecyclerViewAdapter(MessageInboxActivity parent, List<UserInbox> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inbox_list_content, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.name.setText(mValues.get(position).getSellerName());
            holder.item_name.setText(mValues.get(position).getItemName());
            if(mValues.get(position).getMyBitmap() != null) {
                holder.item_image.setImageBitmap(mValues.get(position).getMyBitmap());
                mValues.get(position).setMyBitmap(null);
                holder.item_image.setVisibility(holder.item_image.VISIBLE);
            } else {
                holder.item_image.setVisibility(holder.item_image.GONE);
            }
            //Log.e("TESTing" , String.valueOf(mValues.get(position).getMyBitmap()));
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * The View Holder for the recycler view.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            /** The name text view. */
            final TextView item_name;

            /** The name text view. */
            final TextView name;

            /** The item image view. */
            final ImageView item_image;

            /**
             * Initializes the view holder.
             *
             * @param view The view
             */
            ViewHolder(View view) {
                super(view);
                item_name = (TextView) view.findViewById(R.id.item_name);
                name = (TextView) view.findViewById(R.id.content);
                item_image = (ImageView)view.findViewById(R.id.inbox_item_view);
            }
        }
    }

    /**
     * Async task that retrieves the user inbox.
     *
     * @author TCSS 450 Team 8
     * @version 1.0
     */
    private class UserInboxTask extends AsyncTask<String, Void, String> {
        /**
         * Retrieves the user inbox.
         *
         * @param urls The URL endpoint
         * @return The response from the async task
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                Log.e("urConnection", String.valueOf(url));
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
                    response = "Unable to download the list of users, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * If successful, we set up the recycler view.
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
                    mUserList = UserInbox.parseUserInboxJson(jsonObject.getString("users"));
                    if (!mUserList.isEmpty()) {
                        setupRecyclerView((RecyclerView) mRecyclerView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /** The bottom navigation view for the application. */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent h = new Intent(MessageInboxActivity.this, MainActivity.class);
                            h.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(h);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_camera:
                            Intent p = new Intent(MessageInboxActivity.this, PostActivity.class);
                            p.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(p);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_inbox:
                            break;
                        case R.id.nav_cart:
                            Intent c = new Intent(MessageInboxActivity.this, CartActivity.class);
                            c.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(c);
                            overridePendingTransition(0,0);
                            break;
                        case R.id.nav_person:
                            Intent np = new Intent(MessageInboxActivity.this, ProfileActivity.class);
                            np.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(np);
                            overridePendingTransition(0,0);
                            break;
                    }
                    return false;
                }
            };
    /**
     * Prepare to put the search item on menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(getApplicationContext(), "You tried to submit a search query",
                        Toast.LENGTH_LONG).show();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
