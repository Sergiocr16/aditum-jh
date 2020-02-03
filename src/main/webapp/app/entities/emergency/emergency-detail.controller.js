(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('EmergencyDetailController', EmergencyDetailController);

        EmergencyDetailController.$inject = ['Modal', 'AlertService', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', '$localStorage', 'Emergency'];

        function EmergencyDetailController(Modal, AlertService, $scope, $rootScope, $stateParams, previousState, entity, $localStorage, Emergency) {
            var vm = this;
            vm.isReady = true;
            if ($localStorage.editing) {
                vm.editing = true;
            } else {
                vm.editing = false;
            }

            vm.emergency = entity;
            console.log(entity)

            vm.previousState = previousState.name;
            Modal.enteringDetail();
            $scope.$on("$destroy", function () {
                Modal.leavingDetail();
            });
            $rootScope.active = "emergency";

            vm.edit = function () {

                Modal.confirmDialog("¿Está seguro que desea documentar esta emergencia?", "",
                    function () {
                        vm.isSaving = true;
                        Modal.showLoadingBar();
                        Emergency.update(vm.emergency, onSaveSuccess, onSaveError);


                    })
            }

            function onSaveSuccess(result) {
                Modal.hideLoadingBar();
                Modal.toast("Se documentó la emeregncia exitosamente.")
                vm.editing = false;
                vm.isSaving = false;
            }


            function onSaveError() {
                Modal.hideLoadingBar();
                vm.isSaving = false;
            }


        }
    }

)();
