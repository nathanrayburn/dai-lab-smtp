package dai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un groupe avec un expéditeur, des destinataires et un message.
 */
public class Group {
    final private String sender;
    final private List<String> recipients;
    final private Message message;

    // Constructeur
    public Group(String sender, ArrayList<String> recipients, Message message) {
        this.sender = sender;
        this.recipients = recipients;
        this.message = message;
    }



    /**
     * Crée une liste de groupes basée sur les paramètres fournis.
     * Mélange les listes de victimes et de messages et forme des groupes aléatoires.
     *
     * @param minNumberOfEmailsPerGroup Nombre minimum d'e-mails par groupe.
     * @param maxNumberOfEmailsPerGroup Nombre maximum d'e-mails par groupe.
     * @param numberOfGroups Nombre total de groupes à former.
     * @param victims Liste des victimes (destinataires).
     * @param messages Liste des messages disponibles.
     * @return Liste des groupes créés ou null si une erreur se produit.
     */
    public static List<Group> createGroups(int minNumberOfEmailsPerGroup, int maxNumberOfEmailsPerGroup, int numberOfGroups, List<String> victims ,List<Message> messages){
        List<Group> groups = new ArrayList<>();
        Collections.shuffle(victims);
        Collections.shuffle(messages);
        try{

            // random between min and max number of emails per group
            int groupSize = (int) (Math.random() * (maxNumberOfEmailsPerGroup - minNumberOfEmailsPerGroup))
                    + minNumberOfEmailsPerGroup;

            for (int i = 0; i < numberOfGroups; i++) {

                int startIndex = i * groupSize;

                int endIndex = i* groupSize + (groupSize);

                if (victims.size() < endIndex) {
                    throw new Exception("Error, not enough victims to form a group");
                }

                ArrayList<String> selectedVictims = new ArrayList<>(victims.subList(startIndex, endIndex));

                String sender = selectedVictims.remove(0);

                // get random message
                Message message = messages.get((int) (Math.random() * messages.size()));

                groups.add(new Group(sender, selectedVictims, message));
            }

            return groups;
        }catch(Exception e){
            System.out.println(e.getMessage()+"\n");
        }
        return null;
    }

    // region Accessors

    public Message getMessage() {
        return message;
    }
    public String getSender() {
        return sender;
    }
    public List<String> getRecipients() {
        return recipients;
    }

    // endregion Accessors
}