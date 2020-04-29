//package inbox;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import java.util.List;
//
//import edu.tacoma.uw.udeal.Login;
//import edu.tacoma.uw.udeal.R;
//import inbox.dummy.DummyContent;
//import model.Message;
//import model.UserInbox;
//
///**
// * A fragment representing a single Inbox detail screen.
// * This fragment is either contained in a {@link InboxListActivity}
// * in two-pane mode (on tablets) or a {@link InboxDetailActivity}
// * on handsets.
// */
//public class InboxDetailFragment extends Fragment {
//    /**
//     * The fragment argument representing the item ID that this fragment
//     * represents.
//     */
//    public static final String ARG_ITEM_ID = "item_id";
//    private UserInbox mItem;
//    private ImageButton sendBtn;
//    private EditText messageTextField;
//    private View mRecyclerView;
//
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public InboxDetailFragment() {
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        if (getArguments().containsKey(ARG_ITEM_ID)) {
////            mItem = (UserInbox) getArguments().getSerializable(ARG_ITEM_ID);
////            Activity activity = this.getActivity();
////            activity.setTitle(mItem.getUserName());
////        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_inbox_detail, container, false);
//        getActivity().setTitle("Inbox");
//
//        mRecyclerView = view.findViewById(R.id.inbox_detail_container);
//
//
//        if (savedInstanceState == null) {
//            mItem = (UserInbox)getArguments().getSerializable(ARG_ITEM_ID);
//        }
//        assert mRecyclerView != null;
//        setupRecyclerView((RecyclerView) mRecyclerView);
//
//        sendBtn = (ImageButton)view.findViewById(R.id.sendButton);
//        messageTextField = (EditText)view.findViewById(R.id.myMessageTextField);
//        sendBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                String msg = messageTextField.getText().toString();
//                if (!msg.equals("")){
//                    Message myMessage = new Message(Login.CURRENT_USER,mItem.getUserName(), messageTextField.getText().toString() );
//
//                } else {
//                    return;
//                }
//            }
//        });
//
//        return view;
//    }
//
//    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        Log.e("testin", String.valueOf(this));
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS));
//    }
//    public static class SimpleItemRecyclerViewAdapter
//            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
//
//        private final InboxDetailFragment mParentActivity;
//        private final List<DummyContent.DummyItem> mValues;
////        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        };
//
//        SimpleItemRecyclerViewAdapter(InboxDetailFragment parent, List<DummyContent.DummyItem> items) {
//            mValues = items;
//            Log.e("inbox act", String.valueOf(items));
//            mParentActivity = parent;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.my_message, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//            holder.mIdView.setText(mValues.get(position).id);
//            holder.mContentView.setText(mValues.get(position).content);
//            holder.itemView.setTag(mValues.get(position));
//            //Log.e("testing point a", String.valueOf(holder));
//            //holder.itemView.setOnClickListener(mOnClickListener);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mValues.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            final TextView mIdView;
//            final TextView mContentView;
//
//            ViewHolder(View view) {
//                super(view);
//                mIdView = (TextView) view.findViewById(R.id.my_chat_message);
//                mContentView = (TextView) view.findViewById(R.id.my_chat_message);
//            }
//        }
//    }
//}
