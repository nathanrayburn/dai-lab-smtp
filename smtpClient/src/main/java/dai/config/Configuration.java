package dai.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import dai.model.Message;

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



    /**
     * Constructeur de la classe Configuration.
     * Lit les données de configuration à partir d'un fichier JSON et initialise les champs.
     *
     * @param jsonFilePath Chemin vers le fichier JSON de configuration.
     * @throws Exception Si une erreur survient lors de la lecture du fichier JSON.
     */
    public Configuration(String jsonFilePath) throws Exception{

        ReadJson readJson = new ReadJson();

        Map<String, Object> jsonData = readJson.read(jsonFilePath);

        victims = (List<String>) jsonData.get("emails");

        numberOfGroups = (int) (double) jsonData.get("numberOfGroups");

        smtpHost = (String) jsonData.get("smtpHost");

        smtpPort = (int) (double) jsonData.get("smtpPort");

        minNumberOfEmailsPerGroup = (int) (double) jsonData.get("minNumberOfEmailsPerGroup");

        maxNumberOfEmailsPerGroup = (int) (double) jsonData.get("maxNumberOfEmailsPerGroup");

        messageList = new ArrayList<>();
        var msgList = (List<Map<String, Object>>) jsonData.get("messages");

        for (var message : msgList) {
            Message msg = new Message((String) message.get("subject"), (String) message.get("body"));
            messageList.add(msg);
        }

        validateConfiguration();
    }

    public List<String> getVictims() {
        return victims;
    }

    public List<Message> getMessages() {
        return messageList;
    }

    //region Accessors
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

    //endregion Accessors

    //region Methods

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
                System.out.println("Adresse e-mail invalide trouvée : " + email);
                throw new Exception("Invalide e-mail address found : " + email);
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
                throw new Exception("Invalide message : " + message.getSubject() + " " + message.getBody());

            }
        }
    }
    //endregion Methods
}