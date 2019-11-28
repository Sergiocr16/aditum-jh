(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Protocol', Protocol);

    Protocol.$inject = ['$resource'];

    function Protocol ($resource) {
        var resourceUrl =  'api/protocols/:id';

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
