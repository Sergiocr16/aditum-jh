(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Tasks', Tasks);

    Tasks.$inject = ['$resource', 'DateUtils'];

    function Tasks ($resource, DateUtils) {
        var resourceUrl =  'api/tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.expirationDate = DateUtils.convertDateTimeFromServer(data.expirationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'allByCompany': {
                url:'api/tasks/by-company/:companyId/:status',
                method: 'GET', isArray: true
            },
        });
    }
})();
