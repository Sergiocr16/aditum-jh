(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('JhiTrackerService', JhiTrackerService);

    JhiTrackerService.$inject = ['$rootScope', 'StompManager'];

    function JhiTrackerService ($rootScope, StompManager) {
        var SUBSCRIBE_TRACKER_URL = '/topic/tracker';
        var SEND_ACTIVITY_URL ='/topic/activity';

        var alreadyConnectedOnce = false;

        var service = {
            connect: connect,
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            subscribeToGetEmergencies: subscribeToGetEmergencies
        };

        return service;

        function connect () {
            var stateChangeStart;
            StompManager.connect().then(function() {
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


        function receive () {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL);
        }

        function sendActivity() {
            StompManager.send(SEND_ACTIVITY_URL, {'page': $rootScope.toState.name});
        }

        function subscribe () {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL);
        }

        function unsubscribe () {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL);
        }

        function subscribeToGetEmergencies () {
            StompManager.subscribe('/topic/Emergency/'+$rootScope.companyId);
        }
    }
})();
////EMERGENCY WB
//    function receiveEmergency () {
//         return listenerHomeService.promise;
//    }
//    function reportEmergency(emergency) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/reportEmergency/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(emergency));
//              }
//          }
//
//        function subscribeToGetEmergencies() {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/Emergency/'+$rootScope.companyId, function(data) {
//                             listenerEmergency.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END EMERGENCY WB
//
//
//
//
//
////HOME SERVICE WB
//    function receiveHomeService () {
//         return listenerHomeService.promise;
//    }
//    function sendHomeService(note) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/saveHomeService/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(note));
//              }
//          }
//
//        function subscribeToGetHomeServices () {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/HomeService/'+$rootScope.companyId, function(data) {
//                             listenerHomeService.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END HOME SERVICE WB
//
//
////RESIDENT SERVICE WB
//    function receiveResident () {
//         return listenerHomeService.promise;
//    }
//    function sendResident(resident) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/saveResident/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(resident));
//              }
//          }
//
//        function subscribeToGetResidents () {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/resident/'+$rootScope.companyId, function(data) {
//                             listenerHomeService.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END RESIDENT SERVICE WB
//
//
////VEHICLE SERVICE WB
//    function receiveVehicle () {
//         return listenerHomeService.promise;
//    }
//    function sendVehicle(vehicle) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/saveVehicle/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(vehicle));
//              }
//          }
//
//        function subscribeToGetVehicles () {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/vehicle/'+$rootScope.companyId, function(data) {
//                             listenerHomeService.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END VEHICLE SERVICE WB
//
////VISITOR SERVICE WB
//    function receiveVisitor () {
//         return listenerHomeService.promise;
//    }
//    function sendVisitor(visitor) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/saveVisitor/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(visitor));
//              }
//          }
//
//        function subscribeToGetVisitors () {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/visitor/'+$rootScope.companyId, function(data) {
//                             listenerHomeService.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END VISITOR SERVICE WB
//
////HOUSE SERVICE WB
//    function receiveHouse () {
//         return listenerHomeService.promise;
//    }
//    function sendHouse(house) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/saveHouse/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(house));
//              }
//          }
//
//        function subscribeToGetHouses () {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/house/'+$rootScope.companyId, function(data) {
//                             listenerHomeService.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END HOUSE SERVICE WB
//
////DELETED ENTITY SERVICE WB
//    function receiveDeletedEntity () {
//         return listenerHomeService.promise;
//    }
//    function sendDeletedEntity(entity) {
//              if (stompClient !== null && stompClient.connected) {
//                  stompClient
//                      .send('/topic/deleteEntity/'+$rootScope.companyId,
//                      {},
//                      angular.toJson(entity));
//              }
//          }
//
//        function subscribeToGetDeletedEntities () {
//                     connected.promise.then(function() {
//                         subscribers.push(stompClient.subscribe('/topic/deletedEntity/'+$rootScope.companyId, function(data) {
//                             listenerHomeService.notify(angular.fromJson(data.body));
//                         }));
//                     },null,null);
//        }
////END HOUSE SERVICE WB


