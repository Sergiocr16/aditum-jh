(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentByHouseDialogController', ResidentByHouseDialogController);

    ResidentByHouseDialogController.$inject = ['$state','$timeout','$scope', '$rootScope', '$stateParams', 'CommonMethods','previousState', 'DataUtils','$q', 'entity', 'Resident', 'User', 'Company', 'House','Principal','companyUser','WSResident','SaveImageCloudinary'];

    function ResidentByHouseDialogController($state,$timeout,$scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q,entity, Resident, User, Company, House,Principal,companyUser,WSResident,SaveImageCloudinary) {
          $rootScope.active = "residentsHouses";
        var vm = this;
        var fileImage = null;
        vm.isAuthenticated = Principal.isAuthenticated;
      if(entity.image_url==undefined){
        entity.image_url = null;
        }

                        vm.validate = function(){
                         var invalido = 0;
                        function hasWhiteSpace(s) {
                         function tiene(s) {
                               return /\s/g.test(s);
                            }
                            if(tiene(s)||s==undefined){
                             return true
                            }
                           return false;
                         }

                         function hasCaracterEspecial(s){
                         var caracteres = [",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿"]
                         var invalido = 0;
                          angular.forEach(caracteres,function(val,index){
                          if (s!=undefined){
                           for(var i=0;i<s.length;i++){
                           if(s.charAt(i)==val){
                           invalido++;
                           }
                           }
                           }
                          })
                          if(invalido==0){
                          return false;
                          }else{
                          return true;
                          }
                         }

                         if(vm.resident.name == undefined || vm.resident.lastname == undefined || vm.resident.secondlastname == undefined || hasWhiteSpace(vm.resident.identificationnumber)){
                            toastr["error"]("No puede ingresar espacios en blanco.");
                            invalido++;
                         }else if(hasCaracterEspecial(vm.resident.name)|| hasCaracterEspecial(vm.resident.lastname)|| hasCaracterEspecial(vm.resident.secondlastname)||hasCaracterEspecial(vm.resident.identificationnumber)){
                            invalido++;
                              toastr["error"]("No puede ingresar ningún caracter especial.");
                         }
                          if(invalido==0){
                          return true;
                          }else{
                          return false;
                          }
                        }



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
        if(  vm.validate()){
          CommonMethods.waitingMessage();
            vm.isSaving = true;
             vm.resident.name = CommonMethods.capitalizeFirstLetter(vm.resident.name);
             vm.resident.lastname = CommonMethods.capitalizeFirstLetter(vm.resident.lastname);
             vm.resident.secondlastname = CommonMethods.capitalizeFirstLetter(vm.resident.secondlastname);
             if(vm.resident.id!==null){
               if(vm.temporalIndentification!==vm.resident.identificationnumber){
                   Resident.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.resident.identificationnumber},alreadyExist,allClearUpdate)
                    function alreadyExist(data){
                    bootbox.hideAll();
                     toastr["error"]("La cédula ingresada ya existe.");
                   }
                     function allClear(data){
                          CommonMethods.waitingMessage();
                          console.log(vm.resident.isOwner)
                         if(vm.resident.isOwner == true){
                           vm.resident.isOwner = 1;
                       } else {
                            vm.resident.isOwner = 0;
                       }

                      if(fileImage!==null){

                       vm.imageUser = {user: vm.resident.id};
                     SaveImageCloudinary
                                       .save(fileImage, vm.imageUser)
                                       .then(onSaveImageSuccess, onSaveError, onNotify);
                       function onNotify(info) {
                                   vm.progress = Math.round((info.loaded / info.total) * 100);
                        }
                       function onSaveImageSuccess(data) {
                       vm.resident.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                              if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                        }
                           Resident.update(vm.resident, onSuccess, onSaveError);
                       }
                   }else{
                          if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                    vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                    }
                      Resident.update(vm.resident, onSuccess, onSaveError);
                   }
                     }
                } else {
                console.log()
                    if(vm.resident.isOwner == true){
                          vm.resident.isOwner = 1;
                      } else {
                           vm.resident.isOwner = 0;
                      }

                      if(fileImage!==null){
                        console.log("HAY IMAGEN")
                      vm.imageUser = {user: vm.resident.id};
                     SaveImageCloudinary
                                       .save(fileImage, vm.imageUser)
                                       .then(onSaveImageSuccess, onSaveError, onNotify);
                       function onNotify(info) {
                                   vm.progress = Math.round((info.loaded / info.total) * 100);
                        }
                       function onSaveImageSuccess(data) {
                       vm.resident.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                                  if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                            }
                           Resident.update(vm.resident, onSuccess, onSaveError);
                       }
                   }else{
                            if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                      vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                      }
                      Resident.update(vm.resident, onSuccess, onSaveError);
                   }
                }

             } else{
                Resident.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.resident.identificationnumber},alreadyExist,allClearInsert)
                     function alreadyExist(data){
                        bootbox.hideAll();
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
                      if(fileImage!==null){
                        console.log("HAY IMAGEN")
                      vm.imageUser = {user: vm.resident.id};
                     SaveImageCloudinary
                                       .save(fileImage, vm.imageUser)
                                       .then(onSaveImageSuccess, onSaveError, onNotify);
                       function onNotify(info) {
                                   vm.progress = Math.round((info.loaded / info.total) * 100);
                        }
                       function onSaveImageSuccess(data) {
                       vm.resident.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                               if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                         vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                         }
                         Resident.save(vm.resident, onSuccess, onSaveError);
                       }
                   }else{
                                if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                          vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                          }
                       Resident.save(vm.resident, onSuccess, onSaveError);
                   }
           }
          function allClearUpdate(){
                 modificar();
           }
            function modificar(){
                 CommonMethods.waitingMessage();
                      alert(vm.resident.isOwner)
                     if(vm.resident.isOwner == true){
                       vm.resident.isOwner = 1;
                   } else {
                        vm.resident.isOwner = 0;
                   }
              if(fileImage!==null){

                     vm.imageUser = {user: vm.resident.id};
                    SaveImageCloudinary
                                      .save(fileImage, vm.imageUser)
                                      .then(onSaveImageSuccess, onSaveError, onNotify);
                      function onNotify(info) {
                                  vm.progress = Math.round((info.loaded / info.total) * 100);
                       }
                      function onSaveImageSuccess(data) {
                      vm.resident.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                              if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                        vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                        }
                        Resident.update(vm.resident, onSuccess, onSaveError);
                      }
                  }else{
                      if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                                vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                                }
                 Resident.update(vm.resident, onSuccess, onSaveError);
                  }

            }

            function onSuccess (result) {
               WSResident.sendActivity(result);
               if($rootScope.companyUser.id === result.id){
                $rootScope.companyUser = result;

               }
                vm.isSaving = false;
                $state.go('residentByHouse',null,{reload:true});
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
}

            vm.setImage = function ($file) {
                        if ($file && $file.$error === 'pattern') {
                            return;
                        }
                        if ($file) {
                            DataUtils.toBase64($file, function(base64Data) {
                                $scope.$apply(function() {
                                    vm.displayImage = base64Data;
                                    vm.displayImageType = $file.type;
                                });
                            });
                            fileImage = $file;
                        }
               };
    }
})();
