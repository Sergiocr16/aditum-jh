(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('HouseLoginTracker', HouseLoginTracker);

    HouseLoginTracker.$inject = ['$resource', 'DateUtils'];

    function HouseLoginTracker ($resource, DateUtils) {
        var resourceUrl =  'api/house-login-trackers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastTime = DateUtils.convertDateTimeFromServer(data.lastTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
