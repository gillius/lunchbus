package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * TimeKeeper serves as an example that the server can originate changes to the model of the application.
 */
@Service
class DateSender {
	@Autowired
	SimpMessagingTemplate template

	@Scheduled(fixedRate = 1000L)
	public void trigger() {
		// sends the message to /topic/date
		template.convertAndSend("/topic/date", "Date: " + new Date());
	}
}
