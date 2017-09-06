(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountDialogController', OfficerAccountDialogController);

    OfficerAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OfficerAccount', 'Company', 'User'];

    function OfficerAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OfficerAccount, Company, User) {
        var vm = this;

        vm.officerAccount = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.officerAccount.id !== null) {
                OfficerAccount.update(vm.officerAccount, onSaveSuccess, onSaveError);
            } else {
                OfficerAccount.save(vm.officerAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:officerAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
