package app

import app.iou.IOUStore
import app.iou.LocalIOUStore
import app.iou.RedisIOUStore
import app.taggeditem.LocalTaggedItemStore
import app.taggeditem.RedisTaggedItemStore
import app.taggeditem.TaggedItemStore
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

@Configuration
@ConfigurationProperties(prefix = "app")
class StoreConfig {
	boolean useRedis

	@Bean
	IOUStore iouStore() {
		useRedis ? new RedisIOUStore() : new LocalIOUStore()
	}

	@Bean
	TaggedItemStore<Person> peopleStore() {
		if (useRedis) {
			return RedisTaggedItemStore.of(Person, "person:")
		} else {
			def ret = LocalTaggedItemStore.of(Person)

			ret.addItem("Jason")
			ret.addItem("Bob")

			return ret
		}
	}

	@Lazy @Bean
	JedisPool jedisPool() {
		new JedisPool(new JedisPoolConfig(), "localhost")
	}
}
