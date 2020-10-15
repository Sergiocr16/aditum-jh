(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('CustomChargeType', CustomChargeType);

    CustomChargeType.$inject = ['$resource'];

    function CustomChargeType($resource) {
        var resourceUrl = 'api/custom-charge-types/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getByCompany': {method: 'GET', isArray: true, url: "api/custom-charge-types/by-company/:companyId"},
            'update': {method: 'PUT'}
        });
    }
})();
