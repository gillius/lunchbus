package app.iou

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IOU {
	@Autowired IOUStore store

	@Autowired
	SimpMessagingTemplate mt

	void increment(String creditor, String debtor, int amount) {
		store.increment(creditor, debtor, amount)
		mt.convertAndSend("/topic/iou", entries)
	}

	@MessageMapping("/iou/increment")
	void incrementByMessage(Map<String, Object> message) {
		increment(message.creditor as String, message.debtor as String, message.amount as int)
	}

	@RequestMapping("/iou/current")
	@MessageMapping("/iou/getCurrent")
	@SendToUser("/queue/iou")
	List<IOUEntry> getEntries() {
		store.entries
	}
}
