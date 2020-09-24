(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ExchangeRateBccr', ExchangeRateBccr);

    ExchangeRateBccr.$inject = ['$resource', 'DateUtils'];

    function ExchangeRateBccr($resource, DateUtils) {
        var resourceUrl = 'api/payments/:id';

        return $resource(resourceUrl, {}, {
            'get': {
                url: 'api/exchange-rate/:fechaInicio/:fechaFinal'
            },
        });
    }
})();
