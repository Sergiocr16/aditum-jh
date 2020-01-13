(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Revision', Revision);

    Revision.$inject = ['$resource', 'DateUtils'];

    function Revision ($resource, DateUtils) {
        var resourceUrl =  'api/revisions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.executionDate = DateUtils.convertDateTimeFromServer(data.executionDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
