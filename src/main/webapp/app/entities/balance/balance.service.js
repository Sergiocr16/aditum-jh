(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Balance', Balance);

    Balance.$inject = ['$resource'];

    function Balance ($resource) {
        var resourceUrl =  'api/balances/:id';

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
            },
            'queryBalances': {
                method: 'GET',
                url: 'api/houses-balance',
                isArray: true
             },
            'positiveBalanceByHouse': {
                method: 'GET',
                url: 'api/houses-balance-by-house/:houseId',
            },
            'update': { method:'PUT' }
        });
    }
})();
