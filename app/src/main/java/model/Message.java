package model;

public class Message {
    private String sender;
    private String receiver;
    private String message;
    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String MESSAGE = "message";

    public Message(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getSender(){return sender;}
    public String getReceiver(){return receiver;}
    public String getMessage(){return message;}
}