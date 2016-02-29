package app

import groovy.transform.Canonical

@Canonical
class IOUEntry {
	String creditor
	String debtor
	int amount
}
