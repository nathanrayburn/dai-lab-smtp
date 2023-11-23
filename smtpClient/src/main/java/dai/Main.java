package dai;

/*package main;

import config.Configuration;
import model.Email;
import model.Group;
import network.SMTPClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Charger la configuration
            Configuration config = new Configuration("chemin/vers/le/fichier/config.json");

            // Vérifier la validité de la configuration
            if (!config.validate()) {
                System.out.println("La configuration n'est pas valide.");
                return;
            }

            // Créer des groupes
            List<Group> groups = createGroups(config);

            // Envoyer les emails
            sendEmails(groups, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Group> createGroups(Configuration config) {
        List<Group> groups = new ArrayList<>();
        List<String> victims = new ArrayList<>(config.getVictims());
        Collections.shuffle(victims);

        int groupSize = Math.max(3, victims.size() / config.getNumberOfGroups());

        for (int i = 0; i < config.getNumberOfGroups(); i++) {
            int startIndex = i * groupSize;
            int endIndex = Math.min(startIndex + groupSize, victims.size());

            List<String> selectedVictims = victims.subList(startIndex, endIndex);
            if (selectedVictims.size() < 3) {
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

    private static void sendEmails(List<Group> groups, Configuration config) throws Exception {
        SMTPClient client = new SMTPClient(config.getSmtpHost(), config.getSmtpPort());
        client.connect();

        for (Group group : groups) {
            Email senderEmail = group.getSender();
            List<String> recipients = new ArrayList<>();
            for (Email recipientEmail : group.getRecipients()) {
                recipients.add(recipientEmail.getSender());
            }

            Email message = selectRandomMessage(config.getMessages());
            client.sendEmail(senderEmail.getSender(), recipients, message.getSubject(), message.getBody());
        }

        client.close();
    }

    private static Email selectRandomMessage(List<Email> messages) {
        Collections.shuffle(messages);
        return messages.get(0);
    }
}
*/