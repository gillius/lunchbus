package app.chat

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Chat {
	@Autowired
	private ChatStore store

	@Autowired
	SimpMessagingTemplate mt

	@RequestMapping("/chat")
	@MessageMapping("/chat/getCurrent")
	@SendToUser("/queue/chat")
	List<ChatMessage> getMessages() {
		store.messages
	}

	@MessageMapping("/chat/add")
	void addMessage(ChatMessage message) {
		message.date = new Date()
		store.addMessage(message)
		mt.convertAndSend("/topic/chat", messages)
	}
}
