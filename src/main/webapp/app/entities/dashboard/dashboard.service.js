(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Dashboard', Dashboard);

    Dashboard.$inject = ['$resource'];

    function Dashboard($resource) {
        var resourceUrl = 'api/dashboard/:companyId';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'updateYear': {
                method: 'GET',
                url: 'api/dashboard/updateByYear/:companyId',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'flujoIngresosEgresos':{
                method: 'GET',
                url: 'api/dashboard/anualReportIEB/:companyId/:year',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'updateMonth': {
                method: 'GET',
                url: 'api/dashboard/updateByMonth/:companyId',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'updateWeek': {
                method: 'GET',
                url: 'api/dashboard/updateByWeek/:companyId',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
        'defaulters': {
            method: 'GET',
                url: 'api/dashboard/defaulters/:companyId/:year',
                isArray: true,
                transformResponse: function (data) {
                if (data) {
                    data = angular.fromJson(data);
                }
                return data;
            }
        },
            'defaultersHistoric': {
                method: 'GET',
                url: 'api/dashboard/defaulters-historic/:companyId/:year',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
        });
    }
})();
