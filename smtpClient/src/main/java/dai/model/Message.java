package dai.model;

public class Message {
    final private String subject;
    final private String body;

   public Message( String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
