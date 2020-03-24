(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('RevisionConfigTask', RevisionConfigTask);

    RevisionConfigTask.$inject = ['$resource'];

    function RevisionConfigTask ($resource) {
        var resourceUrl =  'api/revision-config-tasks/:id';

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
