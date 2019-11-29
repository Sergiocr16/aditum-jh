(function () {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSNote', WSNote);

    WSNote.$inject = ['StompManager', 'globalCompany'];

    function WSNote(StompManager, globalCompany) {
        var SUBSCRIBE_TRACKER_URL = '/topic/homeService/';
        var SEND_ACTIVITY_URL = '/topic/sendHomeService/';

        var subscribed = false;

        var service = {
            receive: receive,
            sendActivity: sendActivity,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
        };
        return service;

        function receive() {
            return StompManager.getListener(SUBSCRIBE_TRACKER_URL + globalCompany.getId());
        }

        function sendActivity(entity) {
            StompManager.send(SEND_ACTIVITY_URL + globalCompany.getId(), entity);
        }

        function subscribe(companyId) {
            if (subscribed==false) {
                StompManager.subscribe(SUBSCRIBE_TRACKER_URL + companyId);
                subscribed = true;
            }
        }

        function unsubscribe(companyId) {
            StompManager.unsubscribe(SUBSCRIBE_TRACKER_URL + companyId);
            subscribed = false;
        }
    }
})();
