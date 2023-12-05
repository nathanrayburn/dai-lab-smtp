package dai.config;
import com.google.gson.*;
import java.io.FileReader;

import java.util.Map;

/**
 * Classe pour lire et analyser un fichier JSON.
 * Utilise la bibliothèque Gson pour convertir le contenu JSON en une Map.
 */
public class ReadJson {

    /**
     * Lit et analyse le contenu d'un fichier JSON.
     * Utilise un FileReader pour lire le fichier et Gson pour le convertir en Map.
     *
     * @param pathToJson Chemin vers le fichier JSON à lire.
     * @return Map représentant les données du fichier JSON.
     *         Retourne null si une erreur se produit lors de la lecture.
     */
    public Map read(String pathToJson) {

        // Bloc try-catch pour gérer les exceptions de lecture de fichier
        try(FileReader reader = new FileReader(pathToJson, java.nio.charset.StandardCharsets.UTF_8)){
            // Lecture et conversion du fichier JSON en Map
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);

        }catch(Exception e){
            System.out.println("Error while reading file");
        }
        return null;
    }
}
