(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('CommonAreaReservations', CommonAreaReservations);

    CommonAreaReservations.$inject = ['$resource', 'DateUtils'];

    function CommonAreaReservations ($resource, DateUtils) {
        var resourceUrl =  'api/common-area-reservations/:id';

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
            ,'isAvailableToReserve': {
                method: 'GET',
                url: 'api/common-area-reservations/isAvailableToReserve/:maximun_hours/:reservation_date/:initial_time/:final_time/:common_area_id',
                params:{
                    maximun_hours:'@maximun_hours',
                    reservation_date: '@reservation_date',
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    common_area_id: '@common_area_id'
                }
            },
            'getPendingReservations':{
                method: 'GET',
                url: 'api/common-area-reservations/getPendingReservations/:companyId',
                isArray: true,
                params:{
                    companyId: '@companyId'
                }
            }
        });
    }
})();
