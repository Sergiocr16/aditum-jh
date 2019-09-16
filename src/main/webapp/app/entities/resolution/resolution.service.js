(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Resolution', Resolution);

    Resolution.$inject = ['$resource', 'DateUtils'];

    function Resolution ($resource, DateUtils) {
        var resourceUrl =  'api/resolutions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
