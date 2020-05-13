package inbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.udeal.Login;
import edu.tacoma.uw.udeal.R;
import model.Message;
import model.UserInbox;

/**

 */
public class InboxDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";

    private UserInbox mItem;
    private ImageButton sendBtn;
    private EditText messageTextField;
    private View recyclerView;
    private JSONObject mArguments;
    private List<Message> messageList;
    private SimpleItemRecyclerViewAdapter adapter;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);

        recyclerView = findViewById(R.id.inbox_detail_container);
        if (savedInstanceState == null) {
            mItem = (UserInbox)getIntent().getSerializableExtra(ARG_ITEM_ID);
            setTitle(mItem.getUserName());
        }
        sendBtn = (ImageButton)findViewById(R.id.sendButton);
        messageTextField = (EditText)findViewById(R.id.myMessageTextField);
        sendBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String msg = messageTextField.getText().toString();
                //Log.e("sendBtn", msg);
                if (msg.equals("")){
                    return;
                } else {
                    //Construct a JSONObject to build a formatted message to send.
                    mArguments = new JSONObject();
                    try {
                        StringBuilder url = new StringBuilder(getString(R.string.message));
                        mArguments.put(Message.SENDER, "Ai");
                        mArguments.put(Message.RECIPIENT, mItem.getUserName());
                        mArguments.put(Message.CONTENT, msg);
                        new MessageTaskPost().execute(url.toString());

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error with JSON creation: " + e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public void onResume(){
        StringBuilder url = new StringBuilder(getString(R.string.message));
        // use params, http://nguyen97-services-backend.herokuapp.com/message?sender=Ai&recipient=Test
        url.append("?sender=");
        url.append("Ai");
        url.append("&recipient=");
        url.append(mItem.getUserName());
        new MessageTaskGet().execute(url.toString());
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                StringBuilder url = new StringBuilder(getString(R.string.message));
                // use params, http://nguyen97-services-backend.herokuapp.com/message?sender=Ai&recipient=Test
                url.append("?sender=");
                url.append("Ai");
                url.append("&recipient=");
                url.append(mItem.getUserName());
                new MessageTaskGet().execute(url.toString());
            }
        }, delay);
        super.onResume();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (messageList != null){
            adapter = new SimpleItemRecyclerViewAdapter(this, messageList);
            recyclerView.setAdapter(adapter);
        }
    }
    private class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final InboxDetailActivity mParentActivity;
        private final List<Message> mValues;
        SimpleItemRecyclerViewAdapter(InboxDetailActivity parent, List<Message> items) {
            mValues = items;
            mParentActivity = parent;
            //Log.e("SimpleItemRecyclerViewAdapter", "SimpleItemRecyclerViewAdapter");
            //Log.e("inbox act", String.valueOf(items));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //
            View view;
            ViewHolder viewHolder;
            switch (viewType){
                case 0: //
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message, parent, false);
                    viewHolder = new ViewHolder(view, viewType);
                    return viewHolder;
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);
                    viewHolder = new ViewHolder(view, viewType);
                    return viewHolder;
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if ((mValues.get(position).getSender()).equals(mItem.getUserName()) ){
                holder.mIdView.setText(mValues.get(position).getSender());
                holder.mContentView.setText(mValues.get(position).getMessage());
            } else {
                holder.mContentView.setText(mValues.get(position).getMessage());
            }
            holder.itemView.setTag(mValues.get(position));
            //Log.e("onBindViewHolder", "onBindViewHolder");
        }

        @Override
        public int getItemViewType(int position) {
            int viewType = 1; //Default is 1
            if ((mValues.get(position).getSender()).equals(mItem.getUserName())) {
                viewType = 0; //if zero, their_message layout
            }
            return viewType;
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }
        public void addItem(Message msg){
            notifyDataSetChanged();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mIdView;
            public TextView mContentView;
            ViewHolder(View view, int viewType) {
                super(view);
                if (viewType == 0) { //their_message
                    mIdView = (TextView) view.findViewById(R.id.their_chat_name);
                    mContentView = (TextView) view.findViewById(R.id.their_chat_message);
                } else if (viewType == 1) {
                    mIdView = (TextView) view.findViewById(R.id.my_chat_message);
                    mContentView = (TextView) view.findViewById(R.id.my_chat_message);
                }
            }
        }
    }

    private class MessageTaskPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
                String response = "";
                HttpURLConnection urlConnection = null;
                for (String url : urls) {
                    //Log.e("urConnection", String.valueOf(url));
                    try {
                        URL urlObject = new URL(url);
                        urlConnection = (HttpURLConnection) urlObject.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setDoOutput(true);
                        OutputStreamWriter wr =
                                new OutputStreamWriter(urlConnection.getOutputStream());

                        wr.write(mArguments.toString());
                        wr.flush();
                        wr.close();
                        InputStream content = urlConnection.getInputStream();
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                        String s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }
                    } catch (Exception e) {
                        response = e.getMessage();
                    } finally {
                        if (urlConnection != null)
                            urlConnection.disconnect();
                    }
                }
                //Log.e("task reponse", response);
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
                    //Log.e("task messageTaskPost", String.valueOf(messageList));
                    messageTextField.setText("");
                    //Log.e("mArguments", String.valueOf(mArguments.get(Message.SENDER)));
                    messageList.add(new Message(mArguments.get(Message.SENDER).toString(),
                            mArguments.get(Message.RECIPIENT).toString(),
                            mArguments.get(Message.CONTENT).toString(), "0"));
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class MessageTaskGet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                //Log.e("urConnection", String.valueOf(url));
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-Type", "application/json");

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
            //Log.e("task reponse get", response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
                //Log.e("InboxDetailActivity", s);
                if (s.startsWith("Unable to")) {
                    Toast.makeText(getApplicationContext(), "Unable to download" + s, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    //Log.e("InboxDetailActivity", String.valueOf(jsonObject));
                    if (jsonObject.getBoolean("success") == true) {
                        messageList = Message.parseMessageJson(jsonObject.getString("message"));
                        //Log.e("task", String.valueOf(messageList));
                        if (!messageList.isEmpty()) {
                            setupRecyclerView((RecyclerView) recyclerView);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
    }
}
