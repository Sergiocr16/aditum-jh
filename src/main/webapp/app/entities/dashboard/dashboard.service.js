(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Dashboard', Dashboard);

    Dashboard.$inject = ['$resource'];

    function Dashboard ($resource) {
        var resourceUrl =  'api/dashboard/:companyId';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',
                         transformResponse: function (data) {
                             if (data) {
                                 data = angular.fromJson(data);
                             }
                             return data;
                         }
             },
            'updateYear': { method: 'GET',
            url:'api/dashboard/updateByYear/:companyId',
                                isArray:true,
                                transformResponse: function (data) {
                                                             if (data) {
                                                                 data = angular.fromJson(data);
                                                             }
                                                             return data;
                                                         }
                         },
            'updateMonth': { method: 'GET',
            url:'api/dashboard/updateByMonth/:companyId',
                                      isArray:true,
                                      transformResponse: function (data) {
                                                                   if (data) {
                                                                       data = angular.fromJson(data);
                                                                   }
                                                                   return data;
                                                               }
                         },
        });
    }
})();
