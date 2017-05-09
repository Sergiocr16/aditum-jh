(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSResident', WSResident);

    WSResident.$inject = ['StompManager','$rootScope'];

    function WSResident(StompManager,$rootScope) {
        var SUBSCRIBE_TRACKER_URL = '/topic/resident/';
        var SEND_ACTIVITY_URL = '/topic/sendResident/';


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
