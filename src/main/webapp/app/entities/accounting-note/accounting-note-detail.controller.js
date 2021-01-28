(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountingNoteDetailController', AccountingNoteDetailController);

    AccountingNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountingNote', 'House', 'Company'];

    function AccountingNoteDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountingNote, House, Company) {
        var vm = this;

        vm.accountingNote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:accountingNoteUpdate', function(event, result) {
            vm.accountingNote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
