(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDialogController', OfficerDialogController);

    OfficerDialogController.$inject = ['$state','Principal','$timeout', 'CommonMethods','$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company'];

    function OfficerDialogController ($state, Principal, $timeout, CommonMethods, $scope, $stateParams, $q, DataUtils, entity, Officer, User, Company) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.officer = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;
        CommonMethods.validateLetters();
        vm.loginStringCount = 0;
        CommonMethods.validateNumbers();

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
                updateAccount();
            } else {
                createAccount();
            }
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function createAccount(){
            vm.officer.inservice = 1;
            vm.officer.companyId = $rootScope.companyId;
            vm.user.activated = true;
            var authorities = ["ROLE_OFFICER"];
            vm.user.authorities = authorities;
            vm.user.firstName =  vm.officer.name;
            vm.user.lastName = vm.officer.lastname + ' ' + vm.officer.secondlastname;
            vm.user.email = vm.officer.email;
            vm.user.login = generateLogin(0);
            User.save(vm.user, onSaveUser, onSaveLoginError);
            function onSaveUser (result) {
                 vm.officer.userId = result.id;
                 vm.officer.name = CommonMethods.capitalizeFirstLetter(vm.officer.name);
                 vm.officer.lastname = CommonMethods.capitalizeFirstLetter(vm.officer.lastname);
                 vm.officer.secondlastname = CommonMethods.capitalizeFirstLetter(vm.officer.secondlastname);
                 Officer.save(vm.officer, onSaveSuccess, onSaveError);
                 vm.isSaving = false;
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

                    Officer.update(vm.officer, onUpdateSuccess, onSaveError);

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
