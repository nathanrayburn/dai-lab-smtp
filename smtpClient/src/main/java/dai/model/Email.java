package dai.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un e-mail avec un expéditeur, des destinataires, un sujet et un corps de message.
 */
public class Email {
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

    /**
     * Crée un objet Email à partir d'un groupe.
     *
     * @param group Groupe à partir duquel créer l'e-mail.
     * @return Email nouvellement créé.
     */
    private static Email createEmail(Group group){
        Message msg = group.getMessage();
        return new Email(group.getSender(),group.getRecipients(), msg.getSubject(), msg.getBody());
    }

    /**
     * Crée une liste d'e-mails à partir d'une liste de groupes.
     *
     * @param groups Liste de groupes pour la création des e-mails.
     * @return Liste des e-mails créés.
     */
    public static List<Email> createEmails(List<Group> groups){
        List<Email> emailList = new ArrayList<>();

        for(Group group : groups){
            emailList.add(createEmail(group));
        }
        return emailList;
    }
    // region Accessors
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

    // endregion Accessors
}
