package dai.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import dai.model.Message;
import dai.network.SMTPClient;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Classe pour gérer la configuration de l'application depuis un fichier JSON.
 * Contient des paramètres de configuration tels que les adresses e-mail des victimes,
 * les paramètres du serveur SMTP, le nombre de groupes, etc.
 */
public class Configuration {

    private final List<String> victims;
    ArrayList<Message> messageList;
    private final String smtpHost;
    private final int smtpPort;
    private final int numberOfGroups;
    private final int minNumberOfEmailsPerGroup;
    private final int maxNumberOfEmailsPerGroup;
    private static final Logger LOGGER = Logger.getLogger(SMTPClient.class.getName());
    /**
     * Constructeur de la classe Configuration.
     * Lit les données de configuration à partir d'un fichier JSON et initialise les champs.
     *
     * @param jsonFilePath Chemin vers le fichier JSON de configuration.
     * @throws FileNotFoundException Si le fichier JSON n'est pas trouvé.
     * @throws IOException Si une erreur de lecture se produit.
     * @throws IllegalArgumentException Si la configuration est invalide.
     */
    public Configuration(String jsonFilePath) throws Exception, IOException, IllegalArgumentException {
        ReadJson readJson = new ReadJson();

        Map<String, Object> jsonData = readJson.read(jsonFilePath);

        if (jsonData == null) {
            throw new IOException("Erreur lors de la lecture du fichier de configuration JSON.");
        }

        victims = safelyCastToList(jsonData.get("emails"));
        numberOfGroups = safelyCastToInt(jsonData.get("numberOfGroups"));
        smtpHost = (String) jsonData.get("smtpHost");
        smtpPort = safelyCastToInt(jsonData.get("smtpPort"));
        minNumberOfEmailsPerGroup = safelyCastToInt(jsonData.get("minNumberOfEmailsPerGroup"));
        maxNumberOfEmailsPerGroup = safelyCastToInt(jsonData.get("maxNumberOfEmailsPerGroup"));



        List<Map<String, Object>> msgList = safelyCastToList(jsonData.get("messages"));
        messageList = new ArrayList<>();
        for (Map<String, Object> message : msgList) {
            String subject = (String) message.get("subject");
            String body = (String) message.get("body");
            messageList.add(new Message(subject, body));
        }

        validateConfiguration();
    }

    // region Accessors

    public List<String> getVictims() {
        return victims;
    }

    public List<Message> getMessages() {
        return messageList;
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

    public int getMinNumberOfEmailsPerGroup() {
        return minNumberOfEmailsPerGroup;
    }
    public int getMaxNumberOfEmailsPerGroup() {
        return maxNumberOfEmailsPerGroup;
    }

    // endregion Accessors



    // region Methods

    /**
     * Valide le nombre de groupes basé sur les paramètres de configuration.
     * @throws Exception Si la configuration n'est pas valide.
     */
    public void validateNbGroups() throws Exception {
        if(minNumberOfEmailsPerGroup > maxNumberOfEmailsPerGroup){
            throw new IllegalArgumentException("Minimum number of groups must be less than maximum number of groups");
        }
        if (victims.size() < numberOfGroups * maxNumberOfEmailsPerGroup) {
            throw new Exception("Error, not enough victims to form groups.\nNumber of groups : " + numberOfGroups + "\nMax number of emails per group : " + maxNumberOfEmailsPerGroup + "\nNumber of victims : " + victims.size() + "\n");
        }
    }

    /**
     * Valide l'ensemble de la configuration.
     * @throws Exception Si la configuration n'est pas valide.
     */
    public void validateConfiguration() throws Exception {
        validateEmails();
        validateMessages();
        validateNbGroups();
    }

    /**
     * Valide les adresses e-mail dans la liste des victimes.
     * @throws Exception Si une adresse e-mail est invalide.
     */
    private void validateEmails() throws Exception {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        for (String email : victims) {
            if (!emailPattern.matcher(email).matches()) {
                LOGGER.log(Level.INFO, "Adresse e-mail invalide trouvée : " + email);
                throw new Exception("E-mail invalide trouvée : " + email);
            }
        }
    }


    /**
     * Valide les messages dans la liste des messages.
     * @throws Exception Si un message est invalide.
     */
    private void validateMessages() throws Exception {
        for (Message message : messageList) {
            if (message.getSubject() == null || message.getBody() == null ||
                    message.getSubject().trim().isEmpty() || message.getBody().trim().isEmpty()) {
                LOGGER.log(Level.INFO, "Message invalide : " + message.getSubject() + " " + message.getBody());
                throw new Exception("Message Invalide : " + message.getSubject() + " " + message.getBody());
            }
        }
    }

    /**
     * Tente de convertir en toute sécurité un objet en List.
     *
     * @param object L'objet à convertir.
     * @return Une List ou lance une IllegalArgumentException si la conversion échoue.
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> safelyCastToList(Object object) {
        if (object instanceof List) {
            return (List<T>) object;
        } else {
            LOGGER.log(Level.INFO, "L'objet n'est pas une liste.");
            throw new IllegalArgumentException("L'objet n'est pas une liste.");
        }
    }

    /**
     * Tente de convertir en toute sécurité un objet en int.
     *
     * @param object L'objet à convertir.
     * @return Un int ou lance une IllegalArgumentException si la conversion échoue.
     */
    private int safelyCastToInt(Object object) {
        if (object instanceof Double) {
            return ((Double) object).intValue();
        } else {
            LOGGER.log(Level.INFO, "L'objet n'est pas un nombre.");
            throw new IllegalArgumentException("L'objet n'est pas un nombre.");
        }
    }
    // endregion Methods
}