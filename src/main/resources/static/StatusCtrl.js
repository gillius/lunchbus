angular.module('webapp').controller('StatusCtrl', function($scope, mp) {
	this.date = null;
	this.connected = false;

	var ctrl = this;
	
	mp.connected.then(function() {
		ctrl.connected = true;
	});

	mp.subscribe("/topic/date", function(data) {
		ctrl.date = data.body;
	});
});