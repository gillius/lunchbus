angular.module('webapp').controller('IouCtrl', function(mp) {
	var ctrl = this;

	ctrl.entries = [];

	var updateEntries = function(data) {
		ctrl.entries = data.body;
	};

	mp.subscribe('/topic/iou', updateEntries);
	mp.subscribe('/user/queue/iou', updateEntries);

	mp.send('/app/iou/getCurrent', "");

	var increment = function(entry, amount) {
		mp.send('/app/iou/increment', {creditor: entry.creditor, debtor: entry.debtor, amount: amount});
	};

	this.more = function(entry) {
		increment(entry, 1);
	};

	this.less = function(entry) {
		increment(entry, -1);
	};

	this.addPerson = function() {
		mp.send('/app/iou/increment', {creditor: ctrl.creditor, debtor: ctrl.debtor, amount: 0});
	}
});