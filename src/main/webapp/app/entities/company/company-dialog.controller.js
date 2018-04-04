(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['AdminInfo','House','$state','CompanyConfiguration','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company','AdministrationConfiguration'];

    function CompanyDialogController (AdminInfo,House,$state,CompanyConfiguration,$timeout, $scope, $stateParams, $uibModalInstance, entity, Company, AdministrationConfiguration) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        loadQuantities();
        function loadQuantities(){
            House.query({ companyId: vm.company.id}, onSuccess, onError);
        }
        function onSuccess(data) {
            vm.houseQuantity = data.length;
            if(vm.company.id!=undefined){
            getAdmins();
            }
        }

        function getAdmins(){
            AdminInfo.getAdminsByCompanyId({
                companyId: vm.company.id
            }, onSuccess);
            function onSuccess(data) {
                vm.adminsQuantity = data.length;
            }
        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        getConfiguration();
        function getConfiguration(){
            CompanyConfiguration.getByCompanyId({companyId:vm.company.id}).$promise.then(onSuccessCompany, onError);
        }
        function onSuccessCompany (data) {
            angular.forEach(data, function(configuration, key) {
                vm.companyConfiguration = configuration;
            });

        }

        function onError () {

        }
        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onUpdateSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveCompanySuccess, onSaveError);

            }
        }
        function onUpdateSuccess (result) {
            CompanyConfiguration.update(vm.companyConfiguration, onSaveSuccess, onSaveError);
        }
        function onSaveCompanySuccess (result) {
            vm.companyConfiguration.companyId = result.id;
            CompanyConfiguration.save(vm.companyConfiguration, onSaveSuccess, onSaveError);
            AdministrationConfiguration.save({squareMetersPrice:0, companyId:result.id});
        }
        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:companyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
