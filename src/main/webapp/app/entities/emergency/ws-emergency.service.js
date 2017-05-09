(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSEmergency', WSEmergency);

    WSEmergency.$inject = ['StompManager','$rootScope'];

    function WSEmergency(StompManager,$rootScope) {
        var SUBSCRIBE_TRACKER_URL = '/topic/emergency/';
        var SEND_ACTIVITY_URL = '/topic/reportEmergency/';


        var service = {
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
        };

        return service;

        function receive () {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + $rootScope.companyId);
        }

        function sendActivity(entity) {
            StompManager.send(SEND_ACTIVITY_URL + $rootScope.companyId, entity);
        }

        function subscribe (companyId) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + companyId);
        }

        function unsubscribe (companyId) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + companyId);
        }
    }
})();
