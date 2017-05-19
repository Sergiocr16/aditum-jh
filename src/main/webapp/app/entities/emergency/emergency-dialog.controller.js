(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyDialogController', EmergencyDialogController);

    EmergencyDialogController.$inject = ['$timeout', '$scope','$state','$stateParams', 'Principal','WSEmergency','$rootScope'];

    function EmergencyDialogController ($timeout, $scope,$state, $stateParams, Principal,WSEmergency,$rootScope) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.reportEmergency = save;
        $rootScope.active = "reportemergencyactive";
       angular.element(document).ready(function(){
        $("#all").fadeIn("slow");
       })

       function formatValidEmergency(){
       vm.emergency = {};
       vm.emergency.companyId = $rootScope.companyId;
       vm.emergency.houseId = $rootScope.companyUser.houseId;
       vm.emergency.isAttended = 0;
       }

        function save () {
       bootbox.confirm({
           message: '<div class="gray-font font-15">¿Seguro que desea reportar una emergencia?</div>',
           closeButton: false,

           buttons: {
               confirm: {
                   label: 'Reportar emergencia',
                   className: 'btn-success'
               },
               cancel: {
                   label: 'Cancelar',
                   className: 'btn-danger'
               }
           },
           callback: function(result) {
               if (result) {
               formatValidEmergency();
                 WSEmergency.sendActivity(vm.emergency);
                        toastr["success"]("Se ha reportado la emergencia, enseguida será notificada a los oficiales");
                        vm.emergency = undefined;
                        $state.go('residentByHouse');
               }
           }
       })

        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:emergencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
