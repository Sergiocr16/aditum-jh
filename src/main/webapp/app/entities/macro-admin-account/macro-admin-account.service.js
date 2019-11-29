(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MacroAdminAccount', MacroAdminAccount);

    MacroAdminAccount.$inject = ['$resource'];

    function MacroAdminAccount($resource) {
        var resourceUrl = 'api/macro-admin-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'queryByMacro': {method: 'GET', url: 'api/macro-admin-accounts/byMacro/:macroId', isArray: true},
            'findByUserId': {
                url: 'api/macro-admin-accounts/findByUserId/:id',
                method: 'GET',
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
            'update': {method: 'PUT'}
        });
    }
})();
