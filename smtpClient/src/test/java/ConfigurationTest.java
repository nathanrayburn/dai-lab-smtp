import dai.config.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
public class ConfigurationTest {
    @Test
    public void testReadJsonFile() {
        try{
            Configuration configuration = new Configuration("src/main/java/dai/config/config.json");
            configuration.validate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
