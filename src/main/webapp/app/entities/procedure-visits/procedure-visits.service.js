(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ProcedureVisits', ProcedureVisits);

    ProcedureVisits.$inject = ['$resource', 'DateUtils'];

    function ProcedureVisits ($resource, DateUtils) {
        var resourceUrl =  'api/procedure-visits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.visitDate = DateUtils.convertDateTimeFromServer(data.visitDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
