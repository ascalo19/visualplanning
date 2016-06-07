package ascalo19.visualplanning;

import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static final String PID_FILE_NAME = "visualplanning.pid";

    public static void main(String[] args) {
        if (!ApplicationUtils.isRunning()) {
            new SpringApplicationBuilder(Application.class).headless(false).listeners(new ApplicationPidFileWriter(PID_FILE_NAME)).run(args);
        }
        ApplicationUtils.showUi();
    }

    @Bean
    public ApplicationUtils applicationUtils() {
        return new ApplicationUtils();
    }
}
