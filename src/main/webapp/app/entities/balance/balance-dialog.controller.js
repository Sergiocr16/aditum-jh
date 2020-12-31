(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BalanceDialogController', BalanceDialogController);

    BalanceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Balance', 'House'];

    function BalanceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Balance, House) {
        var vm = this;

        vm.balance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.houses = House.query({filter: 'balance-is-null'});
        $q.all([vm.balance.$promise, vm.houses.$promise]).then(function() {
            if (!vm.balance.houseId) {
                return $q.reject();
            }
            return House.get({id : vm.balance.houseId}).$promise;
        }).then(function(house) {
            vm.houses.push(house);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.balance.id !== null) {
                Balance.update(vm.balance, onSaveSuccess, onSaveError);
            } else {
                Balance.save(vm.balance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:balanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
