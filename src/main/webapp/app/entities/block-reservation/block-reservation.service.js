(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('BlockReservation', BlockReservation);

    BlockReservation.$inject = ['$resource'];

    function BlockReservation($resource) {
        var resourceUrl = 'api/block-reservations/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'isBlocked': {method: 'GET', isArray: false, url: "api/block-reservations/by-House-id/:houseId"},
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
