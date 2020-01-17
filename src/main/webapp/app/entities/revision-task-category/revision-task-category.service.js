(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('RevisionTaskCategory', RevisionTaskCategory);

    RevisionTaskCategory.$inject = ['$resource'];

    function RevisionTaskCategory ($resource) {
        var resourceUrl =  'api/revision-task-categories/:id';

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