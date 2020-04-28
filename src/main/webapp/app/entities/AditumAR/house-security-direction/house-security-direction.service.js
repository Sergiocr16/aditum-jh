(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('HouseSecurityDirection', HouseSecurityDirection);

    HouseSecurityDirection.$inject = ['$resource'];

    function HouseSecurityDirection ($resource) {
        var resourceUrl =  'api/house-security-directions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getAllHouses': {
                method: 'GET',
                isArray: true,
                url: "api/allHousesAR/:companyId/:desocupated/:houseNumber"
            },
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
