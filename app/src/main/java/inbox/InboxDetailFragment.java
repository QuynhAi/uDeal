package inbox;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.udeal.R;
import model.UserInbox;

/**
 * A fragment representing a single Inbox detail screen.
 * This fragment is either contained in a {@link InboxListActivity}
 * in two-pane mode (on tablets) or a {@link InboxDetailActivity}
 * on handsets.
 */
public class InboxDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private UserInbox mItem;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InboxDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = (UserInbox) getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            activity.setTitle(mItem.getUserName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inbox_detail, container, false);
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.inbox_detail)).setText("place holder");
        }
        return rootView;
    }
}
