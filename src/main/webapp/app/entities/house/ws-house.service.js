(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSHouse', WSHouse);

    WSHouse.$inject = ['StompManager','$rootScope'];

    function WSHouse(StompManager,$rootScope) {
        var SUBSCRIBE_TRACKER_URL = '/topic/house/';
        var SEND_ACTIVITY_URL = '/topic/sendHouse/';


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
