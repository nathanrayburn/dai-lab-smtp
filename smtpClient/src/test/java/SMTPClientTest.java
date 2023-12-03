import dai.network.SMTPClient;
import dai.model.Email;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SMTPClientTest {

    private SMTPClient client;

    @BeforeEach
    public void setUp() {
        client = new SMTPClient("smtp.example.com", 25);
    }

    @AfterEach
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testConnect() {
        Exception exception = assertThrows(Exception.class, () -> client.connect());
        assertNotNull(exception);
    }

    @Test
    public void testSendEmail() {
        List<String> recipients = Arrays.asList("recipient@example.com");
        Exception exception = assertThrows(Exception.class, () ->
                client.sendEmail("sender@example.com", recipients, "Subject", "Body")
        );
        assertNotNull(exception);
    }

    @Test
    public void testPrepareContent() {
        Exception exception = assertThrows(Exception.class, () ->
                client.prepareContent("sender@example.com", Arrays.asList("recipient@example.com"), "Subject", "Body")
        );
        assertNotNull(exception);
        // Ce test échouera si la préparation du contenu ne fonctionne pas comme prévu
    }


    @Test
    public void testEncodeBase64() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        String plainText = "Hello World!";
        String encodedText = client.encodeBase64(plainText);
        assertFalse(encodedText.isEmpty());
        assertNotEquals(plainText, encodedText);
    }

    @Test
    public void testBuildSubjectHeader() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        String subject = "Test Subject";
        String encodedSubject = client.encodeBase64(subject);
        client.buildSubjectHeader(encodedSubject, emailContent);
        assertTrue(emailContent.toString().contains(encodedSubject));
    }

    @Test
    public void testBuildBody() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        String body = "This is a test body.";
        String encodedBody = client.encodeBase64(body);
        client.buildBody(encodedBody, emailContent);
        assertTrue(emailContent.toString().contains(encodedBody));
    }

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

    @Test
    public void testBuildSenderHeader() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        String sender = "sender@example.com";
        client.buildSenderHeader(sender, emailContent);
        assertTrue(emailContent.toString().contains(sender));
    }


    @Test
    public void testBuildContentTypeHeader() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        StringBuilder emailContent = new StringBuilder();
        client.buildContentTypeHeader(emailContent);
        assertTrue(emailContent.toString().contains("Content-Transfer-Encoding: base64"));
    }

    @Test
    public void testSendGroupEmail() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        List<String> recipients = Arrays.asList("recipient@example.com");
        Email email = new Email("sender@example.com", recipients, "Subject", "Body");
        Exception exception = assertThrows(Exception.class, () -> client.sendGroupEmail(email));
        assertNotNull(exception);
    }
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
            System.out.println(sentContent);
            assertTrue(sentContent.contains("From: <" + from + ">"));
            assertTrue(sentContent.contains("To: <recipient1@example.com>, <recipient2@example.com>"));
            assertTrue(sentContent.contains("Subject:=?utf-8?B?")); // Checking subject encoding

        } catch (Exception e) {
            // Handle or log any exception that might occur during the test
            e.printStackTrace();
            fail("Exception occurred during test: " + e.getMessage());
        }
    }
    @Test
    public void testClose() {
        SMTPClient client = new SMTPClient("smtp.example.com", 25);
        assertDoesNotThrow(client::close);
    }
}

