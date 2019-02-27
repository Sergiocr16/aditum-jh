(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ActivosFijos', ActivosFijos);

    ActivosFijos.$inject = ['$resource'];

    function ActivosFijos ($resource) {
        var resourceUrl =  'api/activos-fijos/:id';

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
