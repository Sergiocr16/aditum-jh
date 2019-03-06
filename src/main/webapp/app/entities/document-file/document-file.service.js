(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('DocumentFile', DocumentFile);

    DocumentFile.$inject = ['$resource'];

    function DocumentFile ($resource) {
        var resourceUrl =  'api/document-files/:id';

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
