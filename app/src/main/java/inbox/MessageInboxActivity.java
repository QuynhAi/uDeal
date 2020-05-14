package inbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.udeal.CartActivity;
import edu.tacoma.uw.udeal.MainActivity;
import edu.tacoma.uw.udeal.PostActivity;
import edu.tacoma.uw.udeal.ProfileActivity;
import edu.tacoma.uw.udeal.R;
import model.UserInbox;

public class MessageInboxActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private List<UserInbox> mUserList;
    private RecyclerView mRecyclerView;
    private int mColumnCount = 1;
    private String current;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_toolbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
//        if(mUserList == null){
//            new UserInboxTask().execute(getString(R.string.register));
//        }

        SharedPreferences settings = getSharedPreferences((getString(R.string.LOGIN_PREFS)), Context.MODE_PRIVATE);
        current = settings.getString(getString(R.string.username), "");
        mRecyclerView = findViewById(R.id.fragment_container);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
        if (findViewById(R.id.inbox_detail_container) != null) {
            mTwoPane = true;
        }
    }

    public void onResume(){
        super.onResume();
        if(mUserList == null){
            //new MessageInboxActivity.UserInboxTask().execute(getString(R.string.register));
            new UserInboxTask().execute(getString(R.string.members));
        }
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mUserList != null){
            recyclerView.setAdapter(new MessageInboxActivity.SimpleItemRecyclerViewAdapter(this, mUserList, mTwoPane));
        }
    }

    private class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MessageInboxActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MessageInboxActivity mParentActivity;
        private final List<UserInbox> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInbox item = (UserInbox) view.getTag();
                if (mTwoPane) {
//                    Original
//                    Bundle arguments = new Bundle();
//                    arguments.putSerializable(InboxDetailFragment.ARG_ITEM_ID, item);
//                    InboxDetailFragment fragment = new InboxDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getFragmentManager().beginTransaction()
//                            .replace(R.id.inbox_detail_container, fragment)
//                            .commit();

                    // Testing
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
            //holder.profile.setText(mValues.get(position).id);
            holder.name.setText(mValues.get(position).getOtherUserName());
            holder.profile.setImageResource(R.drawable.ic_person_black_24dp);
            holder.item_image.setImageResource(R.drawable.ic_card_giftcard_black_24dp);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView profile;
            final TextView name;
            final ImageView item_image;

            ViewHolder(View view) {
                super(view);
                profile = (ImageView) view.findViewById(R.id.inbox_profile);
                name = (TextView) view.findViewById(R.id.content);
                item_image = (ImageView)view.findViewById(R.id.inbox_item_view);
            }
        }
    }

    private class UserInboxTask extends AsyncTask<String, Void, String> {
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
                    mUserList = UserInbox.parseUserInboxJson(jsonObject.getString("names"));
                    if (!mUserList.isEmpty()) {
                        setupRecyclerView((RecyclerView) mRecyclerView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
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
}
