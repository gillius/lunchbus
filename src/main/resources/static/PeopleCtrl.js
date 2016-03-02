angular.module('webapp').controller('PeopleCtrl', function(mp) {
	var ctrl = this;

	ctrl.people = [];

	var updatePeople = function(data) {
		ctrl.people = data.body;
	};

	mp.subscribe('/topic/people', updatePeople);
	mp.subscribe('/user/queue/people', updatePeople);

	mp.send('/app/people/getCurrent', "");

	this.addPerson = function() {
		mp.send('/app/people/add', {name: ctrl.name});
		ctrl.name = '';
	};

	this.updateActive = function(item) {
		mp.send('/app/people/setActive', {name: item.name, active: item.active});
	}
});