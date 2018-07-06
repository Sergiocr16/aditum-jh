(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Payment', Payment);

    Payment.$inject = ['$resource', 'DateUtils'];

    function Payment ($resource, DateUtils) {
        var resourceUrl =  'api/payments/:id';

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
            'sendPaymentEmail':{
               url:'api/payments/sendEmail/:paymentId'
             },
            'getByHouse':{
             url:'api/payments/byHouse/:houseId',
             isArray: true,
            },
            'getByHouseFilteredByDate':{
                 method: 'GET',
                  url: 'api/payments/between/:initial_time/:final_time/byHouseId/:houseId',
                  isArray: true,
                  params:{
                       initial_time:'@initial_time',
                       final_time: '@final_time',
                       companyId: '@houseId',
                       }
             },
            'update': { method:'PUT' },
            'findBetweenDatesByCompany':{
                     method: 'GET',
                     url: 'api/payments/between/:initial_time/:final_time/byCompany/:companyId',
                     isArray: true,
                     params:{
                          initial_time:'@initial_time',
                          final_time: '@final_time',
                          companyId: '@companyId',
                }
            },
          'findBetweenDatesByCompanyAndAccount':{
                 method: 'GET',
                 url: 'api/payments/between/:initial_time/:final_time/byCompany/:companyId/andAccount/:accountId',
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
