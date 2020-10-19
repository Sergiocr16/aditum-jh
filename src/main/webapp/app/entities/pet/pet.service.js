(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Pet', Pet);

    Pet.$inject = ['$resource'];

    function Pet($resource) {
        var resourceUrl = 'api/pets/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'getByCompany': {method: 'GET', isArray: true, url: "api/pets/by-company/:companyId/:name"},
            'getByHouse': {method: 'GET', isArray: true, url: "api/pets/by-house/:houseId/:name"},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
