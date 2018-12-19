(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdministrationConfigurationDetailController', AdministrationConfigurationDetailController);

    AdministrationConfigurationDetailController.$inject = ['Modal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AdministrationConfiguration', 'Company'];

    function AdministrationConfigurationDetailController(Modal,$scope, $rootScope, $stateParams, previousState, entity, AdministrationConfiguration, Company) {
        var vm = this;
        $rootScope.active = "administrationConfiguration";
        vm.administrationConfiguration = entity;
        console.log(vm.administrationConfiguration)
        vm.isReady = false;
        vm.previousState = previousState.name;
        vm.save = save;
        vm.data = {
            cb1: true,
            cb4: true,
            cb5: false
        };

        vm.message = 'false';
        vm.isReady = true;
        vm.onChange = function(cbState) {
            vm.message = cbState;
        };
        var unsubscribe = $rootScope.$on('aditumApp:administrationConfigurationUpdate', function(event, result) {
            vm.administrationConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
        function save () {

            Modal.confirmDialog("¿Está seguro que desea guardar los cambios?","",
                function(){
                    if(vm.administrationConfiguration.usingSubchargePercentage==="0"){
                        vm.administrationConfiguration.usingSubchargePercentage = true;
                    }else{
                        vm.administrationConfiguration.usingSubchargePercentage = false;
                    }
                    vm.isSaving = true;
                    if (vm.administrationConfiguration.id !== null) {
                        AdministrationConfiguration.update(vm.administrationConfiguration, onSaveSuccess, onSaveError);
                    } else {
                        AdministrationConfiguration.save(vm.administrationConfiguration, onSaveSuccess, onSaveError);
                    }

                });

        }

        function onSaveSuccess (result) {
           Modal.toast("Se han guardado los cambios existosamente.")
            vm.administrationConfiguration = result;
            if(vm.administrationConfiguration.usingSubchargePercentage===true){
                vm.administrationConfiguration.usingSubchargePercentage = "0";
            }else{
                vm.administrationConfiguration.usingSubchargePercentage = "1";
            }
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
