(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('CommonAreaSchedule', CommonAreaSchedule);

    CommonAreaSchedule.$inject = ['$resource'];

    function CommonAreaSchedule ($resource) {
        var resourceUrl =  'api/common-area-schedules/:id';

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
            ,'findSchedulesByCommonArea': {
                method: 'GET',
                url: 'api/schedules/byCommonArea/:commonAreaId',
                isArray: true
            }
        });
    }
})();
