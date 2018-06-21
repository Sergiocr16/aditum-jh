(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('AdministrationConfiguration', AdministrationConfiguration);

    AdministrationConfiguration.$inject = ['$resource'];

    function AdministrationConfiguration ($resource) {
        var resourceUrl =  'api/administration-configurations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
            url:'api/administration-configurationsByCompanyId/:companyId',
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
