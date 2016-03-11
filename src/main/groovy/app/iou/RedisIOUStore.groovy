package app.iou

import org.springframework.beans.factory.annotation.Autowired
import redis.clients.jedis.JedisPool

class RedisIOUStore implements IOUStore {
	@Autowired
	JedisPool pool

	@Override
	void increment(String creditor, String debtor, int amount) {
		pool.resource.withCloseable { jedis ->
			jedis.hincrBy("iou", "$creditor,$debtor", amount)
		}
	}

	@Override
	List<IOUEntry> getEntries() {
		pool.resource.withCloseable { jedis ->
			jedis.hgetAll("iou")
		}.collect { entry ->
			def (creditor, debtor) = entry.key.split(",", 2)
			new IOUEntry(creditor: creditor, debtor: debtor, amount: entry.value as int)
		}
	}
}
