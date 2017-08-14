(function() {
    'use strict';

    angular
        .module('aditumApp')
        .factory('authInterceptor', authInterceptor);

    authInterceptor.$inject = ['$rootScope', '$q', '$location', '$localStorage', '$sessionStorage', 'cloudinary'];

    function authInterceptor ($rootScope, $q, $location, $localStorage, $sessionStorage, cloudinary) {
        var service = {
            request: request
        };

        return service;

        function request (config) {
            /*jshint camelcase: false */
            config.headers = config.headers || {};
            if (config.url.startsWith(cloudinary.config().base_url)) {
                return config;
            }
            var token = $localStorage.authenticationToken || $sessionStorage.authenticationToken;
            if (token) {
                config.headers.Authorization = 'Bearer ' + token;
            }
            return config;
        }
    }
})();
