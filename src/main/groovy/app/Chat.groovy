package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Chat {
	private final ArrayDeque<ChatMessage> messages = new ArrayDeque<>(20)

	@Autowired
	SimpMessagingTemplate mt

	@RequestMapping("/chat")
	@MessageMapping("/chat/getCurrent")
	@SendToUser("/queue/chat")
	Collection<ChatMessage> getMessages() {
		messages
	}

	@MessageMapping("/chat/add")
	void addMessage(ChatMessage message) {
		if (messages.size() >= 20)
			messages.removeFirst()
		message.date = new Date()
		messages.addLast(message)
		mt.convertAndSend("/topic/chat", messages)
	}
}
