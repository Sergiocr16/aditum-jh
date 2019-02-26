(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ActivosFijosDialogController', ActivosFijosDialogController);

    ActivosFijosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActivosFijos', 'Company','globalCompany','Modal','$rootScope'];

    function ActivosFijosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActivosFijos, Company,globalCompany,Modal,$rootScope) {
        var vm = this;
        $rootScope.active = "activosFijos";
        vm.activosFijos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        if (vm.activosFijos.id !== null) {
            vm.mainTitle="Editar activo fijo";

        } else {
            vm.mainTitle="Registrar activo fijo";
        }
        function save () {
            vm.isSaving = true;
            if (vm.activosFijos.id !== null) {
                Modal.showLoadingBar();
                ActivosFijos.update(vm.activosFijos, onUpdateSuccess, onSaveError);
            } else {
                vm.activosFijos.companyId = globalCompany.getId();
                vm.activosFijos.deleted = 1;
                Modal.showLoadingBar();
                ActivosFijos.save(vm.activosFijos, onSaveSuccess, onSaveError);
            }

        }

        function onUpdateSuccess (result) {
            Modal.hideLoadingBar();
            Modal.toast("Se ha actualizado el activo fijo correctamente");
            $scope.$emit('aditumApp:activosFijosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function onSaveSuccess (result) {
            Modal.hideLoadingBar();
            Modal.toast("Se ha registrado el activo fijo correctamente");
            $scope.$emit('aditumApp:activosFijosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            Modal.toast("Ha ocurrido un error inesperado");
        }


    }
})();
