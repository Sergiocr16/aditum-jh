(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Visitant', Visitant);

    Visitant.$inject = ['$resource', 'DateUtils'];

    function Visitant($resource, DateUtils) {
        var resourceUrl = 'api/visitants/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.arrivaltime = DateUtils.convertDateTimeFromServer(data.arrivaltime);
                        data.invitationstaringtime = DateUtils.convertDateTimeFromServer(data.invitationstaringtime);
                        data.invitationlimittime = DateUtils.convertDateTimeFromServer(data.invitationlimittime);
                    }
                    return data;
                }
            },
            'findInvitedByHouseAndIdentificationNumber': {
                url: 'api/visitants/invited/findOneByHouse/:identificationNumber/:houseId/:companyId',
                method: 'GET',
                params: {
                    identificationNumber: '@identificationNumber',
                    houseId: '@houseId',
                    companyId: '@companyId',
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.invitationstaringtime = DateUtils.convertDateTimeFromServer(data.invitationstaringtime);
                        data.invitationlimittime = DateUtils.convertDateTimeFromServer(data.invitationlimittime);
                    }
                    return data;
                }
            },
            'findByFilter': {
                method: 'GET',
                url: 'api/visitants/filter/:initial_time/:final_time/byCompany/:companyId/byHouse/:houseId/byName/:name',
                isArray: true,
                params: {
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId',
                    houseId: '@houseId',
                    name: '@name',
                }
            },
            'findInvitedByHouse': {
                url: 'api/visitants/invited/finByHouse/:companyId/:houseId',
                method: 'GET',
                params: {
                    houseId: '@houseId',
                    companyId: '@companyId',
                },
                isArray: true,
            },
            'findAllInvited': {
                url: 'api/visitants/invited/all/:companyId/',
                method: 'GET',
                params: {
                    companyId: '@companyId',
                },
                isArray: true,
            },
            'findByHouseInLastMonth': {
                url: 'api/visitants/finByHouse/lastMonth/:houseId',
                method: 'GET',
                params: {
                    houseId: '@houseId'
                },
                isArray: true,
            },
            'findForAdminInLastMonth': {
                url: 'api/visitants/ForAdmin/lastMonth/:companyId',
                method: 'GET',
                params: {
                    companyId: '@companyId'
                },
                isArray: true
            },
            'findBetweenDatesByHouse': {
                method: 'GET',
                url: 'api/visitants/between/:initial_time/:final_time/byHouse/:houseId',
                isArray: true,
                params: {
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    houseId: '@houseId'
                }
            },
            'findBetweenDatesForAdmin': {
                method: 'GET',
                url: 'api/visitants/between/:initial_time/:final_time/ForAdmin/:companyId',
                isArray: true,
                params: {
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId'
                }
            },
            'findByCompanyInLastMonth': {
                url: 'api/visitants/finByCompany/lastMonth/:companyId',
                method: 'GET',
                params: {
                    companyId: '@companyId'
                },
                isArray: true,
            },
            'findBetweenDatesByCompany': {
                method: 'GET',
                url: 'api/visitants/between/:initial_time/:final_time/byCompany/:companyId',
                isArray: true,
                params: {
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    companyId: '@companyId',
                }
            },

            'getVisitorsInTransitByCompany': {
                url: 'api/visitants/getVisitorsInTransitByCompany/:companyId',
                method: 'GET',
                params: {
                    companyId: '@companyId'
                },
                isArray: true
            },
            'getVisitorsInTransitByCompanyFilter': {
                url: 'api/visitants/getVisitorsInTransitByCompany/filter/:companyId/:houseId/:name',
                method: 'GET',
                params: {
                    companyId: '@companyId'
                },
                isArray: true
            },
            'getVisitorsInTransitByHouse': {
                url: 'api/visitants/getVisitorsInTransitByHouse/:houseId',
                method: 'GET',
                params: {
                    houseId: '@houseId'
                },
                isArray: true
            },


            'getByCompanyIdAndIdentification': {
                method: 'GET',
                url: 'api/visitants/:companyId/identification/:identification',
            },
            'getByCompanyIdAndPlate': {
                method: 'GET',
                url: 'api/visitants/:companyId/plate/:plate',
            },
            'update': {method: 'PUT'},
            'delete': {method: 'DELETE'}
        });
    }
})();
