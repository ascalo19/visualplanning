package ascalo19.visualplanning;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {

	private String id;
	private String resourceId;
	private String start;
	private String end;
	private String title;
	private String color;
	private String tooltip;
}
