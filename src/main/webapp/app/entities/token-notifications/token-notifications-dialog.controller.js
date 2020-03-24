(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TokenNotificationsDialogController', TokenNotificationsDialogController);

    TokenNotificationsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TokenNotifications', 'User'];

    function TokenNotificationsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TokenNotifications, User) {
        var vm = this;

        vm.tokenNotifications = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tokenNotifications.id !== null) {
                TokenNotifications.update(vm.tokenNotifications, onSaveSuccess, onSaveError);
            } else {
                TokenNotifications.save(vm.tokenNotifications, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:tokenNotificationsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
