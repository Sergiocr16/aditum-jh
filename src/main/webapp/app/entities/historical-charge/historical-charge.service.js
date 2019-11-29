(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('HistoricalCharge', HistoricalCharge);

    HistoricalCharge.$inject = ['$resource', 'DateUtils'];

    function HistoricalCharge ($resource, DateUtils) {
        var resourceUrl =  'api/historical-charges/:id';

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
