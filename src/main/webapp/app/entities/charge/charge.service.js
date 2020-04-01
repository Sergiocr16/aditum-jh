(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Charge', Charge);

    Charge.$inject = ['$resource', 'DateUtils'];

    function Charge($resource, DateUtils) {
        var resourceUrl = 'api/charges/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
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
            'queryByHouse': {
                method: 'GET',
                url: 'api/chargesPerHouse/:houseId',
                params: {houseId: '@houseId'},
                isArray: true
            },
            'format': {
                method: 'GET',
                url: 'api/formatCompanyId',
                isArray: true
            },
            'findChargesToPayReport': {
                method: 'GET',
                url: 'api/charges/chargesToPay/:final_time/:type/byCompany/:companyId',
                params: {
                    companyId: '@companyId',
                    final_time: '@final_time',
                    type:'@type'
                },
            },
            'findBillingReport': {
                method: 'GET',
                url: 'api/charges/billingReport/:initial_time/:final_time/byCompany/:companyId/:houseId/:category',
                params: {
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId',
                    houseId:'@houseId',
                    category:'@category'
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
