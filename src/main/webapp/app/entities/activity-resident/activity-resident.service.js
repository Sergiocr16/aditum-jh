(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ActivityResident', ActivityResident);

    ActivityResident.$inject = ['$resource', 'DateUtils'];

    function ActivityResident($resource, DateUtils) {
        var resourceUrl = 'api/activity-residents/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'getByUser': {method: 'GET', isArray: true, url: "api/activity-residents-by-user/:userId"},
            'getNotSeeingByUser': {method: 'GET', isArray: true, url: "api/activity-residents-by-user/not-seeing/:userId"},
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
            'update': {method: 'PUT'}
        });
    }
})();
