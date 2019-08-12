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
            'getCondos': {
                method: 'GET',
                url:'api/macro-condominiums/find-micros/:id'
            },
            'getAuthorizedInMacroByIdentification':{
                method: 'GET',
                url:'api/macro-condominiums/:id/findAuthorized/:identification',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getAuthorizedInMacroByPlate':{
                method: 'GET',
                url:'api/macro-condominiums/:id/findAuthorizedByPlate/:plate',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
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
