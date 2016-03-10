package app.iou

interface IOUStore {
	void increment(String creditor, String debtor, int amount)

	List<IOUEntry> getEntries()
}