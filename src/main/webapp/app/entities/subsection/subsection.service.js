(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Subsection', Subsection);

    Subsection.$inject = ['$resource'];

    function Subsection ($resource) {
        var resourceUrl =  'api/subsections/:id';

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
