(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RHAccountDialogController', RHAccountDialogController);

    RHAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'RHAccount', 'User', 'Company','Principal'];

    function RHAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, RHAccount, User, Company, Principal) {
        var vm = this;

        vm.rHAccount = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.companies = Company.query();
            vm.isAuthenticated = Principal.isAuthenticated;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rHAccount.id !== null) {
                RHAccount.update(vm.rHAccount, onSaveSuccess, onSaveError);
            } else {
                RHAccount.save(vm.rHAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:rHAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
