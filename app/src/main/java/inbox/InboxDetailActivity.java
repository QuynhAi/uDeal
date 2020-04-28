package inbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edu.tacoma.uw.udeal.Login;
import edu.tacoma.uw.udeal.R;
import inbox.dummy.DummyContent;
import model.Message;
import model.UserInbox;

/**
 * An activity representing a single Inbox detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link InboxListActivity}.
 */
public class InboxDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private UserInbox mItem;
    private ImageButton sendBtn;
    private EditText messageTextField;
    private View recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);

        recyclerView = findViewById(R.id.inbox_detail_container);
        if (savedInstanceState == null) {
            mItem = (UserInbox)getIntent().getSerializableExtra(ARG_ITEM_ID);
            setTitle(mItem.getUserName());
        }
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        sendBtn = (ImageButton)findViewById(R.id.sendButton);
        messageTextField = (EditText)findViewById(R.id.myMessageTextField);
        sendBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String msg = messageTextField.getText().toString();
                if (!msg.equals("")){
                    Message myMessage = new Message(Login.CURRENT_USER,mItem.getUserName(), messageTextField.getText().toString() );

                } else {
                    return;
                }
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.e("testin", String.valueOf(this));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS));
    }
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final InboxDetailActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
//        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        };

        SimpleItemRecyclerViewAdapter(InboxDetailActivity parent, List<DummyContent.DummyItem> items) {
            mValues = items;
            Log.e("inbox act", String.valueOf(items));
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);
            holder.itemView.setTag(mValues.get(position));
            //Log.e("testing point a", String.valueOf(holder));
            //holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.my_chat_message);
                mContentView = (TextView) view.findViewById(R.id.my_chat_message);
            }
        }
    }
}





//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//
//            navigateUpTo(new Intent(this, InboxListActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//        if (savedInstanceState == null) {
//            Bundle arguments = new Bundle();
//
//            if(getIntent().getSerializableExtra(InboxDetailFragment.ARG_ITEM_ID) != null){
//                arguments.putSerializable(InboxDetailFragment.ARG_ITEM_ID,
//                        getIntent().getSerializableExtra(InboxDetailFragment.ARG_ITEM_ID));
//                InboxDetailFragment fragment = new InboxDetailFragment();
//                fragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.inbox_detail_container, fragment)
//                        .commit();
//            }
//        }