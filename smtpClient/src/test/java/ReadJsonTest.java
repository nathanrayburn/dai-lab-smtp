import dai.config.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Classe de tests pour ReadJson.
 * Teste la lecture de fichiers JSON.
 */
public class ReadJsonTest {

    /**
     * Teste la lecture d'un fichier JSON.
     * Vérifie que les données JSON sont correctement lues et parsées.
     */
    @Test
    public void testReadJsonFile() {

        ReadJson readJson = new ReadJson();
        Map<String, Object> jsonData = readJson.read("src/main/java/dai/config/config.json");

        // Extraction et tests sur les données du fichier JSON
        List<String> victims = (List<String>) jsonData.get("emails");
        List<String> messages = new ArrayList<>();

        List<Map<String, Object>> messageList = (List<Map<String, Object>>) jsonData.get("messages");
        for (Map<String, Object> message : messageList) {
            String messageContent = (String) message.get("body");
            messages.add(messageContent);
        }
        assertNotNull(jsonData);
    }
}
