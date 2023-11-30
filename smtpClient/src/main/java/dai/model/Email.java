package dai.model;

import java.util.ArrayList;
import java.util.List;

public class
Email {
    final private String sender;
    final private List<String> recipients;
    final private String subject;
    final private String body;

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

    public List<String> getRecipients() {
        return recipients;
    }


    public String getSubject() {
        return subject;
    }


    public String getBody() {
        return body;
    }
}
