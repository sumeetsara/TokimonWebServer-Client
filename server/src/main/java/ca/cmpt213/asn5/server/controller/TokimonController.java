package ca.cmpt213.asn5.server.controller;

/**
 * TokimonConstroller class has the mapping from the server such as GetMapping, PostMapping and DeleteMapping
 * Reads in the .json file in the PostConstruct and writes to the .json file in the PreDestroy
 * ArratList of tokimons to store all tokimons on the server
 */

import ca.cmpt213.asn5.server.model.Tokimon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TokimonController {
    private List<Tokimon> tokimons = new ArrayList<>();

    @GetMapping("/tokimon/all")     // GET /api/tokimon/all
    public List<Tokimon> getTokimons(HttpServletResponse response) {
        System.out.println("end point of arrayList");
        response.setStatus(200);
        return tokimons;
    }

    @GetMapping("/tokimon/{id}")   // POST /api/tokimon/change/{id}
    public Tokimon getTokimon(@PathVariable long id, HttpServletResponse response) {
        System.out.println("end point of id tokimon");
        // Search for tokimon with the param tID
        for(int i = 0; i < tokimons.size(); i++) {
            if(tokimons.get(i).getTID() == id) {
                response.setStatus(200);
                return tokimons.get(i);
            }
        }
        throw new IllegalArgumentException();
    }

    @PostMapping("/tokimon/add")    // POST /api/tokimon/add
    public void addTokimon(@RequestBody Tokimon newTokimon, HttpServletResponse response) {
        response.setStatus(201);
        tokimons.add(newTokimon);
    }

    @PostMapping("/tokimon/change/{id}")        // POST /api/tokimon/change/{id}
    public void alterTokimon(@PathVariable long id,  @RequestBody Tokimon newTokimon, HttpServletResponse response) {
        for(int i = 0; i < tokimons.size(); i++) {
            if(tokimons.get(i).getTID() == id) {
                tokimons.remove(i);
                newTokimon.settID(id);
                tokimons.add(newTokimon);
                response.setStatus(201);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    @DeleteMapping("/tokimon/{id}")     // DELETE /api/tokimon/{id}
    public void deleteTokimon(@PathVariable long id,  HttpServletResponse response) {
        System.out.println("end point of delete toki");
        for(int i = 0; i < tokimons.size(); i++) {
            if(tokimons.get(i).getTID() == id) {
                tokimons.remove(i);
                response.setStatus(204);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    @PostConstruct
    public void initialize() {
        readInJson();
    }

    // https://www.baeldung.com/spring-postconstruct-predestroy
    @PreDestroy
    public void destructor() {
        writeToJson();
    }

    // https://www.youtube.com/watch?v=HSuVtkdej8Q
    private void readInJson() {
        File input = new File("data/tokimon.json");
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonArray jsonArrayOfTokimons = fileElement.getAsJsonArray();
            for(JsonElement tokimonElement : jsonArrayOfTokimons) {
                JsonObject tokimonJsonObject = tokimonElement.getAsJsonObject();

                String name = tokimonJsonObject.get("name").getAsString();
                double weight = tokimonJsonObject.get("weight").getAsDouble();
                double height = tokimonJsonObject.get("height").getAsDouble();
                String ability = tokimonJsonObject.get("ability").getAsString();
                double strength = tokimonJsonObject.get("strength").getAsDouble();
                String colour = tokimonJsonObject.get("colour").getAsString();
                long tID = tokimonJsonObject.get("tid").getAsLong();

                Tokimon newTokimon = new Tokimon(name,weight,height,ability,strength,colour);
                newTokimon.settID(tID);

                tokimons.add(newTokimon);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
        }
    }

    // https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
    private void writeToJson() {
        JsonArray jsonTokimonList = new JsonArray();
        for(Tokimon tokimon : tokimons) {
            JsonObject currentTokimon = new JsonObject();
            String name = tokimon.getName();
            Double weight = tokimon.getWeight();
            Double height = tokimon.getHeight();
            String ability = tokimon.getAbility();
            Double strength = tokimon.getStrength();
            String colour = tokimon.getColour();
            long tID = tokimon.getTID();

            currentTokimon.addProperty("name",name);
            currentTokimon.addProperty("weight",weight);
            currentTokimon.addProperty("height",height);
            currentTokimon.addProperty("ability",ability);
            currentTokimon.addProperty("strength",strength);
            currentTokimon.addProperty("colour",colour);
            currentTokimon.addProperty("tid",tID);

            jsonTokimonList.add(currentTokimon);
        }

        try(FileWriter file = new FileWriter("data/tokimon.json")) {
            file.write(jsonTokimonList.toString());
        } catch (IOException e) {
            System.err.println("File to write to not found");
            e.printStackTrace();
        }
    }

    // https://www.youtube.com/watch?v=rXBsnNCH59o
    // Exception Handler
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request ID not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExeption() { }


}
