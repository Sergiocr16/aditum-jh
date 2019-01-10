(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyConfigurationDialogController', CompanyConfigurationDialogController);

    CompanyConfigurationDialogController.$inject = ['House','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyConfiguration', 'Company'];

    function CompanyConfigurationDialogController (House,$timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyConfiguration, Company) {
        var vm = this;

        vm.companyConfiguration = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        loadQuantities();
        function loadQuantities(){
            House.query({ companyId: vm.companyConfiguration.companyId}, onSuccess, onError);
        }
        function onSuccess(data) {
            vm.houseQuantity = data.length;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyConfiguration.id !== null) {
                CompanyConfiguration.update(vm.companyConfiguration, onSaveSuccess, onSaveError);
            } else {
                CompanyConfiguration.save(vm.companyConfiguration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:companyConfigurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();