(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('RevisionConfig', RevisionConfig);

    RevisionConfig.$inject = ['$resource'];

    function RevisionConfig ($resource) {
        var resourceUrl =  'api/revision-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'findByCompany': { method: 'GET',url:'api/revision-configs/all/:companyId', isArray: true},
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
