(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChangeWatchesController', ChangeWatchesController);

    ChangeWatchesController.$inject = ['$rootScope','CommonMethods','AlertService',  '$uibModalInstance', 'Principal','Officer','Watch'];

    function ChangeWatchesController($rootScope,CommonMethods,AlertService, $uibModalInstance, Principal,Officer,Watch) {

        var vm = this;
        vm.clear = clear;
        vm.isAuthenticated = Principal.isAuthenticated;

             Officer.query({
               companyId: $rootScope.companyId
            }, onSuccess, onError);

            function onSuccess(data, headers) {

               vm.officers = data;
                $("#loadingIcon").fadeOut(0);
                setTimeout(function() {
                    $("#officers_turn_container").fadeIn(300);
                }, 200)

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }



            function clear () {
                $uibModalInstance.dismiss('cancel');
            }

            vm.officersLinked = []
            vm.moveToLink = function(officer) {
                  var index = vm.officersLinked.indexOf(officer);
                  vm.officersLinked.splice(index, 1);
                  vm.officers.push(officer);
            }
            vm.moveToLinked = function(officer) {

                    var index = vm.officers.indexOf(officer);
                    vm.officers.splice(index, 1);
                    var item = officer;
                    vm.officersLinked.push(item);
             }

                vm.reportTurn = function() {
                    vm.isSaving = true;
                    if (vm.officersLinked.length == 0) {
                           console.log('dsaf');
                        toastr["error"]("Debe elegir al menos un oficial para reportar el turno");
                         vm.isSaving = false;
                    } else {
                           console.log('dsaf');
                    var watch = {
                        initialTime:  moment(new Date()).format(),
                        responsableofficer: serializeOfficers(vm.officersLinked),
                        companyId: $rootScope.companyId
                    }

                    Watch.save(watch, onSaveSuccess, onSaveError);

                    }
                }
                  function onSaveSuccess (result) {

                            $uibModalInstance.close(result);
                            vm.isSaving = false;
                            toastr["success"]("Se registró el turno correctamente");
                        }

                             function onSaveError () {
                                    vm.isSaving = false;
                                }
//               FUNCION PARA SERIALIZAR OFICIALES
                 function serializeOfficers(officers){
                     var responsableofficers = "";

                     angular.forEach(officers,function(officer,key){
                        responsableofficers += formatOfficer(officer)+"||";
                     })
                     function formatOfficer(officer){
                        var fullName = officer.name+" "+officer.lastname+" "+officer.secondlastname;
                        return officer.id+";"+officer.identificationnumber+";"+fullName;
                      }
                     console.log(responsableofficers);
                   return responsableofficers;
                 }

    }
})();
