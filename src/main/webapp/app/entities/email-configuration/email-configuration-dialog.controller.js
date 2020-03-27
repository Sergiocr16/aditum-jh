(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmailConfigurationDialogController', EmailConfigurationDialogController);

    EmailConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmailConfiguration', 'Company'];

    function EmailConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmailConfiguration, Company) {
        var vm = this;

        vm.emailConfiguration = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.emailConfiguration.id !== null) {
                EmailConfiguration.update(vm.emailConfiguration, onSaveSuccess, onSaveError);
            } else {
                EmailConfiguration.save(vm.emailConfiguration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:emailConfigurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
