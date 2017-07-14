(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SecurityCompanyDialogController', SecurityCompanyDialogController);

    SecurityCompanyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SecurityCompany'];

    function SecurityCompanyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SecurityCompany) {
        var vm = this;

        vm.securityCompany = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.securityCompany.id !== null) {
                SecurityCompany.update(vm.securityCompany, onSaveSuccess, onSaveError);
            } else {
                SecurityCompany.save(vm.securityCompany, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:securityCompanyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
