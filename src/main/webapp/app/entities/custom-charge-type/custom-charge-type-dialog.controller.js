(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeDialogController', CustomChargeTypeDialogController);

    CustomChargeTypeDialogController.$inject = ['Modal', 'globalCompany', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomChargeType', 'Company', '$rootScope'];

    function CustomChargeTypeDialogController(Modal, globalCompany, $timeout, $scope, $stateParams, $uibModalInstance, entity, CustomChargeType, Company, $rootScope) {
        var vm = this;

        vm.customChargeType = entity;
        vm.clear = clear;
        vm.save = save;
        if (vm.customChargeType.id == null) {
            $rootScope.mainTitle = "Crear tipo de cuota"
        } else {
            $rootScope.mainTitle = "Editar tipo de cuota"
        }
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            if(vm.customChargeType.id!=null){
                Modal.confirmDialog("¿Está seguro que desea editar el tipo de cuota?","Modificará la categoría de las cuotas creadas con este tipo anteriormente",function(){
                    vm.isSaving = true;
                    vm.customChargeType.companyId = globalCompany.getId();
                    if (vm.customChargeType.id !== null) {
                        CustomChargeType.update(vm.customChargeType, onSaveSuccess, onSaveError);
                    } else {
                        CustomChargeType.save(vm.customChargeType, onSaveSuccess, onSaveError);
                    }
                })
            }else{
                Modal.confirmDialog("¿Está seguro que crear el tipo de cuota?","UNA VEZ CREADO NO PODRÁ ELIMINARSE",function(){
                    vm.isSaving = true;
                    vm.customChargeType.companyId = globalCompany.getId();
                    if (vm.customChargeType.id !== null) {
                        CustomChargeType.update(vm.customChargeType, onSaveSuccess, onSaveError);
                    } else {
                        CustomChargeType.save(vm.customChargeType, onSaveSuccess, onSaveError);
                    }
                })
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:customChargeTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
