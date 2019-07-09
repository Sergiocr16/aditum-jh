(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('InvitationSchedule', InvitationSchedule);

    InvitationSchedule.$inject = ['$resource'];

    function InvitationSchedule ($resource) {
        var resourceUrl =  'api/invitation-schedules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
            ,'findSchedulesByInvitation': {
                method: 'GET',
                url: 'api/invitation-schedules/byInvitation/:invitationId',
                isArray: true
            }

        });
    }
})();
