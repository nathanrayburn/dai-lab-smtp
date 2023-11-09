package org.example.config;
import com.google.gson.*;
import java.io.FileReader;

public class ReadJson {
    public Object read(String pathToJson){
        try(FileReader reader = new FileReader(pathToJson)){
        Gson gson = new Gson();
        Object obj = gson.fromJson(reader, Object.class);

        return obj;
        }catch(Exception e){
            System.out.println("Error while reading file");
        }
        return null;
    }
}
