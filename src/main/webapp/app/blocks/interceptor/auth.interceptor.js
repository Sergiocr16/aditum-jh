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
        String.prototype.myStartsWith = function(str){
         if(this.indexOf(str)===0){
          return true;
         }else{
           return  false;
         }
        };
            /*jshint camelcase: false */
            config.headers = config.headers || {};
            if (config.url.myStartsWith(cloudinary.config().base_url)) {
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
