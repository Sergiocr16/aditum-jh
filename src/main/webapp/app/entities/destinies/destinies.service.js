(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Destinies', Destinies);

    Destinies.$inject = ['$resource'];

    function Destinies ($resource) {
        var resourceUrl =  'api/destinies/:id';

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
