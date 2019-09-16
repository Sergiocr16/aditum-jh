(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('KeyWords', KeyWords);

    KeyWords.$inject = ['$resource'];

    function KeyWords ($resource) {
        var resourceUrl =  'api/key-words/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
