package dai.network;

import dai.model.Email;
import dai.model.Group;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import dai.config.Configuration;

public class SMTPClient {
    private String smtpHost;
    private int smtpPort;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public SMTPClient(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }


    /**
     * Établit une connexion avec le serveur SMTP.
     */
    public void connect() throws Exception {
        socket = new Socket(smtpHost, smtpPort);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Lire la réponse du serveur
        readResponse();
    }

    /**
     * Lit une réponse du serveur SMTP.
     */
    private String readResponse() throws Exception {
        String response = reader.readLine();
        // Logique pour traiter la réponse ici
        System.out.println("SMTP Response: " + response);
        return response;
    }

    /**
     * Envoie une commande au serveur SMTP et lit la réponse.
     */
    public void sendCommand(String command) throws Exception {
        writer.println(command);
        readResponse();
    }

    public void configuration(){

        try{
            Configuration config = new Configuration("src/main/java/dai/config/config.json");

            if (!config.validate()) {
                System.out.println("La configuration n'est pas valide.");
                return;
            }
          //  List<Group> groups = config.createGroups();
          // sendEmails(groups, config);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Envoie un email via le serveur SMTP.
     */
    public void sendEmail(String from, List<String> recipients, String subject, String body) throws Exception {
        sendCommand("HELO " + smtpHost);
        sendCommand("MAIL FROM: <" + from + ">");
        for (String recipient : recipients) {
            sendCommand("RCPT TO: <" + recipient + ">");
        }
        sendCommand("DATA");
        writer.println("Subject: " + subject);
        writer.println();
        writer.println(body);
        writer.println(".");
        readResponse();
        sendCommand("QUIT");
    }

    /**
     * Ferme la connexion avec le serveur SMTP.
     */
    public void close() throws Exception {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (writer != null) {
            writer.close();
        }
        if (reader != null) {
            reader.close();
        }
    }

}


