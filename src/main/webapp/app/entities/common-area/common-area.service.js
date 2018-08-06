(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('CommonArea', CommonArea);

    CommonArea.$inject = ['$resource'];

    function CommonArea ($resource) {
        var resourceUrl =  'api/common-areas/:id';

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
