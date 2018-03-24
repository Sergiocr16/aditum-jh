(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Egress', Egress);

    Egress.$inject = ['$resource', 'DateUtils'];

    function Egress ($resource, DateUtils) {
        var resourceUrl =  'api/egresses/:id';

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
