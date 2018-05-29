(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('DetallePresupuesto', DetallePresupuesto);

    DetallePresupuesto.$inject = ['$resource'];

    function DetallePresupuesto ($resource) {
        var resourceUrl =  'api/detalle-presupuestos/:id';

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
