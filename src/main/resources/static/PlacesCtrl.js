angular.module('webapp').controller('PlacesCtrl', function(mp) {
	var ctrl = this;

	ctrl.places = [];

	var updatePlaces = function(data) {
		ctrl.places = data.body;
	};

	mp.subscribe('/topic/places', updatePlaces);
	mp.subscribe('/user/queue/places', updatePlaces);

	mp.send('/app/places/getCurrent', "");

	this.addPlace = function() {
		mp.send('/app/places/add', {name: ctrl.name});
		ctrl.name = '';
	};

	this.updateActive = function(item) {
		mp.send('/app/places/setActive', {name: item.name, active: item.active});
	}
});