(function() {
    'use strict';
    /* globals SockJS, Stomp */

    angular
        .module('aditumApp')
        .factory('JhiTrackerService', JhiTrackerService);

    JhiTrackerService.$inject = ['$rootScope', '$window', '$cookies', '$http', '$q', 'AuthServerProvider'];

    function JhiTrackerService ($rootScope, $window, $cookies, $http, $q, AuthServerProvider) {
        var stompClient = null;
        var subscribers = [];
        var listener = $q.defer();
        var listenerHomeService = $q.defer();
        var listenerEmergency = $q.defer();
        var connected = $q.defer();
        var alreadyConnectedOnce = false;

        var service = {
            connect: connect,
            disconnect: disconnect,
            receive: receive,
            sendActivity: sendActivity,
            subscribeToGetHomeServices: subscribeToGetHomeServices,
            receiveHomeService: receiveHomeService,
            sendHomeService: sendHomeService,
            subscribeToGetEmergencies:subscribeToGetEmergencies,
            receiveEmergency:receiveEmergency,
            reportEmergency:reportEmergency,
            subscribe: subscribe,
            unsubscribe: unsubscribe
        };

        return service;

        function connect () {
            //building absolute path so that websocket doesnt fail when deploying with a context path
            var loc = $window.location;
            var url = '//' + loc.host + loc.pathname + 'websocket/tracker';
            var authToken = AuthServerProvider.getToken();
            if(authToken){
                url += '?access_token=' + authToken;
            }
            var socket = new SockJS(url);
            stompClient = Stomp.over(socket);
            var stateChangeStart;
            var headers = {};
            stompClient.connect(headers, function() {
                connected.resolve('success');
                sendActivity();
                if (!alreadyConnectedOnce) {
                    stateChangeStart = $rootScope.$on('$stateChangeStart', function () {
                        sendActivity();
                    });
                    alreadyConnectedOnce = true;
                }
            });
            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(stateChangeStart) && stateChangeStart !== null){
                    stateChangeStart();
                }
            });
        }

        function disconnect () {
            if (stompClient !== null) {
                stompClient.disconnect();
                stompClient = null;
            }
        }

        function receive () {
            return listener.promise;
        }

        function sendActivity() {
            if (stompClient !== null && stompClient.connected) {
                stompClient
                    .send('/topic/activity',
                    {},
                    angular.toJson({'page': $rootScope.toState.name}));
            }
        }
        function subscribe () {
            connected.promise.then(function() {
                subscribers.push(stompClient.subscribe('/topic/tracker', function(data) {
                    listener.notify(angular.fromJson(data.body));
                }));
            }, null, null);
        }

//EMERGENCY WB
    function receiveEmergency () {
         return listenerEmergency.promise;
    }
    function reportEmergency(emergency) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/reportEmergency/'+$rootScope.companyId,
                      {},
                      angular.toJson(emergency));
              }
          }

        function subscribeToGetEmergencies() {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/Emergency/'+$rootScope.companyId, function(data) {
                             listenerEmergency.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END EMERGENCY WB





//HOME SERVICE WB
    function receiveHomeService () {
         return listenerHomeService.promise;
    }
    function sendHomeService(note) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/saveHomeService/'+$rootScope.companyId,
                      {},
                      angular.toJson(note));
              }
          }

        function subscribeToGetHomeServices () {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/HomeService/'+$rootScope.companyId, function(data) {
                             listenerHomeService.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END HOME SERVICE WB


//RESIDENT SERVICE WB
    function receiveResident () {
         return listenerHomeService.promise;
    }
    function sendResident(resident) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/saveResident/'+$rootScope.companyId,
                      {},
                      angular.toJson(resident));
              }
          }

        function subscribeToGetResidents () {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/resident/'+$rootScope.companyId, function(data) {
                             listenerHomeService.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END RESIDENT SERVICE WB


//VEHICLE SERVICE WB
    function receiveVehicle () {
         return listenerHomeService.promise;
    }
    function sendVehicle(vehicle) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/saveVehicle/'+$rootScope.companyId,
                      {},
                      angular.toJson(vehicle));
              }
          }

        function subscribeToGetVehicles () {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/vehicle/'+$rootScope.companyId, function(data) {
                             listenerHomeService.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END VEHICLE SERVICE WB

//VISITOR SERVICE WB
    function receiveVisitor () {
         return listenerHomeService.promise;
    }
    function sendVisitor(visitor) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/saveVisitor/'+$rootScope.companyId,
                      {},
                      angular.toJson(visitor));
              }
          }

        function subscribeToGetVisitors () {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/visitor/'+$rootScope.companyId, function(data) {
                             listenerHomeService.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END VISITOR SERVICE WB

//HOUSE SERVICE WB
    function receiveHouse () {
         return listenerHomeService.promise;
    }
    function sendHouse(house) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/saveHouse/'+$rootScope.companyId,
                      {},
                      angular.toJson(house));
              }
          }

        function subscribeToGetHouses () {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/house/'+$rootScope.companyId, function(data) {
                             listenerHomeService.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END HOUSE SERVICE WB

//DELETED ENTITY SERVICE WB
    function receiveDeletedEntity () {
         return listenerHomeService.promise;
    }
    function sendDeletedEntity(entity) {
              if (stompClient !== null && stompClient.connected) {
                  stompClient
                      .send('/topic/deleteEntity/'+$rootScope.companyId,
                      {},
                      angular.toJson(entity));
              }
          }

        function subscribeToGetDeletedEntities () {
                     connected.promise.then(function() {
                         subscribers.push(stompClient.subscribe('/topic/deletedEntity/'+$rootScope.companyId, function(data) {
                             listenerHomeService.notify(angular.fromJson(data.body));
                         }));
                     },null,null);
        }
//END HOUSE SERVICE WB


        function unsubscribe () {
            if (subscribers.length > 0) {
            angular.forEach(subscribers,function(sub,value){
             sub.unsubscribe();
            })
            }
            subscribers = [];
            listener = $q.defer();
            listenerHomeService = $q.defer();
        }
    }
})();
