package app.taggeditem

import org.springframework.beans.factory.annotation.Autowired
import redis.clients.jedis.JedisPool

class RedisTaggedItemStore<T extends TaggedItem> implements TaggedItemStore<T> {
	/**
	 * An unlikely value for a tag that is used to set active state of the tagged item.
	 */
	private static final String ACTIVE_TAG = "___active"

	private final Class<T> itemType
	private final String keyPrefix

	@Autowired
	JedisPool pool

	static <T extends TaggedItem> RedisTaggedItemStore<T> of(Class<T> itemType, String keyPrefix) {
		new RedisTaggedItemStore(itemType, keyPrefix)
	}

	RedisTaggedItemStore(Class<T> itemType, String keyPrefix) {
		this.itemType = itemType
		this.keyPrefix = keyPrefix
	}

	@Override
	void addItem(String name) {
		pool.resource.withCloseable { jedis ->
			//The set has to have at least one member to exist, so use empty string (which is not a valid tag)
			jedis.sadd(keyPrefix + name, "")
		}
	}

	@Override
	void addTag(String name, String tag) {
		pool.resource.withCloseable { jedis ->
			jedis.sadd(keyPrefix + name, tag)
		}
	}

	void removeTag(String name, String tag) {
		pool.resource.withCloseable { jedis ->
			jedis.srem(keyPrefix + name, tag)
		}
	}

	@Override
	void setActive(String name, boolean active) {
		if (active) {
			addTag(name, ACTIVE_TAG)
		} else {
			removeTag(name, ACTIVE_TAG)
		}
	}

	@Override
	List<T> getItems() {
		pool.resource.withCloseable { jedis ->
			//keys command is a horrible approach to use here but this is a play project and it's super easy to write, and
			//we don't expect data size to be big enough to care.
			def keys = jedis.keys("$keyPrefix*")

			keys.collect {
				//TODO: use pipeline here to improve performance loading each item
				def tags = jedis.smembers(it)
				def item = itemType.newInstance()
				item.name = it.substring(keyPrefix.size()) //i.e. "person:abc" to "abc"
				item.active = ACTIVE_TAG in tags
				item.tags = tags.findAll { it && it != ACTIVE_TAG } //exclude empty string and ACTIVE_TAG
				item
			}
		}.sort {
			//Since keys is not consistent, sort the return results by name
			it.name
		}
	}
}
