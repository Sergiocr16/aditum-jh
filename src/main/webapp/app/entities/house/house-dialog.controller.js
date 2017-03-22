(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['Principal','$timeout', '$scope', '$stateParams', 'entity', 'House', 'Vehicule', 'Visitant', 'Note', 'Resident', 'Emergency', 'Company'];

    function HouseDialogController (Principal,$timeout, $scope, $stateParams,  entity, House, Vehicule, Visitant, Note, Resident, Emergency, Company) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;

        vm.house = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.title = "Registrar casa";
        vm.button = "Registrar";
             setTimeout(function() {
            $("#edit_house_form").fadeIn(600);
         }, 200)
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
