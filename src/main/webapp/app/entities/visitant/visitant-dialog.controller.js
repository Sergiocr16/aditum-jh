(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDialogController', VisitantDialogController);

    VisitantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Visitant', 'House', 'Company'];

    function VisitantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Visitant, House, Company) {
        var vm = this;

        vm.visitant = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.visitant.id !== null) {
                Visitant.update(vm.visitant, onSaveSuccess, onSaveError);
            } else {
                Visitant.save(vm.visitant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:visitantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.arrivaltime = false;
        vm.datePickerOpenStatus.invitationstaringtime = false;
        vm.datePickerOpenStatus.invitationlimittime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
