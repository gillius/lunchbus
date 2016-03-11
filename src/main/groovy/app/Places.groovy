package app

import app.taggeditem.TaggedItemStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Places {
	@Autowired
	private TaggedItemStore<Place> placesStore

	@Autowired
	SimpMessagingTemplate mt

	void addPlace(String name) {
		placesStore.addItem(name)
		mt.convertAndSend("/topic/places", placesStore.items)
	}

	void addTag(String name, String tag) {
		placesStore.addTag(name, tag)
		mt.convertAndSend("/topic/places", placesStore.items)
	}

	void setActive(String name, boolean active) {
		placesStore.setActive(name, active)
		mt.convertAndSend("/topic/places", placesStore.items)
	}

	@MessageMapping("/places/add")
	void addPlaceByMessage(Map<String, Object> message) {
		addPlace(message.name as String)
	}

	@MessageMapping("/places/addTag")
	void addPlaceTagByMessage(Map<String, Object> message) {
		addTag(message.name as String, message.tag as String)
	}

	@MessageMapping("/places/setActive")
	void setPlaceActiveByMessage(Map<String, Object> message) {
		setActive(message.name as String, message.active as boolean)
	}

	@RequestMapping("/places/current")
	@MessageMapping("/places/getCurrent")
	@SendToUser("/queue/places")
	List<Place> getPlaces() {
		placesStore.items
	}
}
