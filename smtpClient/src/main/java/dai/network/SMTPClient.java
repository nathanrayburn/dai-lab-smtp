package dai.network;

import dai.model.Message;
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
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = reader.readLine();
            if (line == null) { // Si null, la connexion a été fermée inopinément
                throw new Exception("La connexion au serveur SMTP a été perdue.");
            }
            sb.append(line).append("\n");
        } while (!line.matches("^(220|250|354|221) .*") && !line.matches("^(4|5)[0-9]{2} .*"));
        System.out.println("SMTP Response: " + sb.toString());
        return sb.toString();
    }

    /**
     * Envoie une commande au serveur SMTP et lit la réponse.
     */
    public void sendCommand(String command) throws Exception {
        writer.print(command + "\r\n");
        writer.flush();
        readResponse();
    }

    /**
     * Envoie un email via le serveur SMTP.
     */
    public void sendEmail(String from, List<String> recipients, String subject, String body) throws Exception {
        sendCommand("EHLO " + smtpHost);
        sendCommand("MAIL FROM: <" + from + ">");

        for (String recipient : recipients) {
            sendCommand("RCPT TO: <" + recipient + ">");
        }

        sendCommand("DATA");
        writer.print("From: <" + from + ">" + "\r\n");
        writer.print("To: ");
        for(String recipient : recipients){
            if(recipient.equals(recipients.get(recipients.size()-1)))
                writer.print("<" + recipient + ">");
            else
                writer.print("<" + recipient + ">, ");
        }
        writer.print("\r\n");
        writer.print("Subject: " + subject + "\r\n");
        writer.print("\r\n");
        writer.print(body + "\r\n"); // Here's the change made to remove "Body: "
        writer.print("\r\n");
        writer.flush();
        sendCommand(".");
    }




    /**
     * Envoie un email à un groupe entier.
     */
    public void sendGroupEmail(Group group) throws Exception {
        var message = group.getMessage();
        sendEmail(group.getSender(), group.getRecipients(), message.getSubject(), message.getBody());
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


