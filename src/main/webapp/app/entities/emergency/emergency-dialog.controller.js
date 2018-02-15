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
        vm.enCamino = undefined;
       angular.element(document).ready(function(){
           setTimeout(function() {
                                        $("#loadingIcon").fadeOut(300);
                              }, 400)
                               setTimeout(function() {
                                   $("#all").fadeIn('slow');
                                   $("#normal").fadeIn('slow');
                               },900 )
       })

       setTimeout(function(){
         var emergencyCode = $rootScope.companyId+""+$rootScope.companyUser.houseId;
              WSEmergency.subscribeAttended(emergencyCode);
              WSEmergency.receiveAttented(emergencyCode).then(null,null,emergencyAttended)
              $rootScope.$on('$stateChangeStart',
              function(event, toState, toParams, fromState, fromParams){
                WSEmergency.unsubscribeAttended(emergencyCode);
              });
       },1200)

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
                        setTimeout(function(){
                        toastr["info"]("Mantente en esta ventana para ser notificado(a) cuando los oficiales hayan escuchado tu alerta y vengan en camino.");
                        },3000)

                        vm.emergency = undefined;
//                        $state.go('residentByHouse');
               }
           }
       })

        }


vm.closeIt = function(){

$('#calma').fadeOut(300)
setTimeout(function(){
$('#normal').fadeIn(300)
},300)
}
function emergencyAttended(emergency){


$('#normal').fadeOut(300)

setTimeout(function(){
$('#calma').fadeIn(300)
},300)
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
