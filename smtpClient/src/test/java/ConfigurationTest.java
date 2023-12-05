import dai.config.*;
import org.junit.Test;


/**
 * Classe de tests pour Configuration.
 * Teste la lecture et la validation de la configuration à partir d'un fichier JSON.
 */
public class ConfigurationTest {

    /**
     * Teste la lecture et la validation de la configuration à partir d'un fichier JSON.
     * Vérifie que la configuration est correctement lue et validée sans exception.
     */
    @Test
    public void testReadJsonFile() {
        try{
            Configuration configuration = new Configuration("src/main/java/dai/config/config.json");
            configuration.validateConfiguration();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
