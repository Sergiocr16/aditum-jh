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
            'saveAllExtraordinaryCharges': {
                method: 'GET',
                isArray: true,
                url: 'api/charges/create/extraordinary-all',
            },
            'waterChargeByHouse': {
                method: 'GET',
                url: 'api/waterChargeByHouse/:houseId',
                params: {houseId: '@houseId'},
                isArray: true
            },
            'sendChargeEmail': {
                method: 'GET',
                url: 'api/charge-manual-send-email/bill/:companyId/:houseId/:emailTo',
                params: {companyId: '@companyId', houseId: '@houseId',emailTo: '@emailTo'},
                isArray: true
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
            'formatWater': {
                method: 'GET',
                url: 'api/formatCompanyWaterCharges',
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
