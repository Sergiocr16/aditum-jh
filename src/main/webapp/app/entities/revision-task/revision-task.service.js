(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('RevisionTask', RevisionTask);

    RevisionTask.$inject = ['$resource'];

    function RevisionTask ($resource) {
        var resourceUrl =  'api/revision-tasks/:id';

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
