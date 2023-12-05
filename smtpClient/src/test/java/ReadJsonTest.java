import dai.config.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Classe de tests pour ReadJson.
 * Teste la lecture de fichiers JSON.
 */
public class ReadJsonTest {
    private static final String CONFIG_FILE_PATH = "src/main/java/dai/config/config.json";

    /**
     * Teste la lecture d'un fichier JSON.
     * Vérifie que les données JSON sont correctement lues et parsées.
     */
    @Test
    public void testReadJsonFile() {
        ReadJson readJson = new ReadJson();
        assertDoesNotThrow(() -> {
            Map<String, Object> jsonData = readJson.read(CONFIG_FILE_PATH);

            assertNotNull(jsonData);

            // Test pour la présence de clés spécifiques
            assertTrue(jsonData.containsKey("emails"));
            assertTrue(jsonData.containsKey("numberOfGroups"));
            assertTrue(jsonData.containsKey("messages"));

            // Test pour les types de données
            assertTrue(jsonData.get("emails") instanceof List);
            assertTrue(jsonData.get("numberOfGroups") instanceof Double);
            assertTrue(jsonData.get("messages") instanceof List);

            // Tests supplémentaires pour la validité des données
            List<String> emails = (List<String>) jsonData.get("emails");
            assertFalse(emails.isEmpty());

            List<Map<String, Object>> messages = (List<Map<String, Object>>) jsonData.get("messages");
            assertFalse(messages.isEmpty());
            messages.forEach(msg -> {
                assertTrue(msg.containsKey("subject"));
                assertTrue(msg.containsKey("body"));
            });
        });
    }
}
