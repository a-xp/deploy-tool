package ru.shoppinglive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.shoppinglive.commons.core.EnvCheck;

/**
 * Created by rkhabibullin on 30.06.2017.
 */
@SpringBootApplication
@EnableCaching
@EnableWebMvc
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class Application {
    public static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args){
        new SpringApplicationBuilder(Application.class).bannerMode(Banner.Mode.OFF).profiles(EnvCheck.getProfile()).run(args);
    }
}
