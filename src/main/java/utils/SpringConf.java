package utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Configuration
@ComponentScan(basePackageClasses = {},
        basePackages = {"pages", "utils"}, lazyInit = true)
@PropertySource({"classpath:mainSettings.properties", "classpath:firstPage.properties"
        , "classpath:secondPage.properties"})
public class SpringConf {

    @Autowired
    private Environment environment;

    private static SpringConf instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }

    @Bean
    public Logger logger(){
        try {
            LogManager.getLogManager()
                    .readConfiguration(Files.newInputStream(Paths.get(Objects.requireNonNull
                            (environment.getProperty("logger.path")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Logger.getLogger("autotests");
    }

    public static <T> T getBean(Class<T> aClass) {
        return instance.applicationContext.getBean(aClass);
    }
}
