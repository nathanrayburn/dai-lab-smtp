import dai.config.*;
import org.junit.Test;

public class ConfigurationTest {
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
