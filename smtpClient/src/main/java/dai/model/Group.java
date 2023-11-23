package dai.model;

import java.util.List;

public class Group {
    private Email sender;
    private List<Email> recipients;

    // Constructeur
    public Group(Email sender, List<Email> recipients) {
        this.sender = sender;
        this.recipients = recipients;
    }

    // Getters et Setters
    public Email getSender() {
        return sender;
    }

    public void setSender(Email sender) {
        this.sender = sender;
    }

    public List<Email> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Email> recipients) {
        this.recipients = recipients;
    }
}

