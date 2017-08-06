(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDialogController', OfficerDialogController);

    OfficerDialogController.$inject = ['$rootScope','$state','Principal','$timeout', 'CommonMethods','$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company','SaveImageCloudinary'];

    function OfficerDialogController ($rootScope,$state, Principal, $timeout, CommonMethods, $scope, $stateParams, $q, DataUtils, entity, Officer, User, Company, SaveImageCloudinary) {
        var vm = this;
        var fileImage = null;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.officer = entity;
         if(vm.officer.image_url==undefined){
            vm.officer.image_url = null;
          }
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;
        var indentification = vm.officer.identificationnumber;
        CommonMethods.validateLetters();
        vm.loginStringCount = 0;
        CommonMethods.validateNumbers();
        $rootScope.active = "officers";
        if(vm.officer.id !== null){
            vm.title = "Editar oficial";
            vm.button = "Editar";
        } else{
            vm.title = "Registrar oficial";
            vm.button = "Registrar";
        }

        setTimeout(function() {
            $("#edit_officer_form").fadeIn(300);
        }, 200)


        function save () {
            vm.isSaving = true;
            if (vm.officer.id !== null) {
              if(indentification!==vm.officer.identificationnumber){
               Officer.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.officer.identificationnumber},alreadyExist,allClear)
                function alreadyExist(data){
                 toastr["error"]("La cédula ingresada ya existe.");
               }
                 function allClear(data){
                    updateAccount();
                 }
            } else {
             updateAccount();
            }




            } else {
                Officer.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.officer.identificationnumber},alreadyExist,allClear)
                    function alreadyExist(data){
                    toastr["error"]("La cédula ingresada ya existe.");
                }
                 function allClear(data){
                        vm.officer.name = CommonMethods.capitalizeFirstLetter(vm.officer.name);
                        vm.officer.lastname = CommonMethods.capitalizeFirstLetter(vm.officer.lastname);
                        vm.officer.secondlastname = CommonMethods.capitalizeFirstLetter(vm.officer.secondlastname);
                        createAccount();
                 }
            }
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function createAccount(){
            var authorities = ["ROLE_OFFICER"];
            vm.user.firstName =  vm.officer.name;
            vm.user.lastName = vm.officer.lastname + ' ' + vm.officer.secondlastname;
            vm.user.email = vm.officer.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);
            User.save(vm.user, onSaveUser, onSaveLoginError);
            function onSaveUser (result) {
                insertOfficer(result.id)
                vm.isSaving = false;
            }

        }
        function insertOfficer(id){
            vm.officer.companyId = $rootScope.companyId;
            vm.officer.userId = id;
            vm.officer.inservice = 0;
            vm.officer.enable = true;
            vm.imageUser = {user: vm.officer.id};
           if(vm.imageUser!==null){
          SaveImageCloudinary
                            .save(fileImage, vm.imageUser)
                            .then(onSaveImageSuccess, onSaveError, onNotify);
            function onNotify(info) {
                        vm.progress = Math.round((info.loaded / info.total) * 100);
             }
            function onSaveImageSuccess(data) {
            vm.officer.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
              Officer.save(vm.officer, onSaveSuccess, onSaveError);
              function onSaveSuccess (result) {
                  vm.isSaving = false;
                  $state.go('officer');
                  toastr["success"]("Se ha registrado el oficial correctamente.");

              }
            }
        }else{
         Officer.save(vm.officer, onSaveSuccess, onSaveError);
         function onSaveSuccess (result) {
             vm.isSaving = false;
             $state.go('officer');
             toastr["success"]("Se ha registrado el oficial correctamente.");

         }
        }

        }
        function updateAccount(){
            User.getUserById({id: vm.officer.userId},onSuccess);
            function onSuccess(user, headers) {
                user.id = vm.officer.userId;
                user.firstName =  vm.officer.name;
                user.lastName = vm.officer.lastname + ' ' + vm.officer.secondlastname;
                user.email = vm.officer.email;

                User.update(user,onSuccessUser);
                function onSuccessUser(data, headers) {
           vm.imageUser = {user: vm.officer.id};
           if(vm.imageUser!==null){
          SaveImageCloudinary
                            .save(fileImage, vm.imageUser)
                            .then(onSaveImageSuccess, onSaveError, onNotify);
            function onNotify(info) {
                        vm.progress = Math.round((info.loaded / info.total) * 100);
             }
            function onSaveImageSuccess(data) {
            vm.officer.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
              Officer.update(vm.officer, onUpdateSuccess, onSaveError);
            }
        }else{
           Officer.update(vm.officer, onUpdateSuccess, onSaveError);
        }

                }
            }

        }
        function onUpdateSuccess (result) {
                vm.isSaving = false;
                $state.go('officer');
                toastr["success"]("Se ha editado el oficial correctamente.");
            }
        function onSaveSuccess (result) {
            $state.go('officer');
            vm.isSaving = false;
        }

        function generateLogin(config){
              var firstletterFirstName = vm.officer.name.charAt(0);
              var firstletterSecondName = vm.officer.secondlastname.charAt(0);
              if(config==1){
              vm.loginStringCount = vm.loginStringCount + 1;
              return firstletterFirstName+vm.officer.lastname+firstletterSecondName+vm.loginStringCount;
              }
              return firstletterFirstName+vm.officer.lastname+firstletterSecondName;
        }

        function onSaveLoginError () {
            vm.isSaving = false;
            vm.user.login = generateLogin(1);
            User.save(vm.user, onSaveUser, onSaveLoginError);
             function onSaveUser (result) {
                    $state.go('officer');

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
