(function () {
    'use strict';

    angular
        .module('aditumApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getUserById': {
                method: 'GET',
                url: 'api/users/:id',

                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }

            }
            ,
            'createUserWithoutSendEmail': {
                method: 'POST',
                url: 'api/createUserWithoutSendEmail'
            },

            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}

        });

        return service;
    }
})();
