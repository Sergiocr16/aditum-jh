(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('HistoricalDefaulter', HistoricalDefaulter);

    HistoricalDefaulter.$inject = ['$resource', 'DateUtils'];

    function HistoricalDefaulter ($resource, DateUtils) {
        var resourceUrl =  'api/historical-defaulters/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
