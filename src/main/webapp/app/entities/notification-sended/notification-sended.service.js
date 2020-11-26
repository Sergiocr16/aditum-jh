(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('NotificationSended', NotificationSended);

    NotificationSended.$inject = ['$resource'];

    function NotificationSended ($resource) {
        var resourceUrl =  'api/notification-sendeds/:id';

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
