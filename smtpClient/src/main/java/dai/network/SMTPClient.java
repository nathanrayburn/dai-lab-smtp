package dai.network;

import dai.model.Email;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


public class SMTPClient {
    private static final Logger LOGGER = Logger.getLogger(SMTPClient.class.getName());
    private final String smtpHost;
    private final int smtpPort;
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
        readResponse();
    }

    /**
     * Lit une réponse du serveur SMTP.
     */
    private void readResponse() throws Exception {
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = reader.readLine();
            if (line == null) {
                throw new Exception("La connexion au serveur SMTP a été perdue.");
            }
            sb.append(line).append("\n");
        } while (!line.matches("^(220|250|354|221) .*") && !line.matches("^([45])[0-9]{2} .*"));

        LOGGER.log(Level.INFO, "SMTP Response: {0}", sb.toString());
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
        writer.print(body + "\r\n");
        writer.print("\r\n");
        writer.flush();
        sendCommand(".");
    }
    /**
     * Envoie un email à un groupe entier.
     */
    public void sendGroupEmail(Email e) throws Exception {
        sendEmail(e.getSender(), e.getRecipients(), e.getSubject(), e.getBody());
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


