(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargesToPayReportController', ChargesToPayReportController);

    ChargesToPayReportController.$inject = ['$rootScope', '$state', 'Charge', 'globalCompany', 'Company', 'CommonMethods', 'AlertService','$scope'];

    function ChargesToPayReportController($rootScope, $state, Charge, globalCompany, Company, CommonMethods, AlertService,$scope) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.transition = transition;
        $rootScope.active = "reporteCuotasPorPagar";
        vm.loadAll = loadAll;
        vm.final_time = new Date();
        vm.chargeType = 5;
        vm.companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());

        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident-detail', {
                id: encryptedId
            })
        }
        vm.exportActions = {
            downloading: false,
            printing: false,
        };
        vm.loadAll();
        vm.showYearDefaulter = function () {
            vm.loadDefaulters(vm.yearDefaulter)
        }
        vm.print = function () {
            vm.exportActions.printing = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.printing = false;
                })
            }, 7000)
            printJS({
                printable: 'api/charges/chargesToPay/file/'+moment(vm.final_time).format()+'/'+vm.chargeType+'/byCompany/'+globalCompany.getId(),
                type: 'pdf',
                modalMessage: "Obteniendo reporte de cuotas por cobrar"
            })
        }
        vm.download = function () {
            vm.exportActions.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.exportActions.downloading = false;
                })
            }, 7000)
        };
        vm.getCategory = function (type) {
            switch (type) {
                case 1:
                    return "MANTENIMIENTO"
                    break;
                case 2:
                    return "EXTRAORDINARIA"
                    break;
                case 3:
                    return "ÁREAS COMUNES"
                    break;
            }
        }

        function loadAll() {
            vm.isReady = false;
            vm.finalTimeFormatted = moment(vm.final_time).format();
            vm.companyId = globalCompany.getId();
            if (vm.chargeType == 5) {
                vm.filtering = false;
            } else {
                vm.filtering = true;
            }
            vm.chargeTypeSetted = vm.chargeType;
            Charge.findChargesToPayReport({
                final_time:  vm.finalTimeFormatted,
                companyId: vm.companyId ,
                type: vm.chargeType
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data) {
                vm.report = data;
                console.log(vm.report)
                Company.get({id: globalCompany.getId()}).$promise.then(function (result) {
                    vm.isReady = true;
                    vm.companyName = result.name;
                });
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
