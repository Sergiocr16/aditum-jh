(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Watch', Watch);

    Watch.$inject = ['$resource', 'DateUtils'];

    function Watch ($resource, DateUtils) {
        var resourceUrl =  'api/watches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.initialtime = DateUtils.convertDateTimeFromServer(data.initialtime);
                        data.finaltime = DateUtils.convertDateTimeFromServer(data.finaltime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
