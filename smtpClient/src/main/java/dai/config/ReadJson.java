package dai.config;
import com.google.gson.*;
import java.io.FileReader;
import dai.model.Email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
public class ReadJson {
    public Map<String, Object> read(String pathToJson) {

        // try catch filereader
        try(FileReader reader = new FileReader(pathToJson)){
            // read json file
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(reader, Map.class);
            return map;
        }catch(Exception e){
            System.out.println("Error while reading file");
        }
        return null;
    }
}
