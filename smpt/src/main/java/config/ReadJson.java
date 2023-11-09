package config;
import org.json.simple.*;
import org.json.simple.*;
import java.io.FileReader;

public class ReadJson {
    public Object read(String pathToJson){
        try(FileReader reader = new FileReader(pathToJson)){
        Object obj = jsonParser.parse(reader);
        }catch(Exception e){
            System.out.println("Error while reading file");
        }
    }
}
