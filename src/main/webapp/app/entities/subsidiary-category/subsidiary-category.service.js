(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('SubsidiaryCategory', SubsidiaryCategory);

    SubsidiaryCategory.$inject = ['$resource'];

    function SubsidiaryCategory ($resource) {
        var resourceUrl =  'api/subsidiary-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
