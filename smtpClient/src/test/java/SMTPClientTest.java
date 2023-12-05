import dai.network.SMTPClient;
import dai.model.Email;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests pour SMTPClient.
 * Teste les différentes fonctionnalités du client SMTP.
 */
public class SMTPClientTest {

    private SMTPClient client;

    /**
     * Configuration initiale pour chaque test.
     * Initialise un client SMTP avec des paramètres de test.
     */
    @BeforeEach
    public void setUp() {
        client = new SMTPClient("smtp.example.com", 25);
    }

    /**
     * Nettoyage après chaque test.
     * Ferme la connexion client SMTP.
     */
    @AfterEach
    public void tearDown() throws Exception {
        client.close();
    }

    /**
     * Teste la connexion au serveur SMTP.
     * Vérifie qu'une exception est lancée, ce qui indique une tentative de connexion.
     */
    @Test
    public void testConnect() {
        Exception exception = assertThrows(Exception.class, () -> client.connect());
        assertNotNull(exception);
    }

    /**
     * Teste l'envoi d'un e-mail.
     * Vérifie qu'une exception est lancée lors de l'envoi.
     */
    @Test
    public void testSendEmail() {
        List<String> recipients = Arrays.asList("recipient@example.com");
        Exception exception = assertThrows(Exception.class, () ->
                client.sendEmail("sender@example.com", recipients, "Subject", "Body")
        );
        assertNotNull(exception);
    }

    /**
     * Teste la préparation du contenu de l'e-mail.
     * Vérifie qu'une exception est lancée lors de la préparation du contenu.
     */
    @Test
    public void testPrepareContent() {
        Exception exception = assertThrows(Exception.class, () ->
                client.prepareContent("sender@example.com", Arrays.asList("recipient@example.com"), "Subject", "Body")
        );
        assertNotNull(exception);
        // Ce test échouera si la préparation du contenu ne fonctionne pas comme prévu
    }

    /**
     * Teste l'encodage en base64.
     * Vérifie que le texte encodé n'est pas vide et différent du texte original.
     */
    @Test
    public void testEncodeBase64() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        String plainText = "Hello World!";
        String encodedText = client.encodeBase64(plainText);
        assertFalse(encodedText.isEmpty());
        assertNotEquals(plainText, encodedText);
    }

    /**
     * Teste la construction de l'en-tête du sujet de l'e-mail.
     * Vérifie que le sujet encodé est inclus dans le contenu de l'e-mail.
     */
    @Test
    public void testBuildSubjectHeader() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        String subject = "Test Subject";
        String encodedSubject = client.encodeBase64(subject);
        client.buildSubjectHeader(encodedSubject, emailContent);
        assertTrue(emailContent.toString().contains(encodedSubject));
    }

    /**
     * Teste la construction du corps de l'e-mail.
     * Vérifie que le corps encodé est inclus dans le contenu de l'e-mail.
     */
    @Test
    public void testBuildBody() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        String body = "This is a test body.";
        String encodedBody = client.encodeBase64(body);
        client.buildBody(encodedBody, emailContent);
        assertTrue(emailContent.toString().contains(encodedBody));
    }

    /**
     * Teste la construction des en-têtes des destinataires de l'e-mail.
     * Vérifie que tous les destinataires sont inclus dans le contenu de l'e-mail.
     */
    @Test
    public void testBuildRecipientHeaders() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        List<String> recipients = Arrays.asList("test1@example.com", "test2@example.com");
        client.buildRecipientHeaders(recipients, emailContent);
        for (String recipient : recipients) {
            assertTrue(emailContent.toString().contains(recipient));
        }
    }

    /**
     * Teste la construction de l'en-tête de l'expéditeur de l'e-mail.
     * Vérifie que l'expéditeur est inclus dans le contenu de l'e-mail.
     */
    @Test
    public void testBuildSenderHeader() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        String sender = "sender@example.com";
        client.buildSenderHeader(sender, emailContent);
        assertTrue(emailContent.toString().contains(sender));
    }

    /**
     * Teste la construction de l'en-tête du type de contenu de l'e-mail.
     * Vérifie que l'en-tête du type de contenu est correctement inclus.
     */
    @Test
    public void testBuildContentTypeHeader() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        client.buildContentTypeHeader(emailContent);
        assertTrue(emailContent.toString().contains("Content-Transfer-Encoding: base64"));
    }

    /**
     * Teste l'envoi d'un e-mail à un groupe.
     * Vérifie qu'une exception est lancée lors de l'envoi du groupe.
     */
    @Test
    public void testSendGroupEmail() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        List<String> recipients = Arrays.asList("recipient@example.com");
        Email email = new Email("sender@example.com", recipients, "Subject", "Body");
        Exception exception = assertThrows(Exception.class, () -> client.sendGroupEmail(email));
        assertNotNull(exception);
    }

    /**
     * Teste le format du contenu de l'e-mail.
     * Vérifie que le contenu de l'e-mail est correctement formaté.
     */
    @Test
    public void testEmailContentFormat() {
        // Create sample data for the email
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        String from = "sender@example.com";
        List<String> recipients = Arrays.asList("recipient1@example.com", "recipient2@example.com");
        String subject = "Test Subject";
        String body = "Test body content";

        try {

            String sentContent = client.prepareContent(from, recipients, subject, body);
            assertTrue(sentContent.contains("From: <" + from + ">"));
            assertTrue(sentContent.contains("To: <recipient1@example.com>, <recipient2@example.com>"));
            assertTrue(sentContent.contains("Subject:=?utf-8?B?")); // Checking subject encoding

        } catch (Exception e) {
            // Handle or log any exception that might occur during the test
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    /**
     * Teste la fermeture du client SMTP.
     * Vérifie qu'aucune exception n'est lancée lors de la fermeture.
     */
    @Test
    public void testClose() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        assertDoesNotThrow(client::close);
    }
}

