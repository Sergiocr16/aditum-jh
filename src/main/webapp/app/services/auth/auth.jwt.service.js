(function() {
    'use strict';

    angular
        .module('aditumApp')
        .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', '$sessionStorage', '$q'];

    function AuthServerProvider ($http, $localStorage, $sessionStorage, $q) {
        var service = {
            getToken: getToken,
            login: login,
            loginWithToken: loginWithToken,
            storeAuthenticationToken: storeAuthenticationToken,
            logout: logout,
            loginBioStar:loginBioStar
        };

        return service;

        function getToken () {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }

        function login (credentials) {

            var data = {
                username: credentials.username,
                password: credentials.password,
                rememberMe: credentials.rememberMe
            };
            return $http.post('api/authenticate', data).success(authenticateSuccess);

            function authenticateSuccess (data, status, headers) {
             console.log(headers('Authorization'))
                var bearerToken = headers('Authorization');
                if (angular.isDefined(bearerToken) && bearerToken.slice(0, 7) === 'Bearer ') {
                    var jwt = bearerToken.slice(7, bearerToken.length);
                     console.log(credentials.rememberMe)
                    service.storeAuthenticationToken(jwt, credentials.rememberMe);
                    console.log(jwt)
                    return jwt;
                }
            }
        }

        function loginBioStar (credentials) {

            return $http.post('https://api.biostar2.com/v1/login', JSON.stringify(credentials)).success(authenticateSuccess);

            function authenticateSuccess (data,config,headers) {

                var bearerToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaC1hZG1pbiIsImF1dGgiOiJST0xFX0FETUlOIiwiZXhwIjoxNTIwMDk0MjYyfQ.MD26WvnxWkzWxX-2tEsUpBHxm1BdMeiw1i5QP65LjdwDHv7QG9gPRuRVMoCuLsYhZEdstRNe8Oxa-mr_qx22_A"

                if (angular.isDefined(bearerToken)  && bearerToken.slice(0, 7) === 'Bearer ') {
                    var jwt = bearerToken;
                    service.storeAuthenticationToken('s%3AhezVWoh47TAgm4uCldKfeWCI6YLYfc88.YWvFv0qeTYJ9zcTW6I0WphR5i9xriLEpZBWNwwyyAZ0', true);
                    return jwt;
                }
            }
        }

        function loginWithToken(jwt, rememberMe) {
            var deferred = $q.defer();

            if (angular.isDefined(jwt)) {
                this.storeAuthenticationToken(jwt, rememberMe);
                deferred.resolve(jwt);
            } else {
                deferred.reject();
            }

            return deferred.promise;
        }

        function storeAuthenticationToken(jwt, rememberMe) {
            if(rememberMe){
            console.log(jwt)
                $localStorage.authenticationToken = jwt;
            } else {
                $sessionStorage.authenticationToken = jwt;
            }
        }

        function logout () {
            delete $localStorage.authenticationToken;
            delete $sessionStorage.authenticationToken;
        }
    }
})();
