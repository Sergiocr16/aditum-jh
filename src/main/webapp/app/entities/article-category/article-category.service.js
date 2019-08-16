(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ArticleCategory', ArticleCategory);

    ArticleCategory.$inject = ['$resource'];

    function ArticleCategory ($resource) {
        var resourceUrl =  'api/article-categories/:id';

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
