package dai;

import dai.*;
import dai.config.Configuration;
import dai.model.Group;
import dai.network.SMTPClient;
import java.util.List;
import dai.model.Email;

public class Main {

    /**
     * Affiche un message d'erreur et quitte le programme.
     *
     * @param message Le message d'erreur à afficher.
     */
    public static void exitWithError(String message) {
        System.out.println(message);
        System.exit(1);
    }

    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println();
            exitWithError("Error: the first parameter must be the config file. Usage: java -jar smtpClient.jar <config_file>");
        }

        try {
            // Configuration et préparation du client SMTP
            Configuration config = new Configuration(args[0]);
            SMTPClient client = new SMTPClient(config.getSmtpHost(), config.getSmtpPort());

            // Création des groupes d'emails
            List<Group> groups = Group.createGroups(config.getMinNumberOfEmailsPerGroup(), config.getMaxNumberOfEmailsPerGroup(), config.getNumberOfGroups(), config.getVictims(), config.getMessages());
            // Vérification de la formation des groupes

            if(groups == null) {
                exitWithError("Error: not enough victims to create groups");
            }else{
                // Envoi des emails
                List<Email> emailList = Email.createEmails(groups);
                client.connect();
                for (Email e : emailList ) {
                    client.sendGroupEmail(e);
                }
                client.close();
            }
        }catch (Exception e) {
            exitWithError("Error: " + e.getMessage());
        }
    }
}
