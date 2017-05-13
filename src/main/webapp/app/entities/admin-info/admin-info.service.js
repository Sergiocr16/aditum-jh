(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('AdminInfo', AdminInfo);

    AdminInfo.$inject = ['$resource'];

    function AdminInfo ($resource) {
        var resourceUrl =  'api/admin-infos/:id';

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
            }

            ,'getAdminsByCompanyId': {
            method: 'GET',
                url: 'api/getAdminsByCompanyId',
                isArray: true
          },
            'update': { method:'PUT' },
            'findByUserId':{
                method:'GET',
                url:'api/admin-infos/findByUserId/:id'
            }
        });
    }
})();
