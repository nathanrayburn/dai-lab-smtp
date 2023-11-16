package dai.config;

import dai.model.Email;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Classe pour gérer la configuration de l'application depuis un fichier JSON.
 */
public class Configuration {
    private List<String> victims;
    private List<String> messages;
    private String smtpHost;
    private int smtpPort;
    private int numberOfGroups;

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


        List<Map<String, Object>> messageList = (List<Map<String, Object>>) jsonData.get("messages");
        if (messageList != null) {
            for (Map<String, Object> message : messageList) {
                String messageContent = (String) message.get("body"); // Assuming the message body is stored under the key "body"
                messages.add(messageContent);
            }
        }
    }

    // Getters pour accéder aux paramètres
  /*  private Email createEmailFromMap(Map<String, Object> emailMap) {
        // Implement logic to create an Email object from the emailMap
        // Assuming you have a method to convert a Map to an Email object
        // Example implementation:
        String sender = (String) emailMap.get("sender");
        List<String> recipients = (List<String>) emailMap.get("recipients");
        String subject = (String) emailMap.get("subject");
        String body = (String) emailMap.get("body");

        // Create an Email object with the extracted data
        if (sender != null && recipients != null && subject != null && body != null) {
            return new Email(sender, recipients, subject, body);
        } else {
            // Handle missing or invalid data if needed
            return null;
        }
    } */
    public List<String> getVictims() {
        return victims;
    }

    public List<String> getMessages() {
        return messages;
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
  /*  public boolean validate() {
        return validateEmails() && validateGroups() && validateMessages();
    } */

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
     * Valide le nombre de groupes.
     *
     * @return true si le nombre de groupes est adéquat, false sinon.
     */
    private boolean validateGroups() {
        if (numberOfGroups < 1 || numberOfGroups > victims.size() / 2) {
            System.out.println("Nombre de groupes invalide : " + numberOfGroups);
            return false;
        }
        return true;
    }

    /**
     * Valide les messages.
     *
     * @return true si tous les messages ont un sujet et un corps, false sinon.
     */
 /*   private boolean validateMessages() {
        for (String message : messages) {
            if (message.getSubject() == null || message.getSubject().isEmpty() ||
                    message.getBody() == null || message.getBody().isEmpty()) {
                System.out.println("Message invalide trouvé.");
                return false;
            }
        }
        return true;
    }
        */
}