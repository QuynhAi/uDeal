package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class Message {
    /** The sender. */
    private String sender;

    /** The receive. */
    private String receiver;

    /** The message. */
    private String message;

    /** The time. */
    private String time;

    /** The item. */
    private String item;


    /** The sender string. */
    public static final String ITEMID = "itemid";

    /** The sender string. */
    public static final String SENDER = "sender";

    /** The recipient string. */
    public static final String RECIPIENT = "recipient";

    /** The content string. */
    public static final String CONTENT = "content";

    /** The time stamp. */
    public static final String TIME_STAMP = "ts";

    /**
     * Initializes the fields in the message.
     *
     * @param sender The sender
     * @param receiver The receiver
     * @param message The message
     * @param time The time
     */
    public Message(String item, String sender, String receiver, String message, String time) {
        this.item = item;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    /**
     * Gets the sender.
     *
     * @return The sender
     */
    public String getSender(){return sender;}

    /**
     * Gets the receiver.
     *
     * @return The receiver.
     */
    public String getReceiver(){return receiver;}

    /**
     * Gets the message.
     *
     * @return The message.
     */
    public String getMessage(){return message;}

    /**
     * Gets the time stamp.
     *
     * @return The time stamp.
     */
    public String getTimeStamp(){return time;}

    /**
     * Parses the JSON message and returns the message list.
     *
     * @param messageJson The JSON message to be parsed.
     * @return The list of message.
     * @throws JSONException if the JSON object cannot be created
     */
    public static List<Message> parseMessageJson(String messageJson) throws JSONException {
        List<Message> msgList = new ArrayList<>();
        if(messageJson != null){
            JSONArray arr = new JSONArray(messageJson);
            //Log.e("MESSAGE", String.valueOf(arr));
            for (int i = 0; i< arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Message msg = new Message(obj.getString(Message.ITEMID), obj.getString(Message.SENDER),
                        obj.getString(Message.RECIPIENT), obj.getString(Message.CONTENT), obj.getString(Message.TIME_STAMP));
                msgList.add(msg);

            }
        }
        return msgList;
    }
}