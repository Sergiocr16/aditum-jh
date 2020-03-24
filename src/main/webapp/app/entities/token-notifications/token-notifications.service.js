(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('TokenNotifications', TokenNotifications);

    TokenNotifications.$inject = ['$resource'];

    function TokenNotifications ($resource) {
        var resourceUrl =  'api/token-notifications/:id';

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
            'login': {
                method: 'GET',
                url:'api/token-notifications-login/:userId/:token',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'logout': {
                method: 'GET',
                url:'api/token-notifications-logout/:userId/:token',
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
