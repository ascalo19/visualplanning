package ascalo19.visualplanning;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "vpl")
@Data
public class ApplicationConfig {

	private Datasource datasource;
	private List<Category> categories;
	private List<String> resources;
	private String dayStartTime;
	private String dayMiddleTime;
	private String dayEndTime;
	private String morningLimitTime;
	private String afternoonLimitTime;

	@Data
	public static class Datasource {

		private Type type;
		private String url;
		private String username;
		private String password;

		public enum Type {
			local, caldav, ews
		}
	}

	@Data
	public static class Category {

		private String name;
		private String color;
	}
}
