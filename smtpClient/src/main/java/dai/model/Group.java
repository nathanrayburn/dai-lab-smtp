package dai.model;

import java.util.ArrayList;
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
    public static List<Group> createGroups(int minNumberOfEmailsPerGroup, int maxNumberOfEmailsPerGroup, int numberOfGroups, List<String> victims){
        List<Group> groups = new ArrayList<>();

        int groupSize = Math.max(maxNumberOfEmailsPerGroup, victims.size() / numberOfGroups);
        for (int i = 0; i < numberOfGroups; i++) {

            int startIndex = i * groupSize;

            int endIndex = Math.min(startIndex + groupSize, victims.size());

            List<String> selectedVictims = victims.subList(startIndex, endIndex);

            if (selectedVictims.size() < minNumberOfEmailsPerGroup || selectedVictims.size() >= maxNumberOfEmailsPerGroup) {
                break; // Pas assez de victimes pour former un groupe
            }

            Email sender = new Email(selectedVictims.remove(0), null, null, null);
            List<Email> recipients = new ArrayList<>();
            for (String recipient : selectedVictims) {
                recipients.add(new Email(recipient, null, null, null));
            }
            groups.add(new Group(sender, recipients));
        }

        return groups;
    }
}

