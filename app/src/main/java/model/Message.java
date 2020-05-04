package model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String sender;
    private String receiver;
    private String message;
    private String time;
    public static final String SENDER = "sender";
    public static final String RECIPIENT = "recipient";
    public static final String CONTENT = "content";
    public static final String TIME_STAMP = "ts";

    public Message(String sender, String receiver, String message, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    public String getSender(){return sender;}
    public String getReceiver(){return receiver;}
    public String getMessage(){return message;}
    public String getTimeStamp(){return time;}

    public static List<Message> parseMessageJson(String messageJson) throws JSONException {
        List<Message> msgList = new ArrayList<>();
        if(messageJson != null){
            JSONArray arr = new JSONArray(messageJson);
            //Log.e("MESSAGE", String.valueOf(arr));
            for (int i = 0; i< arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Message msg = new Message(obj.getString(Message.SENDER),
                        obj.getString(Message.RECIPIENT), obj.getString(Message.CONTENT), obj.getString(Message.TIME_STAMP));
                msgList.add(msg);

            }
        }
        return msgList;
    }
}