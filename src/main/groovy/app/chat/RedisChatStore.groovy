package app.chat

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import org.springframework.beans.factory.annotation.Autowired
import redis.clients.jedis.JedisPool

class RedisChatStore implements ChatStore {
	private static final byte[] key = "chat".getBytes("UTF-8")

	@Autowired
	JedisPool pool

	private ObjectReader reader
	private ObjectWriter writer

	@Autowired
	void setObjectMapper(ObjectMapper mapper) {
		reader = mapper.readerFor(ChatMessage)
		writer = mapper.writerFor(ChatMessage)
	}

	@Override
	List<ChatMessage> getMessages() {
		pool.resource.withCloseable { jedis ->
			jedis.lrange(key, 0, 19).collect { (ChatMessage) reader.readValue(it) }
		}
	}

	@Override
	void addMessage(ChatMessage message) {
		pool.resource.withCloseable { jedis ->
			def tx = jedis.multi()
			tx.rpush(key, [writer.writeValueAsBytes(message)] as byte[][])
			tx.ltrim(key, -20, -1)
			tx.exec()
		}
	}
}
