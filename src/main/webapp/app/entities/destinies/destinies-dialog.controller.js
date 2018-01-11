(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DestiniesDialogController', DestiniesDialogController);

    DestiniesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Destinies'];

    function DestiniesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Destinies) {
        var vm = this;

        vm.destinies = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.destinies.id !== null) {
                Destinies.update(vm.destinies, onSaveSuccess, onSaveError);
            } else {
                Destinies.save(vm.destinies, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:destiniesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
