(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResolutionDialogController', ResolutionDialogController);

    ResolutionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Resolution', 'Article'];

    function ResolutionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Resolution, Article) {
        var vm = this;

        vm.resolution = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.articles = Article.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resolution.id !== null) {
                Resolution.update(vm.resolution, onSaveSuccess, onSaveError);
            } else {
                Resolution.save(vm.resolution, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:resolutionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
