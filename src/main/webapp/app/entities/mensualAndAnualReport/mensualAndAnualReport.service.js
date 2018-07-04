(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MensualAndAnualReport', Dashboard);

    Dashboard.$inject = ['$resource'];

    function Dashboard ($resource) {
        var resourceUrl =  'api/mensualReport/:first_month_day/:final_balance_time/:initial_time/:final_time/:companyId/:withPresupuesto';
        return $resource(resourceUrl, {}, {
              'query': { method: 'GET',
                      params:{
                         first_month_day:'@first_month_day',
                         final_balance_time:'@final_balance_time',
                         initial_time:'@initial_time',
                         final_time: '@final_time',
                         companyId: '@companyId',
                         withPresupuesto: '@withPresupuesto',
                     },
                     transformResponse: function (data) {
                         if (data) {
                             data = angular.fromJson(data);
                         }
                         return data;
                     }

             },
               'getAnualReport': { method: 'GET',
                           url: 'api/mensualAndAnualReport/:actual_month/:companyId/:withPresupuesto',
                           params:{
                              actual_month:'@actual_month',
                              companyId: '@companyId',
                              withPresupuesto: '@withPresupuesto'
                          },
                          transformResponse: function (data) {
                              if (data) {
                                  data = angular.fromJson(data);
                              }
                              return data;
                          }

                          }
        });
    }
})();
