(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('RHAccount', RHAccount);

    RHAccount.$inject = ['$resource'];

    function RHAccount ($resource) {
        var resourceUrl =  'api/r-h-accounts/:id';

        return $resource(resourceUrl, {  }, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },'findByUserId':{
                url:'api/r-h-accounts/findByUserId/:id',
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
