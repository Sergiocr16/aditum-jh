(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MensualAndAnualReport', Dashboard);

    Dashboard.$inject = ['$resource'];

    function Dashboard ($resource) {
        var resourceUrl =  'api/mensualAndAnualReport/:initial_time/:final_time/:companyId';
        return $resource(resourceUrl, {}, {
              'query': { method: 'GET',
              params:{
                 initial_time:'@initial_time',
                 final_time: '@final_time',
                 companyId: '@companyId',
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
