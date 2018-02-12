(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NoteDialogController', NoteDialogController);

    NoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'Note','Principal','WSNote','$rootScope','$state'];

    function NoteDialogController ($timeout, $scope, $stateParams, Note,Principal,WSNote,$rootScope,$state) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.save = save;
        $rootScope.active = "reportHomeService";
        function populateValidNote(){
        vm.note.creationdate = moment(new Date()).format();
        vm.note.companyId = $rootScope.companyId;
        vm.note.notetype = 1;
        vm.note.houseId = $rootScope.companyUser.houseId;
        }

         angular.element(document).ready(function(){
                      setTimeout(function() {
                                $("#loadingIcon").fadeOut(300);
                      }, 400)
                       setTimeout(function() {
                           $("#all").fadeIn('slow');
                       },900 )

         })

        function save () {
            vm.isSaving = true;
            if (vm.note.id !== null) {
            populateValidNote()
            WSNote.sendActivity(vm.note,onSaveSuccess);
            toastr['success']("Has reportado el servicio a domicilio correctamente");
            vm.note = undefined;
            $state.go('residentByHouse');
            }
        }

        function onSaveSuccess (result) {
        console.log('a')
            $scope.$emit('aditumApp:noteUpdate', result);

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
