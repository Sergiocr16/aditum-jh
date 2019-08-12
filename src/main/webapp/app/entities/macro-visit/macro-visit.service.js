(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MacroVisit', MacroVisit);

    MacroVisit.$inject = ['$resource', 'DateUtils'];

    function MacroVisit ($resource, DateUtils) {
        var resourceUrl =  'api/macro-visits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.arrivaltime = DateUtils.convertDateTimeFromServer(data.arrivaltime);
                        data.departuretime = DateUtils.convertDateTimeFromServer(data.departuretime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'findByFilter': {
                method: 'GET',
                url: 'api/macro-visits/filter/:initial_time/:final_time/byMacro/:macroCondominiumId/byCompany/:companyId/byName/:name',
                isArray: true,
                params: {
                    initial_time: '@initial_time',
                    final_time: '@final_time',
                    macroCondominiumId: '@macroCondominiumId',
                    companyId: '@companyId',
                    name: '@name'
                }
            },
            'getByMacroIdAndPlate': {
                url:'api/macro-visits/:macroId/plate/:plate',
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.arrivaltime = DateUtils.convertDateTimeFromServer(data.arrivaltime);
                        data.departuretime = DateUtils.convertDateTimeFromServer(data.departuretime);
                    }
                    return data;
                }
            },
            'getByMacroIdAndIdentification': {
                url:'api/macro-visits/:macroId/identification/:identification',
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.arrivaltime = DateUtils.convertDateTimeFromServer(data.arrivaltime);
                        data.departuretime = DateUtils.convertDateTimeFromServer(data.departuretime);
                    }
                    return data;
                }
            },
        });
    }
})();
