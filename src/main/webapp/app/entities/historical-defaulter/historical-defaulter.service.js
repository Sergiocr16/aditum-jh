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
            'formatAll':{method: 'GET', url:"api/historical-defaulters/format-all-company/:monthNumber"},
            'formatOneSpecificMonth':{method: 'GET', url:"api/historical-defaulters/format-company/:companyId/:monthNumber"},
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
