package dai.model;

/**
 * Classe représentant un message avec un sujet et un corps.
 */
public class Message {
    final private String subject;
    final private String body;

   public Message( String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    // region Accessors

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    // endregion Accessors
}
