package dai.config;
import com.google.gson.*;
import java.io.FileReader;

import java.util.Map;
public class ReadJson {
    public Map read(String pathToJson) {

        // try catch filereader
        try(FileReader reader = new FileReader(pathToJson)){
            // read json file
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch(Exception e){
            System.out.println("Error while reading file");
        }
        return null;
    }
}
