(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Collection', Collection);

    Collection.$inject = ['$resource', 'DateUtils'];

    function Collection ($resource, DateUtils) {
        var resourceUrl =  'api/collections/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'getCollectionByYear': {
                url:'api/collections/:companyId/:year',
                method: 'GET',
                isArray: true
            },
            'update': { method:'PUT' }
        });
    }
})();
