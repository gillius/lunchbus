package app

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class People {
	private TreeSet<Person> people = new TreeSet<>([
	    new Person(name: "Jason", tags: ["cool"])
	])

	@Autowired
	SimpMessagingTemplate mt

	void addPerson(String name) {
		people << new Person(name: name)
		mt.convertAndSend("/topic/people", people)
	}

	void addTag(String name, String tag) {
		update(name) {
			it.tags << tag
		}
	}

	void setActive(String name, boolean active) {
		update(name) {
			it.active = active
		}
	}

	private void update(String name, @ClosureParams(value = SimpleType, options = "app.Person") Closure<?> updater) {
		def person = people.find { it.name == name }
		if (person) {
			updater(person)
		}
		mt.convertAndSend("/topic/people", people)
	}

	@MessageMapping("/people/add")
	void addPersonByMessage(Map<String, Object> message) {
		addPerson(message.name as String)
	}

	@MessageMapping("/people/addTag")
	void addPersonTagByMessage(Map<String, Object> message) {
		addTag(message.name as String, message.tag as String)
	}

	@MessageMapping("/people/setActive")
	void setPersonActiveByMessage(Map<String, Object> message) {
		setActive(message.name as String, message.active as boolean)
	}

	@RequestMapping("/people/current")
	@MessageMapping("/people/getCurrent")
	@SendToUser("/queue/people")
	Collection<Person> getPeople() {
		people
	}
}
