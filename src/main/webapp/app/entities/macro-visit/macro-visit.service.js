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
            'update': { method:'PUT' }
        });
    }
})();
