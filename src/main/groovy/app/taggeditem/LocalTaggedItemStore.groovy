package app.taggeditem

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

class LocalTaggedItemStore<T extends TaggedItem> implements TaggedItemStore<T> {
	private final Class<T> itemType
	private TreeSet<T> items = new TreeSet<>()

	static <T extends TaggedItem> LocalTaggedItemStore<T> of(Class<T> itemType) {
		new LocalTaggedItemStore(itemType)
	}

	LocalTaggedItemStore(Class<T> itemType) {
		this.itemType = itemType
	}

	@Override
	synchronized void addItem(String name) {
		def item = itemType.newInstance()
		item.name = name
		items << item
	}

	@Override
	void addTag(String name, String tag) {
		update(name) {
			it.tags << tag
		}
	}

	@Override
	void setActive(String name, boolean active) {
		update(name) {
			it.active = active
		}
	}

	@Override
	synchronized List<T> getItems() {
		new ArrayList<T>(items)
	}

	private synchronized void update(String name, @ClosureParams(value = SimpleType, options = "app.taggeditem.TaggedItem") Closure<?> updater) {
		def item = items.find { it.name == name }
		if (item) {
			updater(item)
		}
	}
}
