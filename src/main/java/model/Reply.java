package model;

import java.util.Date;

/**
 * Created by markmo on 4/04/2016.
 */
public class Reply {

    private String userId;
    private String userName;
    private String userGroup;
    private String replyText;
    private Date date;

    public Reply(String userId, String userName, String userGroup, String replyText, Date date) {
        this.userId = userId;
        this.userName = userName;
        this.userGroup = userGroup;
        this.replyText = replyText;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        boolean multiline = false;
        String indent = "";
        String delim = " ";
        String edgeDelim = "";
        if (multiline) {
            indent = "    ";
            delim = edgeDelim = "\n";
        }
        StringBuilder sb = new StringBuilder("{");
        sb
                .append(edgeDelim)
                .append(indent).append("\"userId\": \"").append(userId).append("\",").append(delim)
                .append(indent).append("\"userName\": \"").append(userName).append("\",").append(delim)
                .append(indent).append("\"userGroup\": \"").append(userGroup).append("\",").append(delim)
                .append(indent).append("\"replyText\": \"").append(replyText).append("\",").append(delim)
                .append(indent).append("\"date\": \"").append(date).append("\"")
                .append(edgeDelim).append("}");
        return sb.toString();
    }
}
