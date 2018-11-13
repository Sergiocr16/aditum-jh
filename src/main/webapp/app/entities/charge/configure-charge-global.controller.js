(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ConfigureChargeGlobalController', ConfigureChargeGlobalController);

    ConfigureChargeGlobalController.$inject = ['$uibModalInstance', 'House', '$rootScope', '$scope', 'globalCompany','Modal'];

    function ConfigureChargeGlobalController($uibModalInstance, House, $rootScope, $scope, globalCompany,Modal) {
        var vm = this;

        vm.clear = clear;
        vm.isReady = false;
        vm.isSaving = false;
        $rootScope.mainTitle = "Configurar cuota global"
        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.save = function () {
            loadAll();
        }

        vm.confirm = function () {
            Modal.confirmDialog("¿Está seguro que desea cambiar la cuota de manera global?","",function(){
                vm.save();
            })
        }

        function loadAll() {
            vm.isSaving = true;
            House.query({
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                Modal.showLoadingBar()
                vm.houses = data;
                vm.cont = 0;
                angular.forEach(vm.houses, (function (house, key) {
                    house.due = vm.ammount;
                    House.update(house, function (result) {
                        vm.cont++;
                        if (vm.houses.length == vm.cont) {
                            $scope.$emit('aditumApp:globaChargeUpdate', result);
                            $uibModalInstance.close(result);
                            vm.isSaving = false;
                            Modal.hideLoadingBar();
                            Modal.toast("Se modificó la cuota global correctamente.")
                        }
                    })
                }))

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }
})();
