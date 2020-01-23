(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ReservationHouseRestrictions', ReservationHouseRestrictions);

    ReservationHouseRestrictions.$inject = ['$resource', 'DateUtils'];

    function ReservationHouseRestrictions ($resource, DateUtils) {
        var resourceUrl =  'api/reservation-house-restrictions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastTimeReservation = DateUtils.convertDateTimeFromServer(data.lastTimeReservation);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
