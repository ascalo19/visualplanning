package ascalo19.visualplanning;

import java.awt.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationUtils {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	public static void browseQuietly(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
			// Ignore
		}
	}

	public static Date timeOnly(Date date) {
		return parseTime(timeFormat.format(date));
	}

	public static Date parseDate(String date) {
		try {
			return dateFormat.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
			// Ignore
		}
		return null;
	}

	public static Date parseTime(String time) {
		try {
			return timeFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
			// Ignore
		}
		return null;
	}

	public static Date parseDateTime(String dateTime) {
		try {
			return dateTimeFormat.parse(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
			// Ignore
		}
		return null;
	}

	public static Date setTime(Date date, Date time) {
		return parseDateTime(dateFormat.format(date) + " " + timeFormat.format(time));
	}
}
