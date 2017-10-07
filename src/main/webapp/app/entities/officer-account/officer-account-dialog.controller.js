(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountDialogController', OfficerAccountDialogController);

    OfficerAccountDialogController.$inject = ['CommonMethods','$state','$rootScope','$timeout', '$scope', '$stateParams',  '$q', 'entity', 'OfficerAccount', 'Company', 'User','Principal','MultiCompany'];

    function OfficerAccountDialogController (CommonMethods,$state,$rootScope,$timeout, $scope, $stateParams,  $q, entity, OfficerAccount, Company, User,Principal,MultiCompany) {
        var vm = this;
        vm.officerAccount = entity;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.save = save;
        CommonMethods.validateSpecialCharacters();
        vm.companyId = CommonMethods.decryptIdUrl($stateParams.companyId)
        vm.clear = clear;
        function clear () {
         $state.go('officerAccounts', {
               companyId: $stateParams.companyId
           })
        }


        function loadAll () {

        }
        setTimeout(function(){
             var exist = 0;
               if($rootScope.contextLiving == "Dios de Aditum"){
                }else{
                   angular.forEach($rootScope.companyUser.companies, function(value, key) {
                      if(value.id == vm.companyId){
                        exist++;
                      }
                    });
                    if(exist == 0){
                    if($rootScope.contextLiving == "Dios de Aditum"){
                             $state.go('company')
                            }else{
                             $state.go('company-rh')
                            }
                 }
                }

        },500)

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save () {
            CommonMethods.waitingMessage();
            createAccount();
        }

        function createAccount(){
            var authorities = ["ROLE_OFFICER"];
            vm.user.firstName = vm.officerAccount.name;
            vm.user.lastName = vm.officerAccount.name;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            User.save(vm.user, onSaveUser, onInsertError);

        }
            function onSaveUser (result) {

                insertOfficerAccount(result.id)
                vm.isSaving = false;
            }
            function onInsertError (error) {
                console.log(error.data.login);
                switch(error.data.login){
                case "emailexist":
                      toastr["error"]("El correo electrónico ingresado ya existe.");
                break;
                 case "userexist":
                      toastr["error"]("El nombre de usuario ingresado ya existe.");
                break;
                }
                bootbox.hideAll();

            }
        function insertOfficerAccount(id){
                vm.officerAccount.companyId =  vm.companyId;
                vm.officerAccount.userId = id;
                vm.officerAccount.enable = 1;
                OfficerAccount.save(vm.officerAccount, onSaveSuccess, onSaveError);
                function onSaveSuccess (result) {
                    vm.isSaving = false;
                    $state.go('officerAccounts', {companyId: vm.companyId});
                    bootbox.hideAll();
                    toastr["success"]("Se ha registrado el oficial correctamente.");

                }
            }
        function onSaveSuccess (result) {

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

      vm.disableEnabledAdmin= function(officerAccount) {

            var correctMessage;
            if (officerAccount.enable==1) {
                correctMessage = "¿Está seguro que desea deshabilitar la cuenta " + officerAccount.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar la cuenta " + officerAccount.name + "?";
            }
            bootbox.confirm({

                message: correctMessage,

                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        CommonMethods.waitingMessage();
                        OfficerAccount.get({id: officerAccount.id}).$promise.then(onSuccessGetAccount);
                    }
                }
            });
        };

        function onSuccessGetAccount (result) {
            enabledDisabledAccount(result);
        }

        function enabledDisabledAdmin(officerAccount){
            if(officerAccount.enable==1){
                officerAccount.enable = 0;
            } else {
                officerAccount.enable = 1;
            }
            OfficerAccount.update(officerAccount, onSuccessDisabledAdmin);
        }

        function onSuccessDisabledAdmin(data, headers) {

            User.getUserById({
                id: data.userId
            }, onSuccessGetDisabledUser);

        }
        function onSuccessGetDisabledUser(data, headers) {
            if(data.activated==1){
                data.activated = 0;
            } else {
                data.activated = 1;
            }

            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {
                toastr["success"]("Se ha modificado el estado el admin correctamente.");
                bootbox.hideAll();
                loadAll();
            }
        }
    }
})();
