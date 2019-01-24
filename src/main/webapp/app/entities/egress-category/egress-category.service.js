(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('EgressCategory', EgressCategory);

    EgressCategory.$inject = ['$resource'];

    function EgressCategory ($resource) {
        var resourceUrl =  'api/egress-categories/:id';

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
            'update': { method:'PUT' },
            'allCategoriesIncludingDevolution':{
                method: 'GET',
                url: 'api/egress-categories/allCategoriesIncludingDevolution/:companyId',
                isArray: true,
                params:{
                    companyId: '@companyId'
                }
            },
        });
    }
})();
