(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NoteDetailController', NoteDetailController);

    NoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Note', 'House', 'Company', 'User'];

    function NoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Note, House, Company, User) {
        var vm = this;

        vm.note = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:noteUpdate', function(event, result) {
            vm.note = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
