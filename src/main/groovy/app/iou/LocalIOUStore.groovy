package app.iou

import org.springframework.stereotype.Service

@Service
class LocalIOUStore implements IOUStore {
	final List<IOUEntry> entries = [
			new IOUEntry(creditor: 'Jason', debtor: 'Bob', amount: 5)
	]

	@Override
	void increment(String creditor, String debtor, int amount) {
		//TODO: thread-safe
		def entry = entries.find { it.creditor == creditor && it.debtor == debtor }
		if (!entry) {
			entry = new IOUEntry(creditor: creditor, debtor: debtor)
			entries.add(entry)
		}
		entry.amount += amount
	}
}
