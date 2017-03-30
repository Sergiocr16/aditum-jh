(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Resident', Resident);

    Resident.$inject = ['$resource'];

    function Resident ($resource) {
        var resourceUrl =  'api/residents/:id';
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
            'update': { method:'PUT' },
            'residentsEnabled': {
                method: 'GET',
                url: 'api/residentsEnabled',
                isArray: true
             },'residentsDisabled': {
                method: 'GET',
                url: 'api/residentsDisabled',
                isArray: true
            },'findByUserId':{
                url:'api/residents/findByUserId/:id',
                method:'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },'findResidentesByHouseId': {
                 method: 'GET',
                 url: 'api/residents/byHouse',
                 isArray: true
             }
        });
    }
})();
