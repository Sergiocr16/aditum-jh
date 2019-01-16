(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('BalanceByAccount', BalanceByAccount);

    BalanceByAccount.$inject = ['$resource', 'DateUtils'];

    function BalanceByAccount ($resource, DateUtils) {
        var resourceUrl =  'api/balance-by-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'findBetweenDatesByAccount':{
                method: 'GET',
                url: 'api/balance-by-account/between/:initial_time/:final_time/byAccount/:accountId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    accountId: '@accountId'
                }
            }
        });
    }
})();
