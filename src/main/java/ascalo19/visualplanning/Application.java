package ascalo19.visualplanning;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ApplicationContext ac = new SpringApplicationBuilder(Application.class).headless(false).run(args);
		String serverPort = ac.getEnvironment().getProperty("local.server.port");
		ApplicationUtils.browseQuietly("http://localhost:" + serverPort);
	}
}
