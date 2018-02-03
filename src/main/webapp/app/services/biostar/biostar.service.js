
(function() {
    'use strict';
//angular.module('aditumApp')
//           .config(function($httpProvider) {
//               $httpProvider.defaults.withCredentials = true;
//           });
    angular
        .module('aditumApp')
        .factory('BioStar', BioStar);

    BioStar.$inject = ['$resource','$localStorage','$http','$cookieStore'];

    function BioStar ($resource,$localStorage,$http,$cookieStore) {
//return {
//        login: function(okCallback, koCallback){
//            $http({
//                method: 'POST',
//                url: 'https://api.biostar2.com/v1/login',
//            }).success(okCallback).error(koCallback);
//        },
//        lockDoor: function(okCallback, koCallback){
//            $http({
//                method: 'POST',
//                url: 'https://api.biostar2.com/v1/doors/:door_id/lock',
//                params: {door_id:'@door_id'},
//                headers: {'Authorization': $cookieStore.get('token')}
//            }).success(okCallback).error(koCallback);
//        }
//    }
        var service = $resource('https://api.biostar2.com/v1/login', {}, {
            'login': { method: 'POST',url:'https://api.biostar2.com/v1/login', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            },
          'logout': { method: 'POST',url:'https://api.biostar2.com/v1/logout', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            },
            'lockDoor': { method: 'POST',url:'https://api.biostar2.com/v1/doors/:door_id/lock',
             params: {door_id:'@door_id',},
             withCredentials:true,
              isArray: false,
                    interceptor: {
                        response: function(response) {
                            // expose response
                            return response;
                        }
                    }
                },

        });

        return service;
    }
})();
