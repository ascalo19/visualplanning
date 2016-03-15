package ascalo19.visualplanning;

public class Resource {

	private String id;
	private String title;
	private String eventColor;

	public Resource(String id, String title, String eventColor) {
		this.id = id;
		this.title = title;
		this.eventColor = eventColor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEventColor() {
		return eventColor;
	}

	public void setEventColor(String eventColor) {
		this.eventColor = eventColor;
	}
}
