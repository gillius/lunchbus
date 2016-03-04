function LogWindow(elem, maxLength) {
	this.elem = elem;
	this.elems = elem.children;
	this.maxLength = maxLength;
}

LogWindow.prototype.addText = function(text) {
	var span = document.createElement("span");
	span.textContent = text;
	this.elem.appendChild(span);
	if (this.elems.length > this.maxLength) {
		this.elem.removeChild(this.elems.item(0));
	}
};

angular.module('webapp').directive('logWindow', function($q, mp) {
	return {
		link: function(scope, element) {
			var logWindow = new LogWindow(element[0], 20);

			logWindow.addText("started\n");

			//Use getRawClient since we don't want Angular digest or JSON parsing
			var sub = $q.defer(); //use deferred in case element is destroyed before we subscribe
			mp.getRawClient(function(stompClient) {
				sub.resolve(stompClient.subscribe('/topic/logging', function(data) {
					logWindow.addText(data.body);
				}));
			});

			mp.send('/app/logging/start', '');

			//When we hide the logging element, unsubscribe to stop our callback and allow DOM to GC
			scope.$on('$destroy', function() {
				sub.promise.then(function(stompSubscription) {
					stompSubscription.unsubscribe();
				});
			});
		}
	}
});