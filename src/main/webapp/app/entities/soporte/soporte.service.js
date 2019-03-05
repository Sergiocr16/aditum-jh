(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Soporte', Soporte);

    Soporte.$inject = ['$resource', 'DateUtils'];

    function Soporte ($resource, DateUtils) {
        var resourceUrl =  'api/soportes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
