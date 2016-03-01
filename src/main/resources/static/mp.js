angular.module('webapp').factory('mp', function($q, $rootScope, $timeout, host) {
	var socket = new WebSocket("ws://" + host + ":8080/stomp");

	var stompClient = Stomp.over(socket);
	stompClient.debug = angular.noop();

	var connectedDeferred = $q.defer();
	var connected = connectedDeferred.promise;

	stompClient.connect({}, function() {
		connectedDeferred.resolve();
	});

	return {
		connected: connected,

		subscribe: function(queue, callback) {
			connected.then(function() {
				stompClient.subscribe(queue, function(frame) {
					$timeout(function() {
						if (typeof frame.body === 'string' && frame.body && (frame.body.charAt(0) === '{' || frame.body.charAt(0) === '['))
							frame.body = JSON.parse(frame.body);
						callback(frame);
					})
				});
			})
		},

		send: function(destination, message) {
			if (typeof message === 'object')
				message = JSON.stringify(message);
			connected.then(function() {
				stompClient.send(destination, {}, message);
			})
		}
	}
});