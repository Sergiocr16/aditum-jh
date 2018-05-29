(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Presupuesto', Presupuesto);

    Presupuesto.$inject = ['$resource', 'DateUtils'];

    function Presupuesto ($resource, DateUtils) {
        var resourceUrl =  'api/presupuestos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                        data.modificationDate = DateUtils.convertDateTimeFromServer(data.modificationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
