(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ProcedureComments', ProcedureComments);

    ProcedureComments.$inject = ['$resource'];

    function ProcedureComments ($resource) {
        var resourceUrl =  'api/procedure-comments/:id';

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
