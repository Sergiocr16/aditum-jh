(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JhiHealthCheckController', JhiHealthCheckController);

    JhiHealthCheckController.$inject = ['JhiHealthService', '$uibModal','Principal','$rootScope'];

    function JhiHealthCheckController (JhiHealthService, $uibModal,Principal,$rootScope) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.updatingHealth = true;
        vm.getLabelClass = getLabelClass;
        vm.refresh = refresh;
        vm.showHealth = showHealth;
        vm.baseName = JhiHealthService.getBaseName;
        vm.subSystemName = JhiHealthService.getSubSystemName;
        $rootScope.active = "health";
        vm.refresh();

        function getLabelClass (statusState) {
            if (statusState === 'UP') {
                return 'label-success';
            } else {
                return 'label-danger';
            }
        }

        function refresh () {
            vm.updatingHealth = true;
            JhiHealthService.checkHealth().then(function (response) {
                vm.healthData = JhiHealthService.transformHealthData(response);
                vm.updatingHealth = false;
            }, function (response) {
                vm.healthData =  JhiHealthService.transformHealthData(response.data);
                vm.updatingHealth = false;
            });
        }

        function showHealth (health) {
            $uibModal.open({
                templateUrl: 'app/admin/health/health.modal.html',
                controller: 'HealthModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    currentHealth: function() {
                        return health;
                    },
                    baseName: function() {
                        return vm.baseName;
                    },
                    subSystemName: function() {
                        return vm.subSystemName;
                    }

                }
            });
        }

    }
})();
