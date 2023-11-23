package dai.config;

import dai.model.Email;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import dai.model.Group;

/**
 * Classe pour gérer la configuration de l'application depuis un fichier JSON.
 */
public class Configuration {
    private List<String> victims;
    List<Map<String, Object>> messageList;
    private String smtpHost;
    private int smtpPort;
    private int numberOfGroups;

    private final int minNumberOfEmailsPerGroup;
    private final int maxNumberOfEmailsPerGroup;


    /**
     * Constructeur de la classe Configuration.
     *
     * @param jsonFilePath Chemin vers le fichier JSON de configuration.
     * @throws Exception En cas d'erreur de lecture du fichier ou de parsing JSON.
     */
    public Configuration(String jsonFilePath) throws Exception {
        // Lecture du contenu du fichier JSON
        ReadJson readJson = new ReadJson();

        Map<String, Object> jsonData = readJson.read(jsonFilePath);

        victims = (List<String>) jsonData.get("emails");

        numberOfGroups= (int)(double) jsonData.get("numberOfGroups");

        smtpHost = (String) jsonData.get("smtpHost");

        smtpPort = (int)(double) jsonData.get("smtpPort");

        minNumberOfEmailsPerGroup = (int)(double) jsonData.get("minNumberOfEmailsPerGroup");

        maxNumberOfEmailsPerGroup = (int)(double) jsonData.get("maxNumberOfEmailsPerGroup");

    }

    public List<String> getVictims() {
        return victims;
    }

    public List<Map<String, Object>> getMessages() {
        return messageList;
    }
    public void createGroups(){
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
    public String getSmtpHost() {
        return smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    /**
     * Valide la configuration chargée.
     *
     * @return true si la configuration est valide, false sinon.
     */
    public boolean validate() {
        return validateEmails() && validateMessages();
    }

    /**
     * Valide les adresses e-mail.
     *
     * @return true si toutes les adresses e-mail sont valides, false sinon.
     */
    private boolean validateEmails() {
        Pattern emailPattern = Pattern.compile("^.+@.+$");
        for (String email : victims) {
            if (!emailPattern.matcher(email.toString()).matches()) {
                System.out.println("Adresse e-mail invalide trouvée : " + email);

                return false;
            }
        }
        return true;
    }


    /**
     * Valide les messages.
     *
     * @return true si tous les messages ont un sujet et un corps, false sinon.
     */
    private boolean validateMessages() {
        for (var message : messageList) {
            if (message.get("id").toString().isEmpty()|| message.get("subject").toString().isEmpty() ||
                    message.get("body").toString().isEmpty()) {
                System.out.println("Message invalide trouvé.");
                return false;
            }
        }
        return true;
    }

}