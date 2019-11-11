(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ProcedureVisitRanking', ProcedureVisitRanking);

    ProcedureVisitRanking.$inject = ['$resource'];

    function ProcedureVisitRanking ($resource) {
        var resourceUrl =  'api/procedure-visit-rankings/:id';

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
