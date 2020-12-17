(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('HistoricalPositive', HistoricalPositive);

    HistoricalPositive.$inject = ['$resource', 'DateUtils'];

    function HistoricalPositive ($resource, DateUtils) {
        var resourceUrl =  'api/historical-positives/:id';

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
