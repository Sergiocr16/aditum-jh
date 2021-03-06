(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('House', House);

    House.$inject = ['$resource', 'DateUtils'];

    function House($resource, DateUtils) {
        var resourceUrl = 'api/houses/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'getAllHouses': {
                method: 'GET',
                isArray: true,
                url: "api/allHouses/:companyId/:desocupated/:houseNumber"
            },
            'getAllHousesClean': {
                method: 'GET',
                isArray: true,
                url: "api/houses/clean/:companyId"
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.desocupationinitialtime = DateUtils.convertDateTimeFromServer(data.desocupationinitialtime);
                        data.desocupationfinaltime = DateUtils.convertDateTimeFromServer(data.desocupationfinaltime);
                    }
                    return data;
                }
            },
            'getClean': {
                method: 'GET',
                url:'api/house/clean/:id',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getByLoginCode': {
                method: 'GET',
                url: 'api/houses/housesByLoginCode/:loginCode',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.desocupationinitialtime = DateUtils.convertDateTimeFromServer(data.desocupationinitialtime);
                        data.desocupationfinaltime = DateUtils.convertDateTimeFromServer(data.desocupationfinaltime);
                    }
                    return data;
                }
            },
            'validate': {
                method: 'GET',
                url: 'api/houses/validate/:houseNumber/:extension/:companyId',
                params: {
                    houseNumber: '@houseNumber',
                    extension: '@extension',
                    companyId: '@companyId'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.desocupationinitialtime = DateUtils.convertDateTimeFromServer(data.desocupationinitialtime);
                        data.desocupationfinaltime = DateUtils.convertDateTimeFromServer(data.desocupationfinaltime);
                    }
                    return data;
                }
            },

            'validateUpdate': {
                method: 'GET',
                url: 'api/houses/validateUpdate/:houseId/:houseNumber/:extension/:companyId',
                params: {
                    houseNumber: '@houseNumber',
                    extension: '@extension',
                    companyId: '@companyId',
                    houseId: '@houseId'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.desocupationinitialtime = DateUtils.convertDateTimeFromServer(data.desocupationinitialtime);
                        data.desocupationfinaltime = DateUtils.convertDateTimeFromServer(data.desocupationfinaltime);
                    }
                    return data;
                }
            },
            'reportAbsence': {method: 'PUT', url: 'api/houses/report/absence'},
            'update': {method: 'PUT'}
        });
    }
})();
