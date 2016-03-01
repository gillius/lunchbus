angular.module('webapp').controller('MessagesCtrl', function($scope, mp) {
	var socket = new WebSocket("ws://localhost:8080/stomp");

	var stompClient = Stomp.over(socket);
	stompClient.debug = angular.noop();
	
	this.messages = [];
	this.date = null;

	var ctrl = this;
	
	function append(str) {
		ctrl.messages.push(str);
	}

	mp.connected.then(function() {
		append("Connected");
	});

	mp.subscribe("/topic/date", function(data) {
		ctrl.date = data.body;
	});

	mp.subscribe("/topic/hello", function(data) {
		var message = data.body;
		append("Hello topic: " + message);
	});

	mp.subscribe("/user/queue/hello", function(data) {
		var message = data.body;
		append("Hello user queue: " + message);
	});

	mp.subscribe("/queue/hello", function(data) {
		var message = data.body;
		append("Hello queue: " + message);
	});

	mp.send("/app/hello", "");
});