(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Officer', Officer);

    Officer.$inject = ['$resource'];

    function Officer ($resource) {
        var resourceUrl =  'api/officers/:id';

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
            }, 'officersEnabled': {
                method: 'GET',
                url: 'api/officersEnabled',
                isArray: true
            },'officersDisabled': {
                method: 'GET',
                url: 'api/officersDisabled',
                isArray: true
            },'findByUserId':{
                    url:'api/officers/findByUserId/:id',
                    method:'GET',
                    transformResponse: function (data) {
                        if (data) {
                            data = angular.fromJson(data);
                        }
                        return data;
                    }
            },
            'getByCompanyAndIdentification': {
                 method: 'GET',
                 url: 'api/officers/findByCompanyAndIdentification/:companyId/:identificationID',
                 params:{
                    companyId:'@companyId',
                    licensePlate: '@identificationID'
                  },
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
