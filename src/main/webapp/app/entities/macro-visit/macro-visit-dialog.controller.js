(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroVisitDialogController', MacroVisitDialogController);

    MacroVisitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MacroVisit', 'MacroCondominium', 'Company', 'House'];

    function MacroVisitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MacroVisit, MacroCondominium, Company, House) {
        var vm = this;

        vm.macroVisit = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.macrocondominiums = MacroCondominium.query();
        vm.companies = Company.query();
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.macroVisit.id !== null) {
                MacroVisit.update(vm.macroVisit, onSaveSuccess, onSaveError);
            } else {
                MacroVisit.save(vm.macroVisit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:macroVisitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.arrivaltime = false;
        vm.datePickerOpenStatus.departuretime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
