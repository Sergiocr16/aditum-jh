(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MacroOfficerAccount', MacroOfficerAccount);

    MacroOfficerAccount.$inject = ['$resource'];

    function MacroOfficerAccount ($resource) {
        var resourceUrl =  'api/macro-officer-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryByMacro': { method: 'GET',url:'api/macro-officer-accounts/byMacro/:macroId', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
            ,'findByUserId':{
            url:'api/macro-officer-accounts/findByUserId/:id',
                method:'GET',
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
