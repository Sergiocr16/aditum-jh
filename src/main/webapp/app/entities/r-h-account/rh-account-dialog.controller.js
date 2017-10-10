(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RHAccountDialogController', RHAccountDialogController);

    RHAccountDialogController.$inject = ['CommonMethods','$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'RHAccount', 'User', 'Company','Principal','$state'];

    function RHAccountDialogController (CommonMethods,$timeout, $scope, $stateParams, $uibModalInstance, $q, entity, RHAccount, User, Company, Principal,$state) {
        var vm = this;

        vm.rHAccount = entity;
        vm.clear = clear;
        vm.save = save;
        vm.user = entity;
        vm.users = User.query();
        vm.companies = Company.query();
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loginStringCount = 0;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
        vm.rHAccount.enable = true;
            vm.isSaving = true;
            if (vm.rHAccount.id !== null) {
            vm.rHAccount.name = CommonMethods.capitalizeFirstLetter(vm.rHAccount.name);
            vm.rHAccount.lastname = CommonMethods.capitalizeFirstLetter(vm.rHAccount.lastname);
            vm.rHAccount.secondlastname = CommonMethods.capitalizeFirstLetter(vm.rHAccount.secondlastname);
            vm.rHAccount.enterprisename = CommonMethods.capitalizeFirstLetter(vm.rHAccount.enterprisename);
                RHAccount.update(vm.rHAccount, onSaveSuccess, onSaveError);
                updateAccount();
            } else {
                createAccount();
            }
        }

        function createAccount(){
            var authorities = ["ROLE_RH"];
            vm.user.firstName =  vm.rHAccount.name;
            vm.user.lastName = vm.rHAccount.lastname + ' ' + vm.rHAccount.secondlastname;
            vm.user.email = vm.rHAccount.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);

            User.save(vm.user, onSaveUser, onSaveLoginError);
        }
       function onSaveLoginError () {
            vm.isSaving = false;
            vm.user.login = generateLogin(1);
            User.save(vm.user, onSaveUser, onSaveLoginError);
             function onSaveUser (result) {
                     insertAdmin(result.id)
                      vm.isSaving = false;
             }
        }
         function onSaveUser (result) {
                        insertAdmin(result.id)
                        vm.isSaving = false;
          }
        function insertAdmin(id){

            vm.rHAccount.userId = id;
            RHAccount.save(vm.rHAccount, onSaveSuccess, onSaveError);
            function onSaveSuccess (result) {
                vm.isSaving = false;
                $uibModalInstance.close(result);
                toastr["success"]("Se ha registrado el usuario de recursos humanos correctamente.");
            }
        }


        function updateAccount(){
            User.getUserById({id: vm.rHAccount.userId},onSuccess);
            function onSuccess(user, headers) {
                user.id = vm.rHAccount.userId;
                user.activated = true;
                user.firstName =  vm.rHAccount.name;
                user.lastName = vm.rHAccount.lastname + ' ' + vm.rHAccount.secondlastname;
                user.email = vm.rHAccount.email;
                User.update(user,onSuccessUser);
                function onSuccessUser(data, headers) {
                    RHAccount.update(vm.rHAccount, onUpdateSuccess, onSaveError);
                }
            }

        }
        function onUpdateSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
            toastr["success"]("Se ha editado el usuario de recursos humanos correctamente.");
        }
        function generateLogin(config){

            var firstletterFirstName = vm.rHAccount.name.charAt(0);
            var firstletterSecondName = vm.rHAccount.secondlastname.charAt(0);
            if(config==1){
                vm.loginStringCount = vm.loginStringCount + 1;
                return firstletterFirstName+vm.rHAccount.lastname+firstletterSecondName+vm.loginStringCount;
            }
            return firstletterFirstName+vm.rHAccount.lastname+firstletterSecondName;
        }
        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:officerAccountstUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
