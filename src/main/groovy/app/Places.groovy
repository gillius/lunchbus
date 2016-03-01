package app

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
		def place = places.find { it.name == name }
		if (place) {
			place.tags << tag
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

	@RequestMapping("/places/current")
	@MessageMapping("/places/getCurrent")
	@SendToUser("/queue/places")
	Collection<Place> getPlaces() {
		places
	}
}
