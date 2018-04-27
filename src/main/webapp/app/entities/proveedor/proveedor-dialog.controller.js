(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProveedorDialogController', ProveedorDialogController);

    ProveedorDialogController.$inject = ['CommonMethods','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proveedor', 'Company','$rootScope'];

    function ProveedorDialogController (CommonMethods,$timeout, $scope, $stateParams, $uibModalInstance, entity, Proveedor, Company,$rootScope) {
        var vm = this;

        vm.proveedor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        CommonMethods.validateNumbers();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.proveedor.id !== null) {
                Proveedor.update(vm.proveedor, onUpdateSuccess, onSaveError);
            } else {
                vm.proveedor.deleted = 0;
                vm.proveedor.companyId = $rootScope.companyId;
                Proveedor.save(vm.proveedor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            toastr["success"]("Se registró el proveedor correctamente");

            $scope.$emit('aditumApp:proveedorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onUpdateSuccess (result) {
            toastr["success"]("Se modificó el proveedor correctamente");

            $scope.$emit('aditumApp:proveedorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
