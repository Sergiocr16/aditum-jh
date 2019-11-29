(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('SubsidiaryType', SubsidiaryType);

    SubsidiaryType.$inject = ['$resource'];

    function SubsidiaryType ($resource) {
        var resourceUrl =  'api/subsidiary-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryAllByCompany': { url:'api/subsidiary-types/all-by-company/:id', method: 'GET', isArray: true},
            'queryAllSubByCompany': { url:'api/subsidiary-types/all-sub-by-company/:id', method: 'GET', isArray: true},
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
