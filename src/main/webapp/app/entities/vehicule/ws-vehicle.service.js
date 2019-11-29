(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSVehicle', WSVehicle);

    WSVehicle.$inject = ['StompManager','globalCompany'];

    function WSVehicle(StompManager,globalCompany) {
        var SUBSCRIBE_TRACKER_URL = '/topic/vehicle/';
        var SEND_ACTIVITY_URL = '/topic/sendVehicle/';


        var service = {
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
        };

        return service;

        function receive () {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + globalCompany.getId());
        }

        function sendActivity(entity) {
            StompManager.send(SEND_ACTIVITY_URL + globalCompany.getId(), entity);
        }

        function subscribe (companyId) {
            StompManager.subscribe(SUBSCRIBE_TRACKER_URL + companyId);
        }

        function unsubscribe (companyId) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + companyId);
        }
    }
})();
