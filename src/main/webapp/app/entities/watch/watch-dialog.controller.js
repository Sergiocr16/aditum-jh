(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchDialogController', WatchDialogController);

    WatchDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Watch', 'AccessDoor', 'Company'];

    function WatchDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Watch, AccessDoor, Company) {
        var vm = this;

        vm.watch = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.accessdoors = AccessDoor.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.watch.id !== null) {
                Watch.update(vm.watch, onSaveSuccess, onSaveError);
            } else {
                Watch.save(vm.watch, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:watchUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
