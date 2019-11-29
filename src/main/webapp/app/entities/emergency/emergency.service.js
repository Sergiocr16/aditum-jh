(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Emergency', Emergency);

    Emergency.$inject = ['$resource'];

    function Emergency ($resource) {
        var resourceUrl =  'api/emergencies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
           'findAll': {
               method: 'GET',
               url: 'api/emergencies',
               isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
