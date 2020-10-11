package utils;

import efr.testDataClient.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class DataProvider {

    private final Environment properties;

    private final Logger logger;

    private Yaml yaml;

    private Map<String, String> map;

    private TestClient testClient;

    private final String clientsPath = "src/main/resources/efr/clients/";

    private final String resources = "src/main/resources";

    @Autowired
    public DataProvider(Environment properties, Logger logger) {
        this.properties = properties;
        this.logger = logger;
    }

    @PostConstruct
    public void getDataProvider() throws IOException {
        yaml = new Yaml();
        map = new HashMap<>();
        Files.walkFileTree(Paths.get(resources), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                logger.info("preVisitDirectory: " + dir.toString());
                if (dir.toString().contains("clients")) return FileVisitResult.SKIP_SUBTREE;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                logger.info("visitFile: " + file);
                if (file.getFileName().toString().endsWith(".yaml")) {
                    map.putAll(yaml.load(Files.newInputStream(file)));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                logger.severe("visitFileFailed: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void loadYAML(String path) throws IOException {
        map.putAll(yaml.load(Files.newInputStream(Paths.get(path))));
        Files.walkFileTree(Paths.get(clientsPath), new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.getFileName().toString().contains(map.get("client"))) {
                            testClient = yaml.load(Files.newInputStream(file));
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
    }

    public TestClient getClient() {
        return testClient;
    }

    private String getString(String key) {
        return map.get(key);
    }

    private boolean has(String key) {
        return map.containsKey(key);
    }

    public String getProperty(String key) {
        String property;
        if (!key.equals("") && (property = System.getProperty(key)) != null) return property;
        else if ((property = getString(key)) != null) return property;
        else if ((property = properties.getProperty(key)) != null) return property;
        else return null;
    }

    public boolean containsProperty(String key) {
        if (System.getProperty(key) != null) return true;
        else if (has(key)) return true;
        else return properties.containsProperty(key);
    }
}