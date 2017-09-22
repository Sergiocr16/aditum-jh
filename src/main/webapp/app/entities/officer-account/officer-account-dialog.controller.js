(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountDialogController', OfficerAccountDialogController);

    OfficerAccountDialogController.$inject = ['$rootScope','$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OfficerAccount', 'Company', 'User'];

    function OfficerAccountDialogController ($rootScope,$timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OfficerAccount, Company, User) {
        var vm = this;

        vm.officerAccount = entity;
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
            if (vm.officerAccount.id !== null) {
                updateAccount();
                OfficerAccount.update(vm.officerAccount, onSaveSuccess, onSaveError);
            } else {
             createAccount();
            }
        }

        function createAccount(){
            var authorities = ["ROLE_OFFICER"];
            vm.user.firstName = vm.officerAccount.name;
            vm.user.lastName = vm.officerAccount.name;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            User.save(vm.user, onSaveUser, onSaveLoginError);

        }
            function onSaveUser (result) {
                insertOfficerAccount(result.id)
                vm.isSaving = false;
            }
            function onSaveLoginError (error) {
                console.log(error);
                toastr["error"]("El nombre de usuario ingresado ya existe");
            }
        function insertOfficerAccount(id){
                console.log($stateParams.companyId);
                vm.officerAccount.companyId = $stateParams.companyId;
                vm.officerAccount.userId = id;
                OfficerAccount.save(vm.officerAccount, onSaveSuccess, onSaveError);
                function onSaveSuccess (result) {
                    vm.isSaving = false;
                    $uibModalInstance.close(result);
                    toastr["success"]("Se ha registrado el oficial correctamente.");

                }
            }
        function onSaveSuccess (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
