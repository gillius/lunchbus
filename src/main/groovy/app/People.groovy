package app

import app.taggeditem.TaggedItemStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class People {
	@Autowired
	private TaggedItemStore<Person> personStore

	@Autowired
	SimpMessagingTemplate mt

	void addPerson(String name) {
		personStore.addItem(name)
		mt.convertAndSend("/topic/people", personStore.items)
	}

	void addTag(String name, String tag) {
		personStore.addTag(name, tag)
		mt.convertAndSend("/topic/people", personStore.items)
	}

	void setActive(String name, boolean active) {
		personStore.setActive(name, active)
		mt.convertAndSend("/topic/people", personStore.items)
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
	List<Person> getPeople() {
		personStore.items
	}
}
