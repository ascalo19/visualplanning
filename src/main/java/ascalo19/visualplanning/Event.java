package ascalo19.visualplanning;

public class Event {

	private String id;
	private String resourceId;
	private String start;
	private String end;
	private String title;
	private String color;

	public Event(String id, String resourceId, String start, String end, String title, String color) {
		this.id = id;
		this.resourceId = resourceId;
		this.start = start;
		this.end = end;
		this.title = title;
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
