(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDialogController', ResidentDialogController);

    ResidentDialogController.$inject = ['$state','$timeout','$scope', '$rootScope', '$stateParams', 'CommonMethods','previousState', 'DataUtils','$q', 'entity', 'Resident', 'User', 'Company', 'House','Principal','companyUser'];

    function ResidentDialogController($state,$timeout,$scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q,entity, Resident, User, Company, House,Principal,companyUser) {
        var vm = this;
        console.log(companyUser);
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.clear = clear;
        vm.save = save;
        vm.user = entity;
        vm.users = User.query();
        vm.companies = Company.query();

        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        vm.success = null;
        vm.loginStringCount = 0;
        vm.SaveUserError = false;

        if(vm.resident.id !== null){
            vm.title = "Editar residente";
            vm.button = "Editar";
            var autorizadorStatus = vm.resident.isOwner;
            if(vm.resident.isOwner==1){
                 vm.resident.isOwner=true;
            }

        } else{
            vm.title = "Registrar residente";
            vm.button = "Registrar";
        }


        House.query({},onSuccessHouses);
        function onSuccessHouses(data, headers) {
            vm.houses = data;
             setTimeout(function() {
                $("#edit_resident_form").fadeIn(600);
             }, 200)
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resident.id !== null) {
                if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "") {
                    toastr["error"]("Debe ingresar un correo para asignar el residente como autorizador de filial.");
                } else if(autorizadorStatus==1 && vm.resident.isOwner==false){
                     updateAccount(0);
                } else if(autorizadorStatus==0 && vm.resident.isOwner==true){
                     if(vm.resident.userId!==null){
                         updateAccount(1);
                     } else{
                         createAccount(2);
                     }
                } else {
                    changeStatusIsOwner();
                    Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                }
             } else {
                if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "" ) {
                    toastr["error"]("Debe ingresar un correo para asignar el residente como autorizador de filial.");
                } else if(vm.resident.isOwner == 1){
                     createAccount(1);
                } else {
                     insertResident(null);
                }
            }
        }

        function changeStatusIsOwner(){
            if(vm.resident.isOwner==false){vm.resident.isOwner=0}else{vm.resident.isOwner=1}
        }
        function createAccount(opcion){
            var authorities = ["ROLE_USER"];
            vm.user.firstName =  vm.resident.name;
            vm.user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
            vm.user.email = vm.resident.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);
            User.save(vm.user, onSaveUser, onSaveLoginError);
            function onSaveUser (result) {
               if(opcion==1){
                   insertResident(result.id)
               }
               else if(opcion==2){
                     vm.resident.userId = result.id;
                     vm.resident.isOwner = 1;
                     Resident.update(vm.resident, onUpdateSuccess, onSaveError);
               }
               vm.isSaving = false;
            }

        }
         function updateAccount(status){
             User.getUserById({id: vm.resident.userId},onSuccess);
             function onSuccess(user, headers) {
                 user.id = vm.resident.userId;
                 user.activated = status;
                 user.firstName =  vm.resident.name;
                 user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
                 user.email = vm.resident.email;
                 User.update(user,onSuccessUser);
                 function onSuccessUser(data, headers) {
                    changeStatusIsOwner();
                    Resident.update(vm.resident, onUpdateSuccess, onSaveError);

                  }
              }

            }
            function onUpdateSuccess (result) {
                vm.isSaving = false;
                $state.go('resident');
                toastr["success"]("Se ha editado el residente correctamente.");
            }
        function insertResident(id){
            vm.resident.enabled = 1;
            vm.resident.companyId = 1;
            vm.resident.userId = id;
            if(vm.resident.isOwner){
            vm.resident.isOwner = 1;
            }else{
            vm.resident.isOwner = 0;
            }
            Resident.save(vm.resident, onSaveSuccess, onSaveError);
             function onSaveSuccess (result) {
                  vm.isSaving = false;
                  $state.go('resident');
                  toastr["success"]("Se ha registrado el residente correctamente.");
              }
        }

        function generateLogin(config){
              var firstletterFirstName = vm.resident.name.charAt(0);
              var firstletterSecondName = vm.resident.secondlastname.charAt(0);
              if(config==1){
              vm.loginStringCount = vm.loginStringCount + 1;
              return firstletterFirstName+vm.resident.lastname+firstletterSecondName+vm.loginStringCount;
              }
              return firstletterFirstName+vm.resident.lastname+firstletterSecondName;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        function onSaveLoginError () {
            vm.isSaving = false;
            vm.user.login = generateLogin(1);
            User.save(vm.user, onSaveUser, onSaveLoginError);
             function onSaveUser (result) {
                   $state.go('resident');

             }
        }

        vm.setImage = function ($file, resident) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        resident.image = base64Data;
                        resident.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
