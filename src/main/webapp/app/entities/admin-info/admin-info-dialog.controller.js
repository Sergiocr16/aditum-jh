
(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoDialogController', AdminInfoDialogController);

    AdminInfoDialogController.$inject = ['$rootScope','$state','CommonMethods','$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'AdminInfo', 'User', 'Company','Principal'];

    function AdminInfoDialogController ($rootScope,$state,CommonMethods,$timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, AdminInfo, User, Company, Principal) {

        var vm = this;
        vm.loginStringCount = 0;
        vm.adminInfo = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;
        vm.required = 1;
        vm.users = User.query();
        vm.company = Company.query();

        vm.isAuthenticated = Principal.isAuthenticated;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        vm.isAuthenticated = Principal.isAuthenticated;
        angular.element(document).ready(function () {
            $("#myInfo").fadeIn(600);
        });

             if (vm.adminInfo.id !== null) {
                      User.getUserById({id: vm.adminInfo.userId},onSuccess);
            }

         function onSuccess(user, headers) {
             vm.user = user;
             vm.adminInfo.email  = vm.user.email;
             }

        function save () {
            vm.isSaving = true;
console.log(vm.adminInfo)
            if (vm.adminInfo.id !== null) {
                AdminInfo.update(vm.adminInfo, onSaveSuccess, onSaveError);
                updateAccount();
            } else {
                vm.adminInfo.name = CommonMethods.capitalizeFirstLetter(vm.adminInfo.name);
                vm.adminInfo.lastname = CommonMethods.capitalizeFirstLetter(vm.adminInfo.lastname);
                vm.adminInfo.secondlastname = CommonMethods.capitalizeFirstLetter(vm.adminInfo.secondlastname);
                createAccount();
            }
        }
        function createAccount(){
            var authorities = ["ROLE_MANAGER"];
            vm.user.firstName =  vm.adminInfo.name;
            vm.user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
            vm.user.email = vm.adminInfo.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);

            User.save(vm.user, onSaveUser, onSaveLoginError);
            function onSaveUser (result) {
                insertAdmin(result.id)
                vm.isSaving = false;
            }

        }
        function onSaveLoginError () {
            vm.isSaving = false;
            vm.user.login = generateLogin(1);
            User.save(vm.user, onSaveUser, onSaveLoginError);
            function onSaveUser (result) {
                $state.go('admin-info');

            }
        }
        function insertAdmin(id){
            vm.adminInfo.enabled = 1;
            vm.adminInfo.companyId = vm.adminInfo.companyId;
            vm.adminInfo.userId = id;
            AdminInfo.save(vm.adminInfo, onSaveSuccess, onSaveError);
            function onSaveSuccess (result) {
                vm.isSaving = false;
                $uibModalInstance.close(result);
                toastr["success"]("Se ha registrado el administrador correctamente.");

            }
        }

        function updateAccount(){
            User.getUserById({id: vm.adminInfo.userId},onSuccess);
            function onSuccess(user, headers) {
                user.id = vm.adminInfo.userId;
                user.activated = vm.adminInfo.enabled;
                user.firstName =  vm.adminInfo.name;
                user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
                user.email = vm.adminInfo.email;
                User.update(user,onSuccessUser);
                function onSuccessUser(data, headers) {
                    AdminInfo.update(vm.adminInfo, onUpdateSuccess, onSaveError);

                }
            }

        }
        function onUpdateSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
            toastr["success"]("Se ha editado el administrador correctamente.");
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        function generateLogin(config){

            var firstletterFirstName = vm.adminInfo.name.charAt(0);
            var firstletterSecondName = vm.adminInfo.secondlastname.charAt(0);
            if(config==1){
                vm.loginStringCount = vm.loginStringCount + 1;
                return firstletterFirstName+vm.adminInfo.lastname+firstletterSecondName+vm.loginStringCount;
            }
            return firstletterFirstName+vm.adminInfo.lastname+firstletterSecondName;
        }
        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:adminInfoUpdate', result);
            $state.go('home');
            toastr["success"]("Se ha editado tu información correctamente.");
         $rootScope.companyUser = result;
                        $rootScope.currentUserImage = result.image;
                        $rootScope.currentUserImageContentType = result.imageContentType;
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        function updateAccount(){
                 vm.user.id = vm.adminInfo.userId;
                 vm.user.activated = 1;
                 vm.user.firstName =  vm.adminInfo.name;
                 vm.user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
                 vm.user.email = vm.adminInfo.email;
                 User.update(vm.user,onSuccessUser);
                 function onSuccessUser(data, headers) {
                    save();
                  }
              }



        vm.setImage = function ($file, adminInfo) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        adminInfo.image = base64Data;
                        adminInfo.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
