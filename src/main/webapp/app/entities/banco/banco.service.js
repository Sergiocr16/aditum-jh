(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Banco', Banco);

    Banco.$inject = ['$resource', 'DateUtils'];

    function Banco ($resource, DateUtils) {
        var resourceUrl =  'api/bancos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaCapitalInicial = DateUtils.convertDateTimeFromServer(data.fechaCapitalInicial);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getAccountStatus':{
                method: 'GET',
                url: 'api/bancos/accountStatus/:first_month_day/:final_capital_date/:initial_time/:final_time/:accountId',
                params:{
                    first_month_day:'@first_month_day',
                    final_capital_date:'@final_capital_date',
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    accountId: '@accountId'
                }
            }
        });
    }
})();
