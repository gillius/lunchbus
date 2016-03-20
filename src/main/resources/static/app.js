angular.module('webapp', []);

angular.module('webapp').constant('host', (location.host || 'localhost').split(':')[0]);
angular.module('webapp').constant('port', location.port ? ':' + location.port : '');
angular.module('webapp').constant('protocol', (location.protocol || 'http:'));