(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProtocolDialogController', ProtocolDialogController);

    ProtocolDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Protocol', 'Company'];

    function ProtocolDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Protocol, Company) {
        var vm = this;

        vm.protocol = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.protocol.id !== null) {
                Protocol.update(vm.protocol, onSaveSuccess, onSaveError);
            } else {
                Protocol.save(vm.protocol, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:protocolUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, protocol) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        protocol.image = base64Data;
                        protocol.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
