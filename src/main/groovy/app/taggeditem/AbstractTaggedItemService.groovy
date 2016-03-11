package app.taggeditem

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate

/**
 * Common functionality to services working with {@link TaggedItem}s.
 */
abstract class AbstractTaggedItemService<T extends TaggedItem> {
	protected final String topic
	protected final TaggedItemStore<T> itemStore

	AbstractTaggedItemService(String topic, TaggedItemStore<T> itemStore) {
		this.topic = topic
		this.itemStore = itemStore
	}

	@Autowired
	SimpMessagingTemplate mt

	void addItem(String name) {
		itemStore.addItem(name)
		mt.convertAndSend(topic, itemStore.items)
	}

	void addTag(String name, String tag) {
		itemStore.addTag(name, tag)
		mt.convertAndSend(topic, itemStore.items)
	}

	void setActive(String name, boolean active) {
		itemStore.setActive(name, active)
		mt.convertAndSend(topic, itemStore.items)
	}
}
