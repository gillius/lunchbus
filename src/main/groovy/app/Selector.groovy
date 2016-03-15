package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service

/**
 * The Selector selects the place will you will eat your lunch. You do not question it. It takes the tags of the
 * active people and picks from active places that contain all of the tags. Then it picks the best one at random.
 */
@Controller
class Selector {
	@Autowired
	People people

	@Autowired
	Places places

	@Autowired
	SimpMessagingTemplate mt

	private final Random random = new Random()

	private volatile String selectedPlace = ""

	@MessageMapping("/selection/selectPlace")
	synchronized void selectPlace() {
		def requiredTags = new HashSet<String>()
		for (Person p : people.people) {
			if (p.active)
				requiredTags.addAll(p.tags)
		}

		def candidates = places.places.findAll {
			it.active && it.tags.containsAll(requiredTags)
		}

		if (!candidates.empty) {
			selectedPlace = candidates[random.nextInt(candidates.size())].name
		} else {
			selectedPlace = "nowhere!"
		}

		updateTopic()
	}

	@MessageMapping("/selection/clearSelectedPlace")
	synchronized void clearPlace() {
		selectedPlace = ""
		updateTopic()
	}

	@MessageMapping("/selection/getSelectedPlace")
	@SendToUser("/queue/selectedPlace")
	synchronized String getSelectedPlace() {
		selectedPlace
	}

	private void updateTopic() {
		mt.send("/topic/selectedPlace", MessageBuilder.withPayload(selectedPlace.getBytes("UTF-8")).build());
	}
}
