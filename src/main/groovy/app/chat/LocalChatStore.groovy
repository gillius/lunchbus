package app.chat

class LocalChatStore implements ChatStore {
	private final ArrayDeque<ChatMessage> messages = new ArrayDeque<>(20)

	@Override
	synchronized List<ChatMessage> getMessages() {
		new ArrayList<ChatMessage>(messages)
	}

	@Override
	synchronized void addMessage(ChatMessage message) {
		if (messages.size() >= 20)
			messages.removeFirst()
		messages.addLast(message)
	}
}
