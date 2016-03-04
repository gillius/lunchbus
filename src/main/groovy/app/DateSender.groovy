package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.MessageBuilder
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
		template.send("/topic/date", MessageBuilder.withPayload(("Date: " + new Date()).getBytes("UTF-8")).build());
	}
}
