(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('AccessDoor', AccessDoor);

    AccessDoor.$inject = ['$resource'];

    function AccessDoor ($resource) {
        var resourceUrl =  'api/access-doors/:id';

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
