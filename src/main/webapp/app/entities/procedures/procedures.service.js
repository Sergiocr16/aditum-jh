(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Procedures', Procedures);

    Procedures.$inject = ['$resource'];

    function Procedures ($resource) {
        var resourceUrl =  'api/procedures/:id';

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
