package dai.network;

import dai.model.Email;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;


public class SMTPClient {
    private static final Logger LOGGER = Logger.getLogger(SMTPClient.class.getName());
    private final String smtpHost;
    private final int smtpPort;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public SMTPClient(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }


    /**
     * This function is used to connect to the SMTP server
     * @throws Exception if the connection to the SMTP server doesn't work
     */
    public void connect() throws Exception {
            socket = new Socket(smtpHost, smtpPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            Thread.sleep(200); // otherwise error INFO: SMTP Response: 421 509e6c73af57 You talk too soon, slow down.
    }

    /**
     * This function is used to read the response from the SMTP server
     * @throws Exception if the connection to the SMTP server is lost
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
     * This function is used to send a command to the SMTP server
     * @param command the command to send
     * @throws Exception if the command is not recognized by the SMTP server
     */
    private void sendCommand(String command) throws Exception {
        writer.write(command + "\r\n");
        writer.flush();
        readResponse();
    }

    /**
     * This function is used to email a group of recipients
     * @param from the sender
     * @param recipients the list of recipients
     * @param subject the subject of the email
     * @param body the body of the email
     * @throws Exception if the email content does not respect SMTP DATA format
     */
    public void sendEmail(String from, List<String> recipients, String subject, String body) throws Exception {
        sendCommand("EHLO " + smtpHost);
        sendCommand("MAIL FROM: <" + from + ">");
        for (String recipient : recipients) {
            sendCommand("RCPT TO: <" + recipient + ">");
        }
        sendCommand("DATA");

        sendEmailContent(from, recipients, subject, body);

        sendCommand(".");
    }

    /**
     * This function is used to email a group of recipients
     * @param from the sender
     * @param recipients the list of recipients
     * @param subject the subject of the email
     * @param body  the body of the email
     * @throws IOException if the email content does not respect SMTP DATA format
     */

    private void sendEmailContent(String from, List<String> recipients, String subject, String body) throws IOException {
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("From: <").append(from).append(">\r\n");

            emailContent.append("To: ");
            for (int i = 0; i < recipients.size(); i++) {
                emailContent.append("<").append(recipients.get(i)).append(">");
                if (i != recipients.size() - 1) {
                    emailContent.append(", ");
                }
            }
            emailContent.append("\r\n");
            emailContent.append("Subject: ").append(subject).append("\r\n");
            emailContent.append("\r\n");
            emailContent.append(body).append("\r\n");
            emailContent.append("\r\n");
            String smtpDataRegex = "(?s)^From: <[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}>\\r\\nTo: (<[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}>)(, <[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}>)*\\r\\nSubject: .+\\r\\n\\r\\n.+\\r\\n\\r\\n$";

            if(Pattern.matches(smtpDataRegex, emailContent.toString())) {
                LOGGER.log(Level.INFO, "SMTP DATA: {0}", emailContent.toString());
            }
            else {
                throw new IOException("Error, email content does not respect SMTP DATA format");
            }

            writer.write(emailContent.toString());
            writer.flush();
    }
    /**
     * This function is used to email a group of recipients
     * @param e the email to send
     * @throws Exception if the email content does not respect SMTP DATA format
     */
    public void sendGroupEmail(Email e) throws Exception {
        sendEmail(e.getSender(), e.getRecipients(), e.getSubject(), e.getBody());
    }

    /**
     * This function is used to close the connection to the SMTP server
     * @throws Exception Any exception that can be thrown by the socket
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


