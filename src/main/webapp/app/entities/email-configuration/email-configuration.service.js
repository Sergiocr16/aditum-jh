(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('EmailConfiguration', EmailConfiguration);

    EmailConfiguration.$inject = ['$resource'];

    function EmailConfiguration ($resource) {
        var resourceUrl =  'api/email-configurations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
