(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['CommonMethods','$state','$rootScope','Principal','$timeout', '$scope', '$stateParams', 'entity', 'House','JhiTrackerService'];

    function HouseDialogController (CommonMethods,$state,$rootScope, Principal,$timeout, $scope, $stateParams,  entity, House,JhiTrackerService) {
        var vm = this;
  $rootScope.active = "houses";
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.house = entity;
        vm.save = save;

        if(vm.house.id !== null){
            vm.title = "Editar house";
            vm.button = "Editar";

        } else{
          vm.title = "Registrar casa";
          vm.button = "Registrar";
        }


             setTimeout(function() {
            $("#edit_house_form").fadeIn(600);
         }, 200)


        function save () {
        CommonMethods.waitingMessage();
            vm.isSaving = true;
            if (vm.house.id !== null) {
                House.update(vm.house, onSaveSuccess, onSaveError);
            } else {
                vm.house.companyId = $rootScope.companyId;
                vm.house.isdesocupated = 0;
                 vm.house.desocupationinitialtime = new Date();
                 vm.house.desocupationfinaltime = new Date();
                House.save(vm.house, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
        JhiTrackerService.sendHouse(result);
            $state.go('house');
            bootbox.hideAll();
               if(vm.house.id !== null){
                    toastr["success"]("Se editó la casa correctamente");
                } else{
                    toastr["success"]("Se registró la casa correctamente");
                }

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
