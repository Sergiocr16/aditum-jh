(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('WaterConsumption', WaterConsumption);

    WaterConsumption.$inject = ['$resource', 'DateUtils'];

    function WaterConsumption ($resource, DateUtils) {
        var resourceUrl =  'api/water-consumptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryByDate': { method: 'GET', url:"api/water-consumptions/:companyId/:date", isArray: true},
            'bilWaterConsumption': { method: 'GET', url:"api/water-consumptions/bill/:wcId/:date/:sendEmail", isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.recordDate = DateUtils.convertDateTimeFromServer(data.recordDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
