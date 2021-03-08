(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDetailController', ResidentDetailController);

    ResidentDetailController.$inject = ['Modal','$state','$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Resident', 'User', 'Company', 'House','Principal'];

    function ResidentDetailController(Modal,$state,$scope, $rootScope, $stateParams, previousState, DataUtils, entity, Resident, User, Company, House,Principal) {
        var vm = this;
        Principal.identity().then(function (account) {
            switch (account.authorities[0]) {
                case "ROLE_MANAGER":
                    $rootScope.active = "residents";
                    break;
                case "ROLE_USER":
                    $rootScope.active = "residentsHouses";
                    break;

            }
        })
        vm.editResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('residentByHouse.edit', {
                id: encryptedId
            })
        }

        vm.deleteResident = function (resident) {
            vm.residentToDelete = resident;
            Modal.confirmDialog("¿Está seguro que desea eliminar al usuario " + resident.name + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    vm.login = resident.userLogin;
                    Resident.delete({
                        id: resident.id
                    }, function () {
                        if (vm.login !== null) {
                            User.getUserById({
                                id: resident.userId
                            }, function (data) {
                                data.activated = 0;
                                data.email = data.email + Math.floor(Math.random() * 1000000000);
                                data.login = data.email;
                                User.update(data, onSuccessDisabledUser);

                                function onSuccessDisabledUser(data, headers) {
                                    Modal.toast("Se ha eliminado el usuario correctamente.");
                                    Modal.hideLoadingBar();
                                    vm.filterResidents();
                                }
                            });
                        } else {
                            Modal.toast("Se ha eliminado el usuario correctamente.");
                            Modal.hideLoadingBar();
                            $rootScope.back();
                            WSDeleteEntity.sendActivity({type: 'resident', id: vm.residentToDelete.id})
                        }

                    });
                });
        };
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.isReady = false;
        $rootScope.mainTitle = 'Detalle de usuario';
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

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
            vm.resident.type = "Propietario residente";
        }else if(vm.resident.type==2){
            vm.resident.type = "Propietario arrendador";
        }else if(vm.resident.type==3){
            vm.resident.type = "Residente";
        }else if(vm.resident.type==4){
            vm.resident.type = "Inquilino";
        }
        if(vm.resident.phonenumber== "" || vm.resident.phonenumber == null){
            vm.resident.phonenumber = "No registrado";
        }


        if(vm.resident.houseId!=null) {
            House.get({id: vm.resident.houseId}, onSuccessHouses);
        }

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
