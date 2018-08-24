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
        });
    }
})();
