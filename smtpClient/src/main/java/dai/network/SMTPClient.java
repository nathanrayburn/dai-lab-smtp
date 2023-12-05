package dai.network;

import dai.model.Email;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Classe client pour interagir avec un serveur SMTP.
 * Permet d'envoyer des e-mails via SMTP.
 */
public class SMTPClient {
    private static final Logger LOGGER = Logger.getLogger(SMTPClient.class.getName());
    private static final String CONTENT_TYPE = "Content-Transfer-Encoding: base64";
    private final String smtpHost;
    private final int smtpPort;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public SMTPClient(String smtpHost, int smtpPort) {
        if (smtpHost == null || smtpHost.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse du serveur SMTP ne peut pas être null ou vide.");
        }
        if (smtpPort <= 0) {
            throw new IllegalArgumentException("Le port doit être un nombre positif.");
        }
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }


    /**
     * Établit une connexion avec le serveur SMTP.
     *
     * @throws IOException Si une erreur de réseau se produit.
     */
    public void connect() throws IOException, InterruptedException {
        try {
            socket = new Socket(smtpHost, smtpPort);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            Thread.sleep(300);
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Hôte SMTP inconnu: {0}", smtpHost);
            throw e;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur de connexion au serveur SMTP: {0}", smtpHost);
            throw e;
        } catch(InterruptedException e){
            LOGGER.log(Level.SEVERE, "Erreur d'interruption de thread");
            throw e;
        }
    }

    /**
     * Lit la réponse du serveur SMTP.
     *
     * @throws IOException Si une erreur survient lors de la lecture de la réponse.
     */
    private void readResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = reader.readLine();
            if (line == null) {
                throw new IOException("La connexion au serveur SMTP a été perdue.");
            }
            sb.append(line).append("\n");
        } while (!line.matches("^(220|250|354|221) .*") && !line.matches("^([45])[0-9]{2} .*"));

        LOGGER.log(Level.INFO, "SMTP Response: {0}", sb.toString());
    }


    /**
     * Envoie une commande au serveur SMTP.
     *
     * @param command La commande à envoyer.
     * @throws IOException Si une erreur survient lors de l'envoi de la commande.
     */
    private void sendCommand(String command) throws IOException {
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("La commande ne peut pas être null ou vide.");
        }
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
     */
    public String prepareContent(String from, List<String> recipients, String subject, String body){
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
     * @throws IOException Si une erreur survient lors de l'envoi de l'e-mail.
     */
    public void sendGroupEmail(Email e) throws IOException {


        sendEmail(e.getSender(), e.getRecipients(), e.getSubject(), e.getBody());
    }

    /**
     * Envoie un e-mail.
     *
     * @param from L'expéditeur de l'e-mail.
     * @param recipients Les destinataires de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param body Le corps de l'e-mail.
     * @throws IOException Si une erreur survient lors de l'envoi de l'e-mail.
     */
    public void sendEmail(String from, List<String> recipients, String subject, String body) throws IOException {
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
     * Ferme la connexion au serveur SMTP et libère les ressources.
     */
    public void close(){
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture du socket", e);
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture du writer", e);
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture du reader", e);
            }
        }
    }
}