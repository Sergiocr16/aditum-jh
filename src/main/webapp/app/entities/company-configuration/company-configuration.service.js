(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('CompanyConfiguration', CompanyConfiguration);

    CompanyConfiguration.$inject = ['$resource'];

    function CompanyConfiguration ($resource) {
        var resourceUrl =  'api/company-configurations/:id';

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
            }

            ,'getByCompanyId': {
            method: 'GET',
                url: 'api/getByCompanyId',
                isArray: true
        },
            'update': { method:'PUT' }
        });
    }
})();
