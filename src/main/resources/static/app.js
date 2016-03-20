angular.module('webapp', []);

angular.module('webapp').constant('host', (location.host || 'localhost').split(':')[0]);
angular.module('webapp').constant('port', (location.port || 8080));
angular.module('webapp').constant('protocol', (location.protocol || 'http:'));