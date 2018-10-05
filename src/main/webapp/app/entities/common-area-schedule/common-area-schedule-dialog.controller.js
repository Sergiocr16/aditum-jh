(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaScheduleDialogController', CommonAreaScheduleDialogController);

    CommonAreaScheduleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CommonAreaSchedule'];

    function CommonAreaScheduleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CommonAreaSchedule) {
        var vm = this;

        vm.commonAreaSchedule = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commonAreaSchedule.id !== null) {
                CommonAreaSchedule.update(vm.commonAreaSchedule, onSaveSuccess, onSaveError);
            } else {
                CommonAreaSchedule.save(vm.commonAreaSchedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:commonAreaScheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
