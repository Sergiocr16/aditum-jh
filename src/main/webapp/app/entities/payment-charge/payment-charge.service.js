(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('PaymentCharge', PaymentCharge);

    PaymentCharge.$inject = ['$resource', 'DateUtils'];

    function PaymentCharge ($resource, DateUtils) {
        var resourceUrl =  'api/payment-charges/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
