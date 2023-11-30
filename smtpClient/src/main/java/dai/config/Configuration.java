package dai.config;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import dai.model.Group;
import dai.model.Message;

/**
 * Classe pour gérer la configuration de l'application depuis un fichier JSON.
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
     *
     * @param jsonFilePath Chemin vers le fichier JSON de configuration.
     */
    public Configuration(String jsonFilePath) {

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
        // TO DO check that message respect UTF 8
        for (var message : msgList) {
            Message msg = new Message((String) message.get("subject"), (String) message.get("body"));
            messageList.add(msg);
        }
        try{
            validate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

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
    public void validate() throws Exception {
         validateEmails();
         validateMessages();
    }

    private void validateEmails() throws Exception {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        for (String email : victims) {
            if (!emailPattern.matcher(email).matches()) {
                System.out.println("Adresse e-mail invalide trouvée : " + email);
                throw new Exception("Invalide e-mail address found : " + email);
            }
        }
    }







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