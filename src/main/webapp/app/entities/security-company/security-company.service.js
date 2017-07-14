(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('SecurityCompany', SecurityCompany);

    SecurityCompany.$inject = ['$resource'];

    function SecurityCompany ($resource) {
        var resourceUrl =  'api/security-companies/:id';

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
