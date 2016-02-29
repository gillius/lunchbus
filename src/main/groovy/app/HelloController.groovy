package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
public class HelloController {
	@Autowired
	SimpMessagingTemplate mt

	@RequestMapping("/hello")
	@MessageMapping("/hello")
	@SendToUser("/queue/hello")
	String index() {
		mt.convertAndSend("/topic/hello", "Someone said hello!")
		"Greetings from Spring Boot!"
	}
}
