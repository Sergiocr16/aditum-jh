(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('OfficerAccount', OfficerAccount);

    OfficerAccount.$inject = ['$resource'];

    function OfficerAccount ($resource) {
        var resourceUrl =  'api/officer-accounts/:id';

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
             ,'getOfficerAccountsByCompanyId': {
                    method: 'GET',
                        url: 'api/getAccountsByCompanyId',
                        isArray: true
                  },
            'findByUserId':{
                url:'api/officer-accounts/findByUserId/:id',
                method:'GET',
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
