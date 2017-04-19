(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('House', House);

    House.$inject = ['$resource', 'DateUtils'];

    function House ($resource, DateUtils) {
        var resourceUrl =  'api/houses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
            'reportAbsence': { method:'PUT',url:'api/houses/report/absence'},
            'update': { method:'PUT' }
        });
    }
})();
