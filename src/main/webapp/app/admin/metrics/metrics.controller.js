(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JhiMetricsMonitoringController', JhiMetricsMonitoringController);

    JhiMetricsMonitoringController.$inject = ['$scope','JhiMetricsService', '$uibModal','Principal'];

    function JhiMetricsMonitoringController ($scope, JhiMetricsService, $uibModal,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.metrics = {};
        vm.refresh = refresh;
        vm.refreshThreadDumpData = refreshThreadDumpData;
        vm.servicesStats = {};
        vm.updatingMetrics = true;

        vm.refresh();

        $scope.$watch('vm.metrics', function (newValue) {
            vm.servicesStats = {};
            angular.forEach(newValue.timers, function (value, key) {
                if (key.indexOf('web.rest') !== -1 || key.indexOf('service') !== -1) {
                    vm.servicesStats[key] = value;
                }
            });

        });

        function refresh () {
            vm.updatingMetrics = true;
            JhiMetricsService.getMetrics().then(function (promise) {
                vm.metrics = promise;
                vm.updatingMetrics = false;
            }, function (promise) {
                vm.metrics = promise.data;
                vm.updatingMetrics = false;
            });
        }

        function refreshThreadDumpData () {
            JhiMetricsService.threadDump().then(function(data) {
                $uibModal.open({
                    templateUrl: 'app/admin/metrics/metrics.modal.html',
                    controller: 'JhiMetricsMonitoringModalController',
                    controllerAs: 'vm',
                    size: 'lg',
                    resolve: {
                        threadDump: function() {
                            return data;
                        }

                    }
                });
            });
        }


    }
})();
