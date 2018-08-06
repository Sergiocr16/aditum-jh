(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementDialogController', AnnouncementDialogController);

    AnnouncementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Announcement', 'Company'];

    function AnnouncementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Announcement, Company) {
        var vm = this;

        vm.announcement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.announcement.id !== null) {
                Announcement.update(vm.announcement, onSaveSuccess, onSaveError);
            } else {
                Announcement.save(vm.announcement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:announcementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.publishingDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
