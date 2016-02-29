package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IOU {
	private List<IOUEntry> entries = [
	    new IOUEntry(creditor: 'Jason', debtor: 'Bob', amount: 5)
	]

	@Autowired
	SimpMessagingTemplate mt

	void increment(String creditor, String debtor, int amount) {
		//TODO: thread-safe
		def entry = entries.find { it.creditor == creditor && it.debtor == debtor }
		if (!entry) {
			entry = new IOUEntry(creditor: creditor, debtor: debtor)
			entries.add(entry)
		}
		entry.amount += amount

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
		entries
	}
}
