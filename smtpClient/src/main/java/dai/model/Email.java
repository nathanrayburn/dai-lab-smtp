package dai.model;

import java.util.ArrayList;
import java.util.List;

public class
Email {
    private String sender;
    private List<String> recipients;
    private String subject;
    private String body;

    // Constructeur
    public Email(String sender, List<String> recipients, String subject, String body) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
    }
    private static Email createEmail(Group group){
        Message msg = group.getMessage();
        return new Email(group.getSender(),group.getRecipients(), msg.getSubject(), msg.getBody());
    }
    public static List<Email> createEmails(List<Group> groups){
        List<Email> emailList = new ArrayList<>();

        for(Group group : groups){
            emailList.add(createEmail(group));
        }
        return emailList;
    }
    // Getters et Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
