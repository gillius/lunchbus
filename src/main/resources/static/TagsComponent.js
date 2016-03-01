angular.module('webapp').component('tags', {
	bindings: {
		route: '=',
		parentName: '=',
		tags: '='
	},
	controller: function(mp) {
		this.tagName = '';
		this.addTag = function() {
			mp.send(this.route, {name: this.parentName, tag: this.tagName});
		}
	},
	template: "<span ng-repeat='tag in $ctrl.tags track by $index'>{{tag}}, </span>" +
	"<form style='display: inline' name='tagsForm' ng-submit='$ctrl.addTag()'>" +
	"<input type='text' ng-model='$ctrl.tagName' ng-required='true'>" +
	"<button type='submit' ng-disabled='!tagsForm.$valid'>+</button>" +
	"</form>"
});