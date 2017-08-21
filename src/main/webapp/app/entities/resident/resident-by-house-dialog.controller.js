(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseDialogController', ResidentByHouseDialogController);

    ResidentByHouseDialogController.$inject = ['$state','$timeout','$scope', '$rootScope', '$stateParams', 'CommonMethods','previousState', 'DataUtils','$q', 'entity', 'Resident', 'User', 'Company', 'House','Principal','companyUser','WSResident'];

    function ResidentByHouseDialogController($state,$timeout,$scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q,entity, Resident, User, Company, House,Principal,companyUser,WSResident) {
          $rootScope.active = "residentsHouses";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;
        vm.temporalIndentification = vm.resident.identificationnumber;
        vm.success = null;
        vm.loginStringCount = 0;
        vm.SaveUserError = false;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        Principal.identity().then(function(account) {
            vm.account = account;
        });
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


        House.query({companyId: $rootScope.companyId}).$promise.then(onSuccessHouses);
        function onSuccessHouses(data, headers) {
            vm.houses = data;
             setTimeout(function() {
                $("#edit_resident_form").fadeIn(600);
             }, 200)
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function save () {
            vm.isSaving = true;
             vm.resident.name = CommonMethods.capitalizeFirstLetter(vm.resident.name);
             vm.resident.lastname = CommonMethods.capitalizeFirstLetter(vm.resident.lastname);
             vm.resident.secondlastname = CommonMethods.capitalizeFirstLetter(vm.resident.secondlastname);
              vm.resident.isOwner = 0;
             if(vm.resident.id!==null){
               if(vm.temporalIndentification!==vm.resident.identificationnumber){
                   Resident.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.resident.identificationnumber},alreadyExist,allClearUpdate)
                    function alreadyExist(data){
                     toastr["error"]("La cédula ingresada ya existe.");
                   }
                } else {
                    if(vm.resident.isOwner == true){
                          vm.resident.isOwner = 1;
                      } else {
                           vm.resident.isOwner = 0;
                      }
                      Resident.update(vm.resident, onSuccess, onSaveError);
                }

             } else{
                Resident.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.resident.identificationnumber},alreadyExist,allClearInsert)
                     function alreadyExist(data){
                      toastr["error"]("La cédula ingresada ya existe.");
                    }



             }

        }
            function allClearInsert(){
                     CommonMethods.waitingMessage();
                     vm.resident.enabled = 1;
                     vm.resident.isOwner = 0;
                     vm.resident.companyId = $rootScope.companyId;
                     vm.resident.houseId = $rootScope.companyUser.houseId
                     Resident.save(vm.resident, onSuccess, onSaveError);
           }
          function allClearUpdate(){
                 modificar();
           }
            function modificar(){
                 CommonMethods.waitingMessage();
                     if(vm.resident.isOwner == true){
                       vm.resident.isOwner = 1;
                   } else {
                        vm.resident.isOwner = 0;
                   }
                   Resident.update(vm.resident, onSuccess, onSaveError);
            }
            function onSuccess (result) {
               WSResident.sendActivity(result);
               if($rootScope.companyUser.id === result.id){
                $rootScope.companyUser = result;
                $rootScope.currentUserImage = result.image;
                $rootScope.currentUserImageContentType = result.imageContentType;
               }
                vm.isSaving = false;
                $state.go('residentByHouse');
                 bootbox.hideAll();
                  if(vm.resident.id !== null){
                    toastr["success"]("Se ha editado el residente correctamente.");
                  }else{
                     toastr["success"]("Se ha registrado el residente correctamente.");
                  }

            }

        function onSaveError () {
            vm.isSaving = false;
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
