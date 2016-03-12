package app

import app.taggeditem.AbstractTaggedItemService
import app.taggeditem.TaggedItemStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Places extends AbstractTaggedItemService<Place> {
	@Autowired
	Places(TaggedItemStore<Place> itemStore) {
		super("/topic/places", itemStore)
	}

	@MessageMapping("/places/add")
	void addPlaceByMessage(Map<String, Object> message) {
		addItem(message.name as String)
	}

	@MessageMapping("/places/addTag")
	void addPlaceTagByMessage(Map<String, Object> message) {
		addTag(message.name as String, message.tag as String)
	}

	@MessageMapping("/places/removeTag")
	void removePlaceTagByMessage(Map<String, Object> message) {
		removeTag(message.name as String, message.tag as String)
	}

	@MessageMapping("/places/setActive")
	void setPlaceActiveByMessage(Map<String, Object> message) {
		setActive(message.name as String, message.active as boolean)
	}

	@RequestMapping("/places/current")
	@MessageMapping("/places/getCurrent")
	@SendToUser("/queue/places")
	List<Place> getPlaces() {
		itemStore.items
	}
}
