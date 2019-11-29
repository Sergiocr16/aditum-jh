(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JhiTrackerController', JhiTrackerController);

    JhiTrackerController.$inject = ['$cookies', '$http', 'JhiTrackerService','Principal','$rootScope'];

    function JhiTrackerController ($cookies, $http, JhiTrackerService,Principal, $rootScope) {
        // This controller uses a Websocket connection to receive user activities in real-time.
        var vm = this;
        $rootScope.active = "tracker"
        vm.activities = [];
        vm.isAuthenticated = Principal.isAuthenticated;
        JhiTrackerService.receive().then(null, null, function(activity) {
            showActivity(activity);
        });

        function showActivity(activity) {
            var existingActivity = false;
            for (var index = 0; index < vm.activities.length; index++) {
                if(vm.activities[index].sessionId === activity.sessionId) {
                    existingActivity = true;
                    if (activity.page === 'logout') {
                        vm.activities.splice(index, 1);
                    } else {
                        vm.activities[index] = activity;
                    }
                }
            }
            if (!existingActivity && (activity.page !== 'logout')) {
                vm.activities.push(activity);
            }
        }

    }
})();
