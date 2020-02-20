(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('CondominiumRecord', CondominiumRecord);

    CondominiumRecord.$inject = ['$resource', 'DateUtils'];

    function CondominiumRecord ($resource, DateUtils) {
        var resourceUrl =  'api/condominium-records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'findByCompany': { method: 'GET',url:'api/condominium-records/find-all/:companyId/:type', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.uploadDate = DateUtils.convertDateTimeFromServer(data.uploadDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
