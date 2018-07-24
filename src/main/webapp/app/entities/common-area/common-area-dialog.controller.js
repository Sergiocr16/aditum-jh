(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDialogController', CommonAreaDialogController);

    CommonAreaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'CommonArea'];

    function CommonAreaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, CommonArea) {
        var vm = this;

        vm.commonArea = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commonArea.id !== null) {
                CommonArea.update(vm.commonArea, onSaveSuccess, onSaveError);
            } else {
                CommonArea.save(vm.commonArea, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:commonAreaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPicture = function ($file, commonArea) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        commonArea.picture = base64Data;
                        commonArea.pictureContentType = $file.type;
                    });
                });
            }
        };

    }
})();
