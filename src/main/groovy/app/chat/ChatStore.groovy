package app.chat

interface ChatStore {
	List<ChatMessage> getMessages()
	void addMessage(ChatMessage message)
}
