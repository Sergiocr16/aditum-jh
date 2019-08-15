(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('WSVisitorInvitation', WSVisitorInvitation);

    WSVisitorInvitation.$inject = ['StompManager','globalCompany'];

    function WSVisitorInvitation(StompManager,globalCompany) {
        var SUBSCRIBE_TRACKER_URL = '/topic/visitor-invitation/';
        var SEND_ACTIVITY_URL = '/topic/sendVisitorInvitation/';

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
