package inbox;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import edu.tacoma.uw.udeal.Login;
import edu.tacoma.uw.udeal.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);

        if (savedInstanceState == null) {
            mItem = (UserInbox)getIntent().getSerializableExtra(ARG_ITEM_ID);
            setTitle(mItem.getUserName());
        }

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