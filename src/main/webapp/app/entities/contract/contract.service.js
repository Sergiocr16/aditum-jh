(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Contract', Contract);

    Contract.$inject = ['$resource', 'DateUtils'];

    function Contract ($resource, DateUtils) {
        var resourceUrl =  'api/contracts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dueDate = DateUtils.convertDateTimeFromServer(data.dueDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
