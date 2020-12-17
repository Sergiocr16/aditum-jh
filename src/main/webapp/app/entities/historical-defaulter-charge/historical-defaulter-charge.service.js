(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('HistoricalDefaulterCharge', HistoricalDefaulterCharge);

    HistoricalDefaulterCharge.$inject = ['$resource', 'DateUtils'];

    function HistoricalDefaulterCharge ($resource, DateUtils) {
        var resourceUrl =  'api/historical-defaulter-charges/:id';

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
