(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoEditDialogController', AdminInfoEditDialogController);

    AdminInfoEditDialogController.$inject = ['$rootScope', '$state', '$scope', '$stateParams', 'Principal', 'DataUtils', 'AdminInfo', 'User', 'Company', 'SaveImageCloudinary', 'companyUser', 'Modal'];

    function AdminInfoEditDialogController($rootScope, $state, $scope, $stateParams, Principal, DataUtils, AdminInfo, User, Company, SaveImageCloudinary, companyUser, Modal) {
        var vm = this;
        var fileImage = null;
        vm.isReady = false;

        if (companyUser.image_url == undefined) {
            companyUser.image_url = null;
        }

        AdminInfo.get({id: companyUser.id}, function (result) {
            vm.adminInfo = result

            vm.byteSize = DataUtils.byteSize;
            vm.openFile = DataUtils.openFile;
            vm.save = updateAccount;
            Modal.enteringForm(updateAccount);
            $scope.$on("$destroy", function () {
                Modal.leavingForm();
            });
            vm.isAuthenticated = Principal.isAuthenticated;
            User.getUserById({id: companyUser.userId}, onSuccess);

            function onSuccess(user, headers) {
                vm.user = user;
                vm.adminInfo.email = vm.user.email;
                vm.isReady = true;
                $rootScope.mainTitle = "Editar información [" + vm.user.login + "]";

            }
        })

        function save(){
            vm.isSaving = true;
            if (vm.adminInfo.id !== null) {
                Modal.confirmDialog("¿Está seguro que desea modificar su información?", "",actionSave)
            }
        }

        function actionSave() {
            if (vm.adminInfo.id !== null) {
               Modal.showLoadingBar();
                vm.imageUser = {user: vm.adminInfo.id};
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify);


                } else {
                    AdminInfo.update(vm.adminInfo, onSaveSuccess, onSaveError);
                }


            } else {
                vm.imageUser = {user: vm.adminInfo.id};
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify);


                } else {
                    AdminInfo.save(vm.adminInfo, onSaveSuccess, onSaveError);
                }

            }

            function onNotify(info) {
                vm.progress = Math.round((info.loaded / info.total) * 100);
            }

            function onSaveImageSuccess(data) {
                vm.adminInfo.image_url = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
                AdminInfo.update(vm.adminInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:adminInfoUpdate', result);
            Modal.hideLoadingBar();

            Modal.toast("Se ha editado tu información correctamente.");
            console.log(result)
            $rootScope.companyUser = result;
            $rootScope.companyUser.image_url = vm.adminInfo.image_url;
            $state.go('dashboard', null, {reload: true});
            vm.isSaving = false;
        }

        function onSaveError() {
            Modal.hideLoadingBar();
            Modal.toast("Ah ocurrido un error editando tu información");

            vm.isSaving = false;
        }

        function updateAccount() {
            vm.user.id = vm.adminInfo.userId;
            vm.user.activated = 1;
            vm.user.firstName = vm.adminInfo.name;
            vm.user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
            vm.user.email = vm.adminInfo.email;
            User.update(vm.user, onSuccessUser);

            function onSuccessUser(data, headers) {
                save();
            }
        }


        vm.setImage = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        vm.displayImage = base64Data;
                        vm.displayImageType = $file.type;
                    });
                });
                fileImage = $file;
            }
        };

    }
})();
