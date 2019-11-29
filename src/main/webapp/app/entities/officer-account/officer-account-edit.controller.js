(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerAccountEditController', OfficerAccountEditController);

    OfficerAccountEditController.$inject = ['$uibModalInstance','CommonMethods','$state','$rootScope','$timeout', '$scope', '$stateParams',  '$q', 'entity', 'OfficerAccount', 'Company', 'User','Principal'];

    function OfficerAccountEditController ($uibModalInstance,CommonMethods,$state,$rootScope,$timeout, $scope, $stateParams,  $q, entity, OfficerAccount, Company, User,Principal) {
        var vm = this;
         CommonMethods.validateSpecialCharacters();
        vm.officerAccount = entity;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.save = save;
        vm.clear = clear;

        vm.companyId = $stateParams.companyId;
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        setTimeout(function(){
                  if($stateParams.id.slice(0,7)!="U2FsdGV"  || $stateParams.companyId.slice(0,7)!="U2FsdGV"){
                  $uibModalInstance.dismiss('cancel');
                   $state.go('officerAccounts')
                  }
        },500)

       getUser();
       function getUser(){
            User.getUserById({ id: vm.officerAccount.userId}).$promise.then(onSuccess);
       }
        function onSuccess (user) {
            vm.user = user;
           }
      function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function save () {
            CommonMethods.waitingMessage();
            updateAccount();

        }

        function updateAccount(){
                 vm.user.firstName =  vm.officerAccount.name;
                 vm.user.lastName = vm.officerAccount.name;
                 User.update(vm.user,onSuccessUser,onErrorUpdate);
                 function onSuccessUser(data, headers) {
                    OfficerAccount.update(vm.officerAccount, onUpdateSuccess, onSaveError);

                  }


            }
        function onUpdateSuccess (result) {
                vm.isSaving = false;
                  bootbox.hideAll();
                   $scope.$emit('aditumApp:rHAccountUpdate', result);
                             $uibModalInstance.close(result);
                toastr["success"]("Se ha editado el oficial correctamente.");
            }


            function onErrorUpdate (error) {
            console.log(error)
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

  function onSaveError (error) {


          bootbox.hideAll();
}



    }
})();
