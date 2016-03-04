package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RestController

import java.util.concurrent.ArrayBlockingQueue

/**
 * An example of a log generator that generates a high level of logging volume to demonstrate the (unrelated to the
 * lunchbus) log window functionality.
 */
@RestController
class LogGenerator {
	@Autowired
	SimpMessagingTemplate mt

	private boolean started = false;

	private ArrayBlockingQueue<String> logQueue = new ArrayBlockingQueue<>(100)

	@MessageMapping("/logging/start")
	public synchronized void start() {
		if (!started) {
			Thread.start {
				Random r = new Random()

				while (true) {
					sleep r.nextInt(200)
					logQueue.offer("Random log message here " + Integer.toHexString(r.nextInt()))
				}
			}
			started = true
		}
	}

	@Scheduled(fixedRate = 250L) //TODO is there a way to start/stop a @Scheduled?
	public void sendLogMessages() {
		List<String> items = []
		def num = logQueue.drainTo(items)
		if (num) {
			String content = items.join('\n') + '\n'
			def message = MessageBuilder.withPayload(content.getBytes("UTF-8")).build()
			mt.send("/topic/logging", message)
		}
	}
}
