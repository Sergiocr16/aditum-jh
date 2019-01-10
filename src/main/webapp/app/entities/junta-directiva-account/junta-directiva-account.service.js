(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('JuntaDirectivaAccount', JuntaDirectivaAccount);

    JuntaDirectivaAccount.$inject = ['$resource'];

    function JuntaDirectivaAccount ($resource) {
        var resourceUrl =  'api/junta-directiva-accounts/:id';

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
            },
            'update': { method:'PUT' },
            'getByCompanyId': {
                method: 'GET',
                url: 'api/junta-directiva-accounts/findByCompanyId/:companyId',
                params:{
                    companyId:'@companyId'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },'findByUserId':{
                url:'api/junta-directiva-accounts/findByUserId/:id',
                method:'GET',
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
