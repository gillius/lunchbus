package app

import app.taggeditem.AbstractTaggedItemService
import app.taggeditem.TaggedItemStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class People extends AbstractTaggedItemService<Person> {
	@Autowired
	People(TaggedItemStore<Person> itemStore) {
		super("/topic/people", itemStore)
	}

	@MessageMapping("/people/add")
	void addPersonByMessage(Map<String, Object> message) {
		addItem(message.name as String)
}

	@MessageMapping("/people/addTag")
	void addPersonTagByMessage(Map<String, Object> message) {
		addTag(message.name as String, message.tag as String)
	}

	@MessageMapping("/people/removeTag")
	void removePersonTagByMessage(Map<String, Object> message) {
		removeTag(message.name as String, message.tag as String)
	}

	@MessageMapping("/people/setActive")
	void setPersonActiveByMessage(Map<String, Object> message) {
		setActive(message.name as String, message.active as boolean)
	}

	@RequestMapping("/people/current")
	@MessageMapping("/people/getCurrent")
	@SendToUser("/queue/people")
	List<Person> getPeople() {
		itemStore.items
	}
}
