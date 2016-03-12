package app.taggeditem

interface TaggedItemStore<T extends TaggedItem> {
	void addItem(String name)
	void addTag(String name, String tag)
	void removeTag(String name, String tag)
	void setActive(String name, boolean active)

	List<T> getItems()
}