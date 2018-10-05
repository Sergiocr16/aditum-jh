(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdministrationConfigurationDetailController', AdministrationConfigurationDetailController);

    AdministrationConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AdministrationConfiguration', 'Company'];

    function AdministrationConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, AdministrationConfiguration, Company) {
        var vm = this;
        $rootScope.active = "administrationConfiguration";
        vm.administrationConfiguration = entity;
        vm.previousState = previousState.name;
        vm.save = save;
        vm.data = {
            cb1: true,
            cb4: true,
            cb5: false
        };

        vm.message = 'false';

        vm.onChange = function(cbState) {
            vm.message = cbState;
        };
        var unsubscribe = $rootScope.$on('aditumApp:administrationConfigurationUpdate', function(event, result) {
            vm.administrationConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
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
            toastr["success"]("Se han guardado los cambios existosamente.")
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
