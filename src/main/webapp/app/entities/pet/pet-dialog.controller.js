(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PetDialogController', PetDialogController);

    PetDialogController.$inject = ['$state','$timeout', '$scope', '$stateParams', 'entity', 'Pet', 'House', 'Company', 'globalCompany', '$rootScope', 'Modal'];

    function PetDialogController($state,$timeout, $scope, $stateParams, entity, Pet, House, Company, globalCompany, $rootScope, Modal) {
        var vm = this;
        vm.pet = entity;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();
        vm.save = save;
        Modal.enteringForm(save);
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

        vm.clearSearchTerm = function () {
            vm.searchTerm = '';
        };
        vm.searchTerm;
        vm.typingSearchTerm = function (ev) {
            ev.stopPropagation();
        }
        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            vm.isReady = true;
        }

        function save() {
            Modal.confirmDialog(vm.titleConfirm,"",function(){
                Modal.showLoadingBar();
                vm.isSaving = true;
                vm.pet.companyId = globalCompany.getId();
                vm.pet.houseId = vm.selectedHouse;
                vm.pet.deleted = 0;
                if (vm.pet.id !== null) {
                    Pet.update(vm.pet, onSaveSuccess, onSaveError);
                } else {
                    Pet.save(vm.pet, onSaveSuccess, onSaveError);
                }
            })
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


    }
})();
