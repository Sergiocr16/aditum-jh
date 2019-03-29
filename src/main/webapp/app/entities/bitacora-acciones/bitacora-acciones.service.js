(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('BitacoraAcciones', BitacoraAcciones);

    BitacoraAcciones.$inject = ['$resource', 'DateUtils'];

    function BitacoraAcciones ($resource, DateUtils) {
        var resourceUrl =  'api/bitacora-acciones/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.ejecutionDate = DateUtils.convertDateTimeFromServer(data.ejecutionDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
