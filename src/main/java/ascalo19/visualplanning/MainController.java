package ascalo19.visualplanning;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MainController {

	@Autowired
	private ApplicationConfig config;
	private Date dayStart;
	private Date dayMiddle;
	private Date dayEnd;
	private Date morningLimit;
	private Date afternoonLimit;

	@PostConstruct
	public void init() {
		dayStart = ApplicationUtils.parseTime(config.getDayStartTime());
		dayMiddle = ApplicationUtils.parseTime(config.getDayMiddleTime());
		dayEnd = ApplicationUtils.parseTime(config.getDayEndTime());
		morningLimit = ApplicationUtils.parseTime(config.getMorningLimitTime());
		afternoonLimit = ApplicationUtils.parseTime(config.getAfternoonLimitTime());
	}

	@RequestMapping("/rest/events.json")
	public List<Event> findEvents(@RequestParam String start, @RequestParam String end) throws Exception {
		List<Event> result = new ArrayList<>();

		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(config.getDatasource().getUsername(), config.getDatasource().getPassword());
		service.setCredentials(credentials);
		service.setUrl(new URI(config.getDatasource().getUrl()));

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date startDate = formatter.parse(start + "T00:00:00");
		Date endDate = formatter.parse(end + "T00:00:00");

		int id = 1;
		for (Resource resource : findResources()) {
			try {
				FolderId folderId = new FolderId(WellKnownFolderName.Calendar, Mailbox.getMailboxFromString(resource.getTitle()));
				CalendarFolder cf = CalendarFolder.bind(service, folderId);
				FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startDate, endDate));
				for (Appointment a : findResults.getItems()) {
					for (ApplicationConfig.Category category : config.getCategories()) {
						if (a.getCategories().contains(category.getName())) {
							Date eventStart;
							Date eventEnd;
							if (BooleanUtils.isTrue(a.getIsAllDayEvent())) {
								eventStart = ApplicationUtils.setTime(a.getStart(), dayStart);
								eventEnd = ApplicationUtils.setTime(a.getStart(), dayEnd);
							} else {
								eventStart = adjustStart(a.getStart());
								eventEnd = adjustEnd(a.getEnd());
							}
							result.add(new Event(String.valueOf(id++), resource.getId(), formatter.format(eventStart), formatter.format(eventEnd), "", category.getColor(), a.getSubject() + "<br/>" + ApplicationUtils.timeFormat.format(a.getStart()) + " - " + ApplicationUtils.timeFormat.format(a.getEnd()) + "<br/>(" + category.getName() + ")"));
							break;
						}
					}
				}
			} catch (Exception e) {
				System.err.println("Error finding appointments for " + resource.getTitle() + " : " + e.getMessage());
			}
		}

		return result;
	}

	private Date adjustStart(Date start) {
		if (ApplicationUtils.timeOnly(start).before(morningLimit)) {
			return ApplicationUtils.setTime(start, dayStart);
		} else {
			return ApplicationUtils.setTime(start, dayMiddle);
		}
	}

	private Date adjustEnd(Date end) {
		if (ApplicationUtils.timeOnly(end).before(afternoonLimit)) {
			return ApplicationUtils.setTime(end, dayMiddle);
		} else {
			return ApplicationUtils.setTime(end, dayEnd);
		}
	}

	@RequestMapping("/rest/resources.json")
	public List<Resource> findResources() throws Exception {
		List<Resource> result = new ArrayList<>();
		for (int i = 0; i < config.getResources().size(); i++) {
			result.add(new Resource(String.valueOf(i + 1), config.getResources().get(i)));
		}
		return result;
	}
}
