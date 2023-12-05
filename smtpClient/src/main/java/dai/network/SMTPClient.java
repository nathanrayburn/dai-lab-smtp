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
import java.io.OutputStreamWriter;
import java.util.Base64;

/**
 * Classe client pour interagir avec un serveur SMTP.
 * Permet d'envoyer des e-mails via SMTP.
 */
public class SMTPClient {
    private static final Logger LOGGER = Logger.getLogger(SMTPClient.class.getName());
    final String CONTENT_TYPE = "Content-Transfer-Encoding: base64";
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
     * Se connecte au serveur SMTP.
     * @throws Exception En cas d'échec de la connexion.
     */
    public void connect() throws Exception {
        socket = new Socket(smtpHost, smtpPort);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.writer = writer;
        this.reader = reader;
        Thread.sleep(200); // otherwise error INFO: SMTP Response: 421 509e6c73af57 You talk too soon, slow down.
    }

    /**
     * Lit la réponse du serveur SMTP.
     *
     * @throws Exception Si une erreur survient lors de la lecture de la réponse.
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
     * Envoie une commande au serveur SMTP.
     *
     * @param command La commande à envoyer.
     * @throws Exception Si une erreur survient lors de l'envoi de la commande.
     */
    private void sendCommand(String command) throws Exception {
        writer.write(command + "\r\n");
        writer.flush();
        readResponse();
    }



    /**
     * Prépare le contenu de l'e-mail à envoyer.
     *
     * @param from L'expéditeur de l'e-mail.
     * @param recipients Les destinataires de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param body Le corps de l'e-mail.
     * @return Le contenu de l'e-mail formaté.
     * @throws Exception Si une erreur survient lors de la préparation du contenu.
     */
    public String prepareContent(String from, List<String> recipients, String subject, String body) throws Exception {
        StringBuilder emailContent = new StringBuilder();
        final String encodedBody = encodeBase64(body);
        final String encodedSubject = encodeBase64(subject);

        buildSenderHeader(from, emailContent);
        buildRecipientHeaders(recipients, emailContent);
        buildSubjectHeader(encodedSubject, emailContent);
        buildContentTypeHeader(emailContent);
        buildBody(encodedBody, emailContent);

        return emailContent.toString();
    }

    /**
     * Construit l'en-tête "From" de l'e-mail.
     *
     * @param from L'expéditeur de l'e-mail.
     * @param emailContent Le StringBuilder pour le contenu de l'e-mail.
     */
    public void buildSenderHeader(String from, StringBuilder emailContent) {
        emailContent.append("From: <").append(from).append(">\r\n");
    }

    /**
     * Construit l'en-tête "To" de l'e-mail avec les destinataires.
     *
     * @param recipients Les destinataires de l'e-mail.
     * @param emailContent Le StringBuilder pour le contenu de l'e-mail.
     */
    public void buildRecipientHeaders(List<String> recipients, StringBuilder emailContent) {
        emailContent.append("To: ");
        for (int i = 0; i < recipients.size(); i++) {
            emailContent.append("<").append(recipients.get(i)).append(">");
            if (i != recipients.size() - 1) {
                emailContent.append(", ");
            }
        }
        emailContent.append("\r\n");
    }

    /**
     * Construit l'en-tête "Subject" de l'e-mail.
     *
     * @param encodedSubject Le sujet encodé de l'e-mail.
     * @param emailContent Le StringBuilder pour le contenu de l'e-mail.
     */
    public void buildSubjectHeader(String encodedSubject, StringBuilder emailContent) {
        emailContent.append("Subject:=?utf-8?B?").append(encodedSubject).append("?=\r\n");
    }

    /**
     * Construit l'en-tête de type de contenu de l'e-mail.
     *
     * @param emailContent Le StringBuilder pour le contenu de l'e-mail.
     */
    public void buildContentTypeHeader(StringBuilder emailContent) {
        emailContent.append(CONTENT_TYPE).append("\r\n\r\n");
    }

    /**
     * Construit le corps de l'e-mail.
     *
     * @param encodedBody Le corps encodé de l'e-mail.
     * @param emailContent Le StringBuilder pour le contenu de l'e-mail.
     */

    public void buildBody(String encodedBody, StringBuilder emailContent) {
        emailContent.append(encodedBody).append("\r\n");
        emailContent.append("\r\n");
    }

    /**
     * Encode une chaîne en base64.
     *
     * @param text La chaîne à encoder.
     * @return La chaîne encodée.
     */
    public String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Envoie un e-mail à un groupe de destinataires.
     *
     * @param e L'e-mail à envoyer.
     * @throws Exception Si une erreur survient lors de l'envoi de l'e-mail.
     */
    public void sendGroupEmail(Email e) throws Exception {
        sendEmail(e.getSender(), e.getRecipients(), e.getSubject(), e.getBody());
    }

    /**
     * Envoie un e-mail.
     *
     * @param from L'expéditeur de l'e-mail.
     * @param recipients Les destinataires de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param body Le corps de l'e-mail.
     * @throws Exception Si une erreur survient lors de l'envoi de l'e-mail.
     */
    public void sendEmail(String from, List<String> recipients, String subject, String body) throws Exception {
        sendCommand("EHLO " + smtpHost);

        sendCommand("MAIL FROM: <" + from + ">");

        for (String recipient : recipients) {
            sendCommand("RCPT TO: <" + recipient + ">");
        }

        sendCommand("DATA");

        writer.write(prepareContent(from, recipients, subject, body));

        writer.flush();

        sendCommand(".");
    }


    /**
     * Ferme la connexion au serveur SMTP et les ressources associées.
     *
     * @throws Exception Si une erreur survient lors de la fermeture des ressources.
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


