(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDialogController', ResidentDialogController);

    ResidentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Resident', 'User', 'Company', 'House'];

    function ResidentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Resident, User, Company, House) {
        var vm = this;

        vm.resident = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
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
            if (vm.resident.id !== null) {
                Resident.update(vm.resident, onSaveSuccess, onSaveError);
            } else {
                Resident.save(vm.resident, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:residentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, resident) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        resident.image = base64Data;
                        resident.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
