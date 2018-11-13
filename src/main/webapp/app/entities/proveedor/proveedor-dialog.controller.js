(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProveedorDialogController', ProveedorDialogController);

    ProveedorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proveedor', 'Company', '$rootScope', 'CommonMethods', 'globalCompany', 'Modal'];

    function ProveedorDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Proveedor, Company, $rootScope, CommonMethods, globalCompany, Modal) {
        var vm = this;
        $rootScope.active = "proovedores";
        vm.proveedor = entity;
        if (vm.proveedor.id == null) {
            $rootScope.mainTitle = "Crear proveedor";
        } else {
            $rootScope.mainTitle = "Editar proveedor";
        }
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();

        function save() {
            var action;
            if (vm.proveedor.id == null) {
                action = "registrar";
            } else {
                action = "editar";
            }
            Modal.confirmDialog("¿Está seguro que desea "+action+" este proveedor?","",function(){
                Modal.showLoadingBar();
                vm.isSaving = true;
                if (vm.proveedor.id !== null) {
                    Proveedor.update(vm.proveedor, onUpdateSuccess, onSaveError);
                } else {
                    vm.proveedor.deleted = 0;
                    vm.proveedor.companyId = globalCompany.getId();
                    Proveedor.save(vm.proveedor, onSaveSuccess, onSaveError);
                }
            })

        }

        function onSaveSuccess(result) {
            Modal.toast("Se registró el proveedor correctamente");
            Modal.hideLoadingBar();
            $scope.$emit('aditumApp:proveedorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onUpdateSuccess(result) {
            Modal.toast("Se modificó el proveedor correctamente");
            Modal.hideLoadingBar();

            $scope.$emit('aditumApp:proveedorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
