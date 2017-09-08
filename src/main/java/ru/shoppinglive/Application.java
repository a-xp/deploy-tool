package ru.shoppinglive;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import ru.shoppinglive.commons.core.EnvCheck;

/**
 * Created by rkhabibullin on 30.06.2017.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        new SpringApplicationBuilder(Application.class).bannerMode(Banner.Mode.OFF).profiles(EnvCheck.getProfile()).run(args);
    }
}
