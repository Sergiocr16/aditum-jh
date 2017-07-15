(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Note', Note);

    Note.$inject = ['$resource', 'DateUtils'];

    function Note ($resource, DateUtils) {
        var resourceUrl =  'api/notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
          'findAll': {
                method: 'GET',
                url: 'api/notes',
                isArray: true
             },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationdate = DateUtils.convertDateTimeFromServer(data.creationdate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
