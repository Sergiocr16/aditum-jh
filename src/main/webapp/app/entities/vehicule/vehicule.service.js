(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Vehicule', Vehicule);

    Vehicule.$inject = ['$resource'];

    function Vehicule ($resource) {
        var resourceUrl =  'api/vehicules/:id';

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
