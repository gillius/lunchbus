angular.module('webapp').controller('SelectorCtrl', function(mp) {
	var ctrl = this;
	ctrl.selectedPlace = "";
	ctrl.selectPlace = function() {
		mp.send('/app/selection/selectPlace', '');
	};
	ctrl.clearPlace = function() {
		mp.send('/app/selection/clearSelectedPlace', '');
	};

	function updateSelectedPlace(data) {
		ctrl.selectedPlace = data.body;
	}

	mp.subscribe('/user/queue/selectedPlace', updateSelectedPlace);
	mp.subscribe('/topic/selectedPlace', updateSelectedPlace);

	mp.send('/selection/getSelectedPlace', '');
});