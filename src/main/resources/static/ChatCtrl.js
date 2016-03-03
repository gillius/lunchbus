angular.module('webapp').controller('ChatCtrl', function(mp) {
	var ctrl = this;

	ctrl.messages = [];
	ctrl.name = "";
	ctrl.message = "";

	var updateChat = function(data) {
		ctrl.messages = data.body;
	};

	mp.subscribe('/topic/chat', updateChat);
	mp.subscribe('/user/queue/chat', updateChat);

	mp.send('/app/chat/getCurrent', '');

	ctrl.addMessage = function() {
		mp.send('/app/chat/add', {name: ctrl.name, message: ctrl.message});
		ctrl.message = "";
	}
});