(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Egress', Egress);

    Egress.$inject = ['$resource', 'DateUtils'];

    function Egress ($resource, DateUtils) {
        var resourceUrl =  'api/egresses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                 isArray: false,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                        data.paymentDate = DateUtils.convertDateTimeFromServer(data.paymentDate);
                        data.expirationDate = DateUtils.convertDateTimeFromServer(data.expirationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'findBetweenDatesByCompany':{
                 method: 'GET',
                 url: 'api/egresses/between/:initial_time/:final_time/byCompany/:companyId',
                 isArray: true,
                 params:{
                      initial_time:'@initial_time',
                      final_time: '@final_time',
                      companyId: '@companyId',
            }
            },
            'findBetweenDatesByCompanyAndAccount':{
                 method: 'GET',
                 url: 'api/egresses/between/:initial_time/:final_time/byCompany/:companyId/andAccount/:accountId',
                 isArray: true,
                 params:{
                      initial_time:'@initial_time',
                      final_time: '@final_time',
                      companyId: '@companyId',
                      accountId: '@accountId'
            }
            }
        });
    }
})();
