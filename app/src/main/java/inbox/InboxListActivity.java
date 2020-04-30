package inbox;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.tacoma.uw.udeal.R;

import model.UserInbox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * An activity representing a list of Inboxes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link InboxDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class InboxListActivity extends Fragment {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<UserInbox> mUserList;
    private RecyclerView mRecyclerView;
    private int mColumnCount = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inbox_list, container, false);
        getActivity().setTitle("Inbox");

        mRecyclerView = view.findViewById(R.id.inbox_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
        if (getActivity().findViewById(R.id.inbox_detail_container) != null) {
            mTwoPane = true;
        }
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mUserList == null){
            new UserInboxTask().execute(getString(R.string.members));
        }
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mUserList != null){
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mUserList, mTwoPane));
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final InboxListActivity mParentActivity;
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
                    Intent intent = new Intent(context, InboxDetailActivity.class);
                    intent.putExtra(InboxDetailActivity.ARG_ITEM_ID, item);
                    context.startActivity(intent);
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, InboxDetailActivity.class);
                    intent.putExtra(InboxDetailActivity.ARG_ITEM_ID, item);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(InboxListActivity parent,
                                      List<UserInbox> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inbox_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.profile.setText(mValues.get(position).id);
            holder.name.setText(mValues.get(position).getUserName());
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
                Toast.makeText(getContext(), "Unable to download" + s, Toast.LENGTH_SHORT)
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
                Toast.makeText(getContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
