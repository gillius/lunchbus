angular.module('webapp').component('tags', {
	bindings: {
		route: '=',
		parentName: '=',
		tags: '='
	},
	controller: function(mp) {
		this.tagName = '';
		this.addTag = function() {
			mp.send(this.route + '/addTag', {name: this.parentName, tag: this.tagName});
		};
		this.removeTag = function(tagName) {
			mp.send(this.route + '/removeTag', {name: this.parentName, tag: tagName});
		}
	},
	template: "<span class='tag label label-info' ng-repeat='tag in $ctrl.tags track by $index'>" +
	"{{tag}} " +
	"<a class='remove' href='#' ng-click='$ctrl.removeTag(tag)'>&times;</a>" +
	"</span>" +
	"<form class='form-inline' style='display: inline' name='tagsForm' ng-submit='$ctrl.addTag()'>" +
	"<div class='input-group'>" +
	"<input class='form-control input-sm' type='text' ng-model='$ctrl.tagName' ng-required='true'> " +
	"<span class='input-group-btn'>" +
	"<button class='btn btn-default btn-sm' type='submit' ng-disabled='!tagsForm.$valid'>+</button>" +
	"</span>" +
	"</div>" +
	"</form>"
});