(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'House', 'Vehicule', 'Visitant', 'Note', 'Resident', 'Emergency', 'Company'];

    function HouseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, House, Vehicule, Visitant, Note, Resident, Emergency, Company) {
        var vm = this;
        vm.house = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.vehicules = Vehicule.query();
        vm.visitants = Visitant.query();
        vm.notes = Note.query();
        vm.residents = Resident.query();
        vm.emergencies = Emergency.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.house.id !== null) {
                House.update(vm.house, onSaveSuccess, onSaveError);
            } else {
                House.save(vm.house, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:houseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.desocupationinitialtime = false;
        vm.datePickerOpenStatus.desocupationfinaltime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
