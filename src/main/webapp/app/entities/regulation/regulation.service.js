(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Regulation', Regulation);

    Regulation.$inject = ['$resource'];

    function Regulation ($resource) {
        var resourceUrl =  'api/regulations/:id';

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
            'completeRegulationInfo': {
                method: 'GET',
                url: 'api/regulations/complete-regulations/:id',
                isArray: false,
                params: {
                    id: '@id'
                }
            },
            'searchInfoByCategoriesAndKeyWords': {
                method: 'PUT',
                url: 'api/regulations/searchInfoByCategoriesAndKeyWords'
            }
        });
    }
})();
