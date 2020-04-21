package edu.tacoma.uw.udeal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Chat extends Fragment {
    private EditText myMessageTextField;
    private ImageButton sendButton;

    public static final String TAG  = "Chat";
    public static String uniqueId;

    private String Username;

    private Boolean hasConnection = false;

    private ListView messageListView;
    private MessageAdapter messageAdapter;

    private Thread thread2;
    private boolean startTyping = false;
    private int time = 2;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://udeal-services-backend.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    @SuppressLint("HandlerLeak")
    Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: typing stopped " + startTyping);
            if(time == 0){
                //getActivity().setTitle("SocketIO");
                Log.i(TAG, "handleMessage: typing stopped time is " + time);
                startTyping = false;
                time = 2;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Inbox");

        final View rootview = inflater.inflate(R.layout.chat, container, false);

        Username = getActivity().getIntent().getStringExtra("username");
        uniqueId = UUID.randomUUID().toString();
        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }

        myMessageTextField = (EditText) rootview.findViewById(R.id.myMessageTextField);
        sendButton = (ImageButton) rootview.findViewById(R.id.sendButton);
        messageListView = rootview.findViewById(R.id.messageListView);
        List<MessageFormat> messageFormatList = new ArrayList<>();
        messageAdapter = new MessageAdapter(rootview.getContext(), R.layout.item_message, messageFormatList);
        messageListView.setAdapter(messageAdapter);

        if(hasConnection){

        }else {
            mSocket.connect();
            //mSocket.on("connect user", onNewUser);
            //mSocket.on("chat message", onNewMessage);
            //mSocket.on("on typing", onTyping);
            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username + " Connected");
                mSocket.emit("connect user", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        hasConnection = true;

        sendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String message = myMessageTextField.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT)
                        .show();
                myMessageTextField.setText("");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", message);
                    jsonObject.put("username", Username);
                    jsonObject.put("uniqueId", uniqueId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootview;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasConnection", hasConnection);
    }
}
