(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MacroCondominium', MacroCondominium);

    MacroCondominium.$inject = ['$resource'];

    function MacroCondominium ($resource) {
        var resourceUrl =  'api/macro-condominiums/:id';

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
