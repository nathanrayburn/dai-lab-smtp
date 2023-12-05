import dai.config.*;
import dai.model.Message;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


/**
 * Classe de tests pour Configuration.
 * Teste la lecture et la validation de la configuration à partir d'un fichier JSON.
 */
public class ConfigurationTest {
    private static final String CONFIG_FILE_PATH = "src/main/java/dai/config/config.json";

    /**
     * Teste la validité de la configuration chargée à partir d'un fichier JSON.
     * Ce test vérifie que :
     * - La liste des e-mails est non vide et chaque e-mail respecte un format valide.
     * - Les paramètres du serveur SMTP (host et port) sont valides et correctement chargés.
     * - La liste des messages n'est pas vide et chaque message a un sujet et un corps valides.
     * - Le nombre de groupes et les limites des e-mails par groupe sont logiques et positifs.
     */
    @Test
    public void testConfigurationValidity() {
        assertDoesNotThrow(() -> {
            Configuration configuration = new Configuration(CONFIG_FILE_PATH);

            // Vérifier les adresses e-mail
            List<String> emails = configuration.getVictims();
            assertFalse(emails.isEmpty());
            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
            emails.forEach(email -> assertTrue(emailPattern.matcher(email).matches()));

            // Vérifier les paramètres SMTP
            assertNotNull(configuration.getSmtpHost());
            assertTrue(configuration.getSmtpPort() > 0);

            // Vérifier les messages
            List<Message> messages = configuration.getMessages();
            assertFalse(messages.isEmpty());
            messages.forEach(message -> {
                assertNotNull(message.getSubject());
                assertNotNull(message.getBody());
            });

            // Vérifier les groupes
            assertTrue(configuration.getNumberOfGroups() > 0);
            assertTrue(configuration.getMinNumberOfEmailsPerGroup() >= 0);
            assertTrue(configuration.getMaxNumberOfEmailsPerGroup() >= configuration.getMinNumberOfEmailsPerGroup());

            configuration.validateConfiguration();
        });
    }
}
