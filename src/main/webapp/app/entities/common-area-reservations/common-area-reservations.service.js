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
                        console.log(data)
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
            }
            ,'isAvailableToReserveNotNull': {
            method: 'GET',
                url: 'api/common-area-reservations/isAvailableToReserveNotNull/:maximun_hours/:reservation_date/:initial_time/:final_time/:common_area_id/:reservation_id',
                params:{
                maximun_hours:'@maximun_hours',
                    reservation_date: '@reservation_date',
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    common_area_id: '@common_area_id',
                    reservation_id: '@reservation_id'
            }
        },
            'findBetweenDatesByCompany':{
                method: 'GET',
                url: 'api/common-area-reservations/between/:initial_time/:final_time/byCompany/:companyId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId',
                }
            },
            'getPendingReservations':{
                method: 'GET',
                url: 'api/common-area-reservations/getPendingReservations/:companyId',
                isArray: true,
                params:{
                    companyId: '@companyId'
                }
            },
            'getAcceptedReservations':{
                method: 'GET',
                url: 'api/common-area-reservations/getAcceptedReservations/:companyId',
              isArray: true,
                params:{
                    companyId: '@companyId'
                }
            },
            'getLastAcceptedReservations':{
                method: 'GET',
                url: 'api/common-area-reservations/getLastAcceptedReservations/:companyId',
                isArray: true,
                params:{
                    companyId: '@companyId'
                }
            },
            'getPendingAndAcceptedReservations':{
                method: 'GET',
                url: 'api/common-area-reservations/getPendingAndAcceptedReservations/:companyId',
                isArray: true,
                params:{
                    companyId: '@companyId'
                }
            },
            'getReservationsByCommonArea':{
                method: 'GET',
                url: 'api/common-area-reservations/getReservationsByCommonArea/:commonAreaId',
                isArray: true,
                params:{
                    commonAreaId: '@commonAreaId'
                }
            },
            'findByHouseId':{
                method: 'GET',
                url: 'api/common-area-reservations/findByHouseId/:houseId',
                isArray: true,
                params:{
                    houseId: '@houseId'
                }
            },
        });
    }
})();
