(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('PaymentProof', PaymentProof);

    PaymentProof.$inject = ['$resource'];

    function PaymentProof ($resource) {
        var resourceUrl =  'api/payment-proofs/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
