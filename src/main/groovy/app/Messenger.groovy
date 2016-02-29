package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class Messenger {
	@Autowired
	SimpMessagingTemplate template

	@Scheduled(fixedRate = 5000L)
	public void trigger() {
		// sends the message to /topic/message
		template.convertAndSend("/topic/message", "Date: " + new Date());
	}
}
