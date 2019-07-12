(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('VisitantInvitation', VisitantInvitation);

    VisitantInvitation.$inject = ['$resource', 'DateUtils'];

    function VisitantInvitation ($resource, DateUtils) {
        var resourceUrl =  'api/visitant-invitations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.invitationstartingtime = DateUtils.convertDateTimeFromServer(data.invitationstartingtime);
                        data.invitationlimittime = DateUtils.convertDateTimeFromServer(data.invitationlimittime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'findInvitedByHouse': {
                url: 'api/visitant-invitations/invited/findByHouse/:companyId/:houseId/:timeFormat',
                method: 'GET',
                params: {
                    houseId: '@houseId',
                    companyId: '@companyId',
                    timeFormat: '@timeFormat'
                },
                isArray: true
            },
            'findInvitedForAdmins': {
                url: 'api/visitant-invitations/invited/forAdmins/:companyId/:timeFormat',
                method: 'GET',
                params: {
                    companyId: '@companyId',
                    timeFormat: '@timeFormat'
                },
                isArray: true
            },
            'findInvitedByHouseAndIdentificationNumber': {
                url: 'api/visitant-invitations/invited/findOneByHouse/:identificationNumber/:houseId/:companyId/:hasSchedule',
                method: 'GET',
                params: {
                    identificationNumber: '@identificationNumber',
                    houseId: '@houseId',
                    companyId: '@companyId',
                    hasSchedule: '@hasSchedule'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.invitationstartingtime = DateUtils.convertDateTimeFromServer(data.invitationstartingtime);
                        data.invitationlimittime = DateUtils.convertDateTimeFromServer(data.invitationlimittime);
                    }
                    return data;
                }
            },
        });
    }
})();
