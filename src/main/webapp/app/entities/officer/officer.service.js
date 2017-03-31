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
            'update': { method:'PUT' }
        });
    }
})();
