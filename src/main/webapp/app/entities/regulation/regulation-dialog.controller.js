(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDialogController', RegulationDialogController);

    RegulationDialogController.$inject = ['Modal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Regulation', 'Company'];

    function RegulationDialogController(Modal,$timeout, $scope, $stateParams, $uibModalInstance, entity, Regulation, Company) {
        var vm = this;

        vm.regulation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.required = 1;
        vm.companies = Company.query();


        Company.query(onSuccessCompany);

        function onSuccessCompany(data) {
            vm.condos = data;
            vm.isReady = true;

            if (vm.regulation.id !== null) {
                vm.title = "Editar reglamento";
                vm.button = "Editar";
                angular.forEach(data, function (company, key) {

                    if (company.id == vm.regulation.companyId) {
                        company.selected = true;
                    }

                });

            } else {
                angular.forEach(data, function (value, key) {
                    value.selected = false;
                });
                vm.title = "Registrar reglamento ";
                vm.button = "Registrar";

            }
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.selectCompany = function (condo) {
            angular.forEach(vm.condos, function (company, key) {
                company.selected = false;
            });

            condo.selected = !condo.selected;
            vm.regulation.companyId = condo.id;
        };

        function save() {
            vm.isSaving = true;
            Modal.showLoadingBar()
            if (vm.regulation.id !== null) {
                Regulation.update(vm.regulation, onSaveSuccess, onSaveError);
            } else {
                vm.regulation.deleted = 0;
                Regulation.save(vm.regulation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:regulationUpdate', result);
            Modal.hideLoadingBar();
            Modal.toast("Se ha gestionado el reglamento correctamente.");
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
