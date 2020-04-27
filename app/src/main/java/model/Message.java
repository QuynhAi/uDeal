package model;

public class Message {
    private String Username;
    private String Message;

    public Message(String username, String message) {
        Username = username;
        Message = message;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}