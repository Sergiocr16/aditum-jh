(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDialogController', OfficerDialogController);

    OfficerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company'];

    function OfficerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Officer, User, Company) {
        var vm = this;

        vm.officer = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.officer.id !== null) {
                Officer.update(vm.officer, onSaveSuccess, onSaveError);
            } else {
                Officer.save(vm.officer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:officerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, officer) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        officer.image = base64Data;
                        officer.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
