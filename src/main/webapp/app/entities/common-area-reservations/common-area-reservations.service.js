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
                url: 'api/common-area-reservations/isAvailableToReserve/:maximun_hours/:reservation_date/:initial_time/:final_time/:common_area_id/:house_id',
                params:{
                    maximun_hours:'@maximun_hours',
                    reservation_date: '@reservation_date',
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    common_area_id: '@common_area_id',
                    house_id: '@house_id'
                }
            }
            ,'isAvailableToReserveNotNull': {
            method: 'GET',
                url: 'api/common-area-reservations/isAvailableToReserveNotNull/:maximun_hours/:reservation_date/:initial_time/:final_time/:common_area_id/:house_id/:reservation_id',
                params:{
                maximun_hours:'@maximun_hours',
                    reservation_date: '@reservation_date',
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    common_area_id: '@common_area_id',
                    house_id: '@house_id',
                    reservation_id: '@reservation_id'
            }
        },
            'findBetweenDatesByHouse':{
                method: 'GET',
                url: 'api/common-area-reservations/between/:initial_time/:final_time/byHouse/:houseId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    houseId: '@houseId'
                }
            },
            'getDevolutionDoneReservations':{
                method: 'GET',
                url: 'api/common-area-reservations/getDevolutionDoneReservations/:companyId',
                isArray: true,
                params:{
                    companyId: '@companyId'
                }
            },
            'findDevolutionDoneBetweenDates':{
                method: 'GET',
                url: 'api/common-area-reservations/devolutionDoneBetween/:initial_time/:final_time/byCompany/:companyId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId'
                }
            },
            'findBetweenDatesByCompany':{
                method: 'GET',
                url: 'api/common-area-reservations/between/:initial_time/:final_time/byCompany/:companyId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId'
                }
            },
            'findBetweenDatesByCompanyAndStatus':{
                method: 'GET',
                url: 'api/common-area-reservations/between/:initial_time/:final_time/byCompany/:companyId/status/:status',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId',
                    status: '@status'
                }
            },
            'forAccessDoor':{
                method: 'GET',
                url: 'api/common-area-reservations/forAccessDoor/:initial_time/:companyId/',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    companyId: '@companyId'
                }
            },
            'findBetweenDatesByCommonArea':{
                method: 'GET',
                url: 'api/common-area-reservations/between/:initial_time/:final_time/byCommonArea/:commonAreaId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    commonAreaId: '@commonAreaId'
                }
            },
            'findBetweenDatesByCommonAreaUser':{
                method: 'GET',
                url: 'api/common-area-reservations/between/user/:initial_time/:final_time/byCommonArea/:commonAreaId',
                isArray: true,
                params:{
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                    commonAreaId: '@commonAreaId'
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
            'getPendingAndAcceptedReservationsBetweenDates':{
                method: 'GET',
                url: 'api/common-area-reservations/getPendingAndAcceptedReservations/betweenDates/:companyId/:initial_time/:final_time',
                isArray: true,
                params:{
                    companyId: '@companyId',
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                }
            },
            'getPendingAndAcceptedReservationsBetweenDatesAndArea':{
                method: 'GET',
                url: 'api/common-area-reservations/getPendingAndAcceptedReservations/betweenDates/area/:areaId/:initial_time/:final_time',
                isArray: true,
                params:{
                    companyId: '@areaId',
                    initial_time:'@initial_time',
                    final_time: '@final_time',
                }
            },
            'getReservationsByCommonArea':{
                method: 'GET',
                url: 'api/common-area-reservations/getReservationsByCommonArea/:commonAreaId',
                isArray: true,
                params:{
                    commonAreaId: '@commonAreaId',
                }
            },
            'getReservationsByCommonAreaFromNow':{
                method: 'GET',
                url: 'api/common-area-reservations/getReservationsByCommonArea/fromNow/:commonAreaId',
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
