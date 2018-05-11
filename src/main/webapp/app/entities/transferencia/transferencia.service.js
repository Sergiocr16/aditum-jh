(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Transferencia', Transferencia);

    Transferencia.$inject = ['$resource', 'DateUtils'];

    function Transferencia ($resource, DateUtils) {
        var resourceUrl =  'api/transferencias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
               'findBetweenDatesByIncomingTransfer':{
                     method: 'GET',
                     url: 'api/transferencias/between/:initial_time/:final_time/incomingTransfer/:accountId',
                     isArray: true,
                     params:{
                          initial_time:'@initial_time',
                          final_time: '@final_time',
                          accountId: '@accountId'
                }
                },


            'findBetweenDatesByOutgoingTransfer':{
                 method: 'GET',
                 url: 'api/transferencias/between/:initial_time/:final_time/outgoingTransfer/:accountId',
                 isArray: true,
                 params:{
                      initial_time:'@initial_time',
                      final_time: '@final_time',
                      accountId: '@accountId'
            }
            },
        });
    }
})();
