(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoDialogController', AdminInfoDialogController);

    AdminInfoDialogController.$inject = ['$state','CommonMethods','$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'AdminInfo', 'User', 'Company'];

    function AdminInfoDialogController ($state,CommonMethods,$timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, AdminInfo, User, Company) {
        var vm = this;

        vm.adminInfo = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;
        vm.required = 1;
        vm.users = User.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        Company.query({
        }, onSuccess);

        function onSuccess(data, headers) {
            vm.company = data;
            setTimeout(function() {
                $("#admin_form").fadeIn(600);
            }, 200)
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
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
            var authorities = ["ROLE_USER"];
            vm.user.firstName =  vm.adminInfo.name;
            vm.user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
            vm.user.email = vm.adminInfo.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);
            User.save(vm.user, onSaveUser, onSaveLoginError);
            function onSaveUser (result) {
                insertResident(result.id)
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
        function insertResident(id){
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
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
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
