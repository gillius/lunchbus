package app.iou

import groovy.transform.Canonical

@Canonical
class IOUEntry {
	String creditor
	String debtor
	int amount
}
