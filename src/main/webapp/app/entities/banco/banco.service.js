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
            'update': { method:'PUT' }
        });
    }
})();
