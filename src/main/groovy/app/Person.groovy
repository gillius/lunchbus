package app

import groovy.transform.Canonical

@Canonical
class Person implements Comparable<Person> {
	String name

	@Override
	int compareTo(Person o) {
		name <=> o.name
	}
}
