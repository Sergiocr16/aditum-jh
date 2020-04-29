(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerARDialogController', OfficerARDialogController);

    OfficerARDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OfficerAR', 'Company', 'User', 'House'];

    function OfficerARDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OfficerAR, Company, User, House) {
        var vm = this;

        vm.officerAR = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.companies = Company.query();
        vm.users = User.query();
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.officerAR.id !== null) {
                OfficerAR.update(vm.officerAR, onSaveSuccess, onSaveError);
            } else {
                OfficerAR.save(vm.officerAR, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:officerARUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
