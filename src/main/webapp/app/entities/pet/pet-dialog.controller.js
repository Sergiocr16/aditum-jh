(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PetDialogController', PetDialogController);

    PetDialogController.$inject = ['Principal', 'DataUtils', 'SaveImageCloudinary', '$state', '$timeout', '$scope', '$stateParams', 'entity', 'Pet', 'House', 'Company', 'globalCompany', '$rootScope', 'Modal'];

    function PetDialogController(Principal, DataUtils, SaveImageCloudinary, $state, $timeout, $scope, $stateParams, entity, Pet, House, Company, globalCompany, $rootScope, Modal) {
        var vm = this;
        vm.pet = entity;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();
        vm.save = save;
        Modal.enteringForm(save);
        var fileImage = null;
        if (vm.pet.imageUrl == undefined) {
            vm.pet.imageUrl = null;
        }
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        if (vm.pet.id == null) {
            $rootScope.mainTitle = "Registrar mascota"
            vm.button = "Registrar";
            vm.success = "Se registró la mascota correctamente."
            vm.titleConfirm = "¿Está seguro que desea registrar la mascota?"
        } else {
            vm.selectedHouse = vm.pet.houseId;
            $rootScope.mainTitle = "Editar mascota";
            vm.titleConfirm = "¿Está seguro que desea editar la mascota?"
            vm.success = "Se edito la mascota correctamente."
            vm.button = "Editar";
        }
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        Principal.identity().then(function (account) {
            switch (account.authorities[0]) {
                case "ROLE_MANAGER":
                    vm.isUser = false;
                    break;
                case "ROLE_JD":
                    vm.isUser = false;
                    break;
                case "ROLE_OWNER":
                    vm.isUser = true;
                    break;
                case "ROLE_USER":
                    vm.isUser = true;
                    break;
                case "ROLE_TENANT":
                    vm.isUser = true;
                    break;
            }
            if (vm.isUser) {
                vm.selectedHouse = globalCompany.getHouseId();
                vm.isReady = true;
                vm.houses.push({id:0})
            } else {
                House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);
            }
        })
        vm.clearSearchTerm = function () {
            vm.searchTerm = '';
        };
        vm.searchTerm;
        vm.typingSearchTerm = function (ev) {
            ev.stopPropagation();
        }


        function onSuccessHouses(data, headers) {
            vm.houses = data;
            vm.isReady = true;
        }

        function save() {
            Modal.confirmDialog(vm.titleConfirm, "", function () {
                Modal.showLoadingBar();
                vm.isSaving = true;
                vm.pet.companyId = globalCompany.getId();
                vm.pet.houseId = vm.selectedHouse;
                vm.pet.deleted = 0;
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify);
                } else {
                    if (vm.pet.id !== null) {
                        Pet.update(vm.pet, onSaveSuccess, onSaveError);
                    } else {
                        Pet.save(vm.pet, onSaveSuccess, onSaveError);
                    }
                }
            })
        }

        function onNotify(info) {
            vm.progress = Math.round((info.loaded / info.total) * 100);
        }

        function onSaveImageSuccess(data) {
            vm.pet.imageUrl = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
            if (vm.pet.id !== null) {
                Pet.update(vm.pet, onSaveSuccess, onSaveError);
            } else {
                Pet.save(vm.pet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            Modal.hideLoadingBar();
            Modal.toast(vm.success);
            $state.go("pet")
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
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
