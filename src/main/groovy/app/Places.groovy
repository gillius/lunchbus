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
class Places {
	private TreeSet<Place> places = new TreeSet<>([
	    new Place(name: "Sakura Garden")
	])

	@Autowired
	SimpMessagingTemplate mt

	void addPlace(String name) {
		places << new Place(name: name)
		mt.convertAndSend("/topic/places", places)
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
		def place = places.find { it.name == name }
		if (place) {
			updater(place)
		}
		mt.convertAndSend("/topic/places", places)
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
	void setPersonActiveByMessage(Map<String, Object> message) {
		setActive(message.name as String, message.active as boolean)
	}

	@RequestMapping("/places/current")
	@MessageMapping("/places/getCurrent")
	@SendToUser("/queue/places")
	Collection<Place> getPlaces() {
		places
	}
}
