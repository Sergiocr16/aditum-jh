(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyRhDialogController', CompanyRhDialogController);

    CompanyRhDialogController.$inject = ['AdminInfo','House','$state','CompanyConfiguration','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company'];

    function CompanyRhDialogController (AdminInfo,House,$state,CompanyConfiguration,$timeout, $scope, $stateParams, $uibModalInstance, entity, Company) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
      function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function onError () {

        }
        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onSaveSuccess, onSaveError);
                 toastr["success"]("Se ha editado el nombre del condominio exitosamente");
            } else {
                Company.save(vm.company, onSaveSuccess, onSaveError);
                    toastr["success"]("Se ha editado el nombre del condominio exitosamente");
            }
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
