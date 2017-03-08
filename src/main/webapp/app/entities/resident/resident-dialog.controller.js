(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDialogController', ResidentDialogController);

    ResidentDialogController.$inject = ['$state','$timeout','$scope', '$rootScope', '$stateParams', 'CommonMethods','previousState', 'DataUtils','$q', 'entity', 'Resident', 'User', 'Company', 'House','Principal'];

    function ResidentDialogController($state,$timeout,$scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q,entity, Resident, User, Company, House,Principal) {
        var vm = this;
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
        vm.title = "Registrar residente";
        vm.button = "Registrar";
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        vm.success = null;
        vm.loginStringCount = 0;
        vm.SaveUserError = false;

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
                Resident.update(vm.resident, onSaveSuccess, onSaveError);
            } else {
                if (vm.resident.isOwner && vm.resident.email == null || vm.resident.isOwner && vm.resident.email == "") {
                    toastr["error"]("Debe ingresar un correo para asignar el residente como autorizador de filial.");
                } else if(vm.resident.isOwner == 1){
                        var authorities = ["ROLE_USER"]
                        authorities.push
                        vm.user.firstName =  vm.resident.name;
                        vm.user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
                        vm.user.email = vm.resident.email;
                        vm.user.activated = true;
                        vm.user.authorities = authorities;
                        vm.user.login = generateLogin(0);
                        User.save(vm.user, onSaveUser, onSaveLoginError);
                        function onSaveUser (result) {
                           insertResident(result.id);
                           $state.go('resident');
                            vm.isSaving = false;
                        }
                    } else {
                    insertResident(null);
                    $state.go('resident');
                    }


                }
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
                console.log(vm.user.login);
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





//(function() {
//    'use strict';
//
//    angular
//        .module('aditumApp')
//        .controller('ResidentDialogController', ResidentDialogController);
//
//    ResidentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Resident', 'User', 'Company', 'House'];
//
//    function ResidentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Resident, User, Company, House) {
//        var vm = this;
//
//        vm.resident = entity;
//        vm.clear = clear;
//        vm.byteSize = DataUtils.byteSize;
//        vm.openFile = DataUtils.openFile;
//        vm.save = save;
//        vm.users = User.query();
//        vm.companies = Company.query();
//        vm.houses = House.query();
//
//        $timeout(function (){
//            angular.element('.form-group:eq(1)>input').focus();
//        });
//
//        function clear () {
//            $uibModalInstance.dismiss('cancel');
//        }
//
//        function save () {
//            vm.isSaving = true;
//            if (vm.resident.id !== null) {
//                Resident.update(vm.resident, onSaveSuccess, onSaveError);
//            } else {
//                Resident.save(vm.resident, onSaveSuccess, onSaveError);
//            }
//        }
//
//        function onSaveSuccess (result) {
//            $scope.$emit('aditumApp:residentUpdate', result);
//            $uibModalInstance.close(result);
//            vm.isSaving = false;
//        }
//
//        function onSaveError () {
//            vm.isSaving = false;
//        }
//
//
//        vm.setImage = function ($file, resident) {
//            if ($file && $file.$error === 'pattern') {
//                return;
//            }
//            if ($file) {
//                DataUtils.toBase64($file, function(base64Data) {
//                    $scope.$apply(function() {
//                        resident.image = base64Data;
//                        resident.imageContentType = $file.type;
//                    });
//                });
//            }
//        };
//
//    }
//})();
