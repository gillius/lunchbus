angular.module('webapp', []);

angular.module('webapp').constant('host', (location.host || 'localhost').split(':')[0]);