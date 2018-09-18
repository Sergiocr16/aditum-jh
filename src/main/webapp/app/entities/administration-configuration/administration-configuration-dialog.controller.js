(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdministrationConfigurationDialogController', AdministrationConfigurationDialogController);

    AdministrationConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AdministrationConfiguration', 'Company'];

    function AdministrationConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AdministrationConfiguration, Company) {
        var vm = this;

        vm.administrationConfiguration = entity;
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
            bootbox.confirm({
                message: "¿Está seguro que desea guardar los cambios?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        vm.isSaving = true;
                        if (vm.administrationConfiguration.id !== null) {
                            AdministrationConfiguration.update(vm.administrationConfiguration, onSaveSuccess, onSaveError);
                        } else {
                            AdministrationConfiguration.save(vm.administrationConfiguration, onSaveSuccess, onSaveError);
                        }
                    }
                }
            });

        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:administrationConfigurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
