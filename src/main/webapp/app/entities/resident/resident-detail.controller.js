(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDetailController', ResidentDetailController);

    ResidentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Resident', 'User', 'Company', 'House','Principal'];

    function ResidentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Resident, User, Company, House,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        $rootScope.active = "residents";
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.isReady = false;

        if (vm.resident.isOwner == 1) {
            vm.authorizer = "SI";
        } else {
            vm.authorizer = "NO";
        }
        if(vm.resident.email== "" || vm.resident.email == null){
            vm.resident.email = "No registrado";
        }
        if(vm.resident.userLogin== "" || vm.resident.userLogin == null){
            vm.resident.userLogin = "No registrado";
        }
        if(vm.resident.type==1){
            vm.resident.type = "Residente propietario";
        }else if(vm.resident.type==2){
            vm.resident.type = "Residente inquilino";
        }else if(vm.resident.type==3){
            vm.resident.type = "Visitante autorizado";
        }
        if(vm.resident.phonenumber== "" || vm.resident.phonenumber == null){
            vm.resident.phonenumber = "No registrado";
        }
        House.get({id:vm.resident.houseId},onSuccessHouses);
        function onSuccessHouses(house, headers) {
          vm.resident.houseId = house.housenumber;
            if(house.housenumber==9999){
               vm.resident.houseId="Oficina"
            }
          $("#residentInformation").fadeIn(300);
          if (house.securityKey == null) {
              vm.securitykey = "No definida"
          } else {
              vm.securitykey = house.securityKey;
          }
          if (house.emergencyKey == null) {
              vm.emergencykey = "No definida"
          } else {
              vm.emergencykey = house.emergencyKey;
          }
          vm.isReady = true;
        }


        vm.title = vm.resident.name + " " + vm.resident.lastname + " " + vm.resident.secondlastname;
        var unsubscribe = $rootScope.$on('aditumApp:residentUpdate', function(event, result) {
            vm.resident = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
