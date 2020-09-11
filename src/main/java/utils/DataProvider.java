package utils;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

@Component
public class DataProvider {

    private final Environment properties;

    private final Logger logger;

    private String property;

    private JSONArray jsonArray;

    private final String resources = "src\\main\\resources";

    @Autowired
    public DataProvider(Environment properties, Logger logger) {
        this.properties = properties;
        this.logger = logger;
    }

    @PostConstruct
    public void getDataProvider() throws IOException {
        StringBuilder stringBuilder = new StringBuilder("[");

        Files.walkFileTree(Paths.get(resources), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                logger.info("preVisitDirectory: " + dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                logger.info("visitFile: " + file);
                if (file.getFileName().toString().endsWith(".json")) {
                    stringBuilder.append(new String(Files.readAllBytes(file.toAbsolutePath()), StandardCharsets.UTF_8)).append(",");
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                logger.info("visitFileFailed: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        //logger.info(stringBuilder.toString());
        jsonArray = new JSONArray(stringBuilder.toString());
    }

    private String getString(String key){
        String result = null;
        for (int i = 0; i < jsonArray.length(); i++){
            if (jsonArray.getJSONObject(i).has(key)) {
                //logger.info("getString: " + key + " - " + jsonArray.getJSONObject(i).getString(key));
                result = jsonArray.getJSONObject(i).getString(key);
            }
        }
        return result;
    }

    private boolean has(String key){
        boolean result = false;
        for (int i = 0; i < jsonArray.length(); i++){
            if (jsonArray.getJSONObject(i).has(key)) result =  true;
        }
        return result;
    }

    public String getProperty(String key){
        if (!key.equals("") && (property = System.getProperty(key)) != null) return property;
        else if ((property = getString(key)) != null) { return property; }
        else if ((property = properties.getProperty(key)) != null) return property;
        else return null;
    }

    public boolean containsProperty(String key){
        if (System.getProperty(key) != null) return true;
        else if (has(key)) return true;
        else return properties.containsProperty(key);
    }
}