package app

import groovy.transform.Canonical

@Canonical
class TaggedItem implements Comparable<TaggedItem> {
	String name
	TreeSet<String> tags = new TreeSet<>()

	@Override
	int compareTo(TaggedItem o) {
		name <=> o.name
	}
}
