package config;
import model.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe pour gérer la configuration de l'application depuis un fichier JSON.
 */
public class Configuration {
    private List<String> victims;
    private List<Email> messages;
    private String smtpHost;
    private int smtpPort;
    private int numberOfGroups;

    /**
     * Constructeur de la classe Configuration.
     *
     * @param jsonFilePath Chemin vers le fichier JSON de configuration.
     * @throws Exception En cas d'erreur de lecture du fichier ou de parsing JSON.
     */
    public Configuration(String jsonFilePath) throws Exception {
        // Lecture du contenu du fichier JSON
        String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject json = new JSONObject(content);
    }

}

