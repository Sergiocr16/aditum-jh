(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CollectionDialogController', CollectionDialogController);

    CollectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Collection', 'House','$rootScope'];

    function CollectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Collection, House, $rootScope) {
        var vm = this;

        vm.collection = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;



        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
            vm.houses = House.query({companyId:$rootScope.companyId});
        },600);

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.collection.id !== null) {
                Collection.update(vm.collection, onSaveSuccess, onSaveError);
            } else {
                Collection.save(vm.collection, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:collectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
