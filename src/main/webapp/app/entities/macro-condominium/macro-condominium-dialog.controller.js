(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroCondominiumDialogController', MacroCondominiumDialogController);

    MacroCondominiumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MacroCondominium', 'Company'];

    function MacroCondominiumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MacroCondominium, Company) {
        var vm = this;

        vm.macroCondominium = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.macroCondominium.id !== null) {
                MacroCondominium.update(vm.macroCondominium, onSaveSuccess, onSaveError);
            } else {
                MacroCondominium.save(vm.macroCondominium, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:macroCondominiumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
