package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class People {
	private TreeSet<Person> people = new TreeSet<>([
	    new Person(name: "Jason")
	])

	@Autowired
	SimpMessagingTemplate mt

	void addPerson(String name) {
		people << new Person(name: name)
		mt.convertAndSend("/topic/people", people)
	}

	@MessageMapping("/people/add")
	void addPersonByMessage(Map<String, Object> message) {
		addPerson(message.name as String)
	}

	@RequestMapping("/people/current")
	@MessageMapping("/people/getCurrent")
	@SendToUser("/queue/people")
	Collection<Person> getPeople() {
		people
	}
}
