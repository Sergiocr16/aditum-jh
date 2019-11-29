(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSEmergency', WSEmergency);

    WSEmergency.$inject = ['StompManager','globalCompany'];

    function WSEmergency(StompManager,globalCompany) {
        var SUBSCRIBE_TRACKER_URL = '/topic/emergency/';
        var SEND_ACTIVITY_URL = '/topic/reportEmergency/';
        var SUBSCRIBE_ATTEND_TRACKER_URL = '/topic/emergencyAttended/';
        var SEND_ACTIVITY_ATTEND_URL = '/topic/attendEmergency/';


        var service = {
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            receiveAttented: receiveAttented,
            sendActivityAttended: sendActivityAttended,
            subscribeAttended: subscribeAttended,
            unsubscribeAttended: unsubscribeAttended,

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

        function receiveAttented (code) {
                return StompManager.getListener(SUBSCRIBE_ATTEND_TRACKER_URL + code);
        }

        function sendActivityAttended(code,entity) {
            StompManager.send(SEND_ACTIVITY_ATTEND_URL + code, entity);
        }

        function subscribeAttended(code) {
            StompManager.subscribe(SUBSCRIBE_ATTEND_TRACKER_URL + code);
        }

        function unsubscribeAttended(code) {
            StompManager.unsubscribe(SUBSCRIBE_ATTEND_TRACKER_URL + code);
        }
    }
})();
