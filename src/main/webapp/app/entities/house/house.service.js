(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('House', House);

    House.$inject = ['$resource', 'DateUtils'];

    function House ($resource, DateUtils) {
        var resourceUrl =  'api/houses/:id';

        return $resource(resourceUrl, {companyId: 1}, {
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
            'update': { method:'PUT' }
        });
    }
})();
