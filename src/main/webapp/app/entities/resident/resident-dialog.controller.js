(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDialogController', ResidentDialogController);

    ResidentDialogController.$inject = ['$state','$timeout','$scope', '$rootScope', '$stateParams', 'CommonMethods','previousState', 'DataUtils','$q', 'entity', 'Resident', 'User', 'Company', 'House','Principal','companyUser','WSResident','SaveImageCloudinary'];

    function ResidentDialogController($state,$timeout,$scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q,entity, Resident, User, Company, House,Principal,companyUser,WSResident,SaveImageCloudinary) {
        $rootScope.active = "residents";
        var vm = this;
        var fileImage = null;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        vm.resident.principalContact = vm.resident.principalContact+"";
        if(entity.image_url==undefined){
        entity.image_url = null;
        }
        vm.required = 1;
       vm.validEmail = function(email){
           var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
           return re.test(String(email).toLowerCase());
       }
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        var indentification = vm.resident.identificationnumber;
        vm.user = entity;
        vm.success = null;
        vm.loginStringCount = 0;
        vm.SaveUserError = false;

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
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
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


        setTimeout(function(){House.query({companyId: $rootScope.companyId}).$promise.then(onSuccessHouses);
        function onSuccessHouses(data, headers) {
         angular.forEach(data,function(value,key){
         value.housenumber = parseInt(value.housenumber);
                      if(value.housenumber==9999){
                      value.housenumber="Oficina"
                      }
                  })
            vm.houses = data;
             setTimeout(function() {
                                                 $("#loadingIcon").fadeOut(300);
                                       }, 400)
                                        setTimeout(function() {
                                            $("#edit_resident_form").fadeIn('slow');
                                        },900 )

        }},500)

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save () {
        if( vm.validate()){
            vm.resident.name = CommonMethods.capitalizeFirstLetter(vm.resident.name);
            vm.resident.lastname = CommonMethods.capitalizeFirstLetter(vm.resident.lastname);
            vm.resident.secondlastname = CommonMethods.capitalizeFirstLetter(vm.resident.secondlastname);
            vm.isSaving = true;
            if (vm.resident.id !== null) {
                if(indentification!==vm.resident.identificationnumber){
                   Resident.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.resident.identificationnumber},alreadyExist,allClearUpdate)
                    function alreadyExist(data){
                     toastr["error"]("La cédula ingresada ya existe.");
                   }

                } else {
                   updateResident();
                }


            } else {
                  Resident.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.resident.identificationnumber},alreadyExist,allClearInsert)
                    function alreadyExist(data){
                     toastr["error"]("La cédula ingresada ya existe.");
                   }



            }
        }

         function allClearInsert(data){
                if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "" ) {
                       toastr["error"]("Debe ingresar un correo para asignar el residente como autorizador de filial.");
                   } else if(vm.resident.isOwner == 1){
                        CommonMethods.waitingMessage();
                        createAccount(1);
                   } else {
                        CommonMethods.waitingMessage();
                        insertResident(null);
                   }
         }
         function allClearUpdate(data){
             updateResident();
         }
        function updateResident(){
          if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "") {
                toastr["error"]("Debe ingresar un correo para asignar el residente como autorizador de filial.");
            } else if(autorizadorStatus==1 && vm.resident.isOwner==false){
                 CommonMethods.waitingMessage();
                 updateAccount(0);
            } else if(autorizadorStatus==0 && vm.resident.isOwner==true){
                 if(vm.resident.userId!==null){
                     CommonMethods.waitingMessage();
                     updateAccount(1);
                 } else{
                     CommonMethods.waitingMessage();
                     createAccount(2);
                 }
            } else if(vm.resident.isOwner==false){
                changeStatusIsOwner();
                CommonMethods.waitingMessage();
                   vm.imageUser = {user: vm.resident.id};
                   if(fileImage!==null){
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

                        Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                    }
                }else{

                  if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                            }
                  Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                }
            }else{
                CommonMethods.waitingMessage();
                updateAccount(vm.resident.enabled);
            }
        }
        function changeStatusIsOwner(){
            if(vm.resident.isOwner==false){vm.resident.isOwner=0}else{vm.resident.isOwner=1}
        }
        function createAccount(opcion){
            vm.opcion = opcion;
            var authorities = ["ROLE_USER"];
            vm.user.firstName =  vm.resident.name;
            vm.user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
            vm.user.email = vm.resident.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);
            User.save(vm.user, onSaveUser, onSaveLoginError);


        }
           function onSaveUser (result) {
                       if(vm.opcion==1){
                           insertResident(result.id)
                       }
                       else if(vm.opcion==2){
                             vm.resident.userId = result.id;
                             vm.resident.isOwner = 1;
                            vm.imageUser = {user: vm.resident.id};
                             if(fileImage!==null){
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
                                    Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                                }
                                }else{
                                  if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                            }
                                 Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                                }
                       }
                       vm.isSaving = false;
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
                  vm.imageUser = {user: vm.resident.id};
                   if(fileImage!==null){
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
                                             }Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                             }
                             }else{
                               if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
                                         vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
                                         }
                              Resident.update(vm.resident, onUpdateSuccess, onSaveError);
                             }

                  }
              }

         }
            function onUpdateSuccess (result) {
            WSResident.sendActivity(result);
                vm.isSaving = false;
                $state.go('resident');
                bootbox.hideAll();
                toastr["success"]("Se ha editado el residente correctamente.");
            }
        function insertResident(id){
            vm.resident.enabled = 1;
            vm.resident.companyId = $rootScope.companyId;
            vm.resident.userId = id;
            if(vm.resident.isOwner){
            vm.resident.isOwner = 1;
            }else{
            vm.resident.isOwner = 0;
            }
          vm.imageUser = {user: id};
           if(fileImage!==null){
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

                                       Resident.save(vm.resident, onSaveSuccess, onSaveError);
                                        function onSaveSuccess (result) {
                                        WSResident.sendActivity(result);
                                             vm.isSaving = false;
                                             $state.go('resident');
                                             bootbox.hideAll();
                                             toastr["success"]("Se ha registrado el residente correctamente.");
                                         }
                        }
            }else{

            if(vm.resident.identificationnumber!=undefined || vm.resident.identificationnumber!= null){
            vm.resident.identificationnumber = vm.resident.identificationnumber.toUpperCase()
            }

           Resident.save(vm.resident, onSaveSuccess, onSaveError);
                function onSaveSuccess (result) {
                WSResident.sendActivity(result);
                     vm.isSaving = false;
                     $state.go('resident');
                     bootbox.hideAll();
                     toastr["success"]("Se ha registrado el residente correctamente.");
                 }
            }

        }

        function generateLogin(config){
function getCleanedString(cadena){
   // Definimos los caracteres que queremos eliminar
   var specialChars = "!@#$^&%*()+=-[]\/{}|:<>?,.";

   // Los eliminamos todos
   for (var i = 0; i < specialChars.length; i++) {
       cadena= cadena.replace(new RegExp("\\" + specialChars[i], 'gi'), '');
   }

   // Lo queremos devolver limpio en minusculas
   cadena = cadena.toLowerCase();

   // Quitamos espacios y los sustituimos por _ porque nos gusta mas asi
   cadena = cadena.replace(/ /g,"_");

   // Quitamos acentos y "ñ". Fijate en que va sin comillas el primer parametro
   cadena = cadena.replace(/á/gi,"a");
   cadena = cadena.replace(/é/gi,"e");
   cadena = cadena.replace(/í/gi,"i");
   cadena = cadena.replace(/ó/gi,"o");
   cadena = cadena.replace(/ú/gi,"u");
   cadena = cadena.replace(/ñ/gi,"n");
   return cadena;
}
              var firstletterFirstName = vm.resident.name.charAt(0);
              var firstletterSecondName = vm.resident.secondlastname.charAt(0);
              if(config==1){
              vm.loginStringCount = vm.loginStringCount + 1;
              return getCleanedString(firstletterFirstName+vm.resident.lastname+firstletterSecondName+vm.loginStringCount);
              }
              return getCleanedString(firstletterFirstName+vm.resident.lastname+firstletterSecondName);
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        function onSaveLoginError (error) {
            vm.isSaving = false;
             switch(error.data.login){
                case "emailexist":
                      toastr["error"]("El correo electrónico ingresado ya existe.");
                          bootbox.hideAll();
                break;
                 case "userexist":
                      vm.user.login = generateLogin(1);

                    User.save(vm.user, onSaveUser, onSaveLoginError);

                break;
                }

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
