(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['Auth','$state','$scope','$rootScope','CommonMethods','AccessDoor', 'Resident' ,'House','Vehicule','Visitant','AlertService', 'Principal','$filter','companyUser','WSDeleteEntity','WSEmergency','WSHouse','WSResident','WSVehicle','WSNote','WSVisitor'];

    function AccessDoorController(Auth,$state,$scope,$rootScope,CommonMethods, AccessDoor, Resident, House,Vehicule,Visitant,AlertService,Principal,$filter,companyUser,WSDeleteEntity,WSEmergency,WSHouse,WSResident,WSVehicle,WSNote,WSVisitor) {
        var vm = this;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        var residentsList, vehiculesList, housesList, emergencyList, visitorsList;
        var securityKey, emergencyKey, housenumber;

        vm.logout = logout;
        vm.show = 4;

        vm.isAuthenticated = Principal.isAuthenticated;
        function logout() {
            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.showLogin = true;
            unsubscribe();
        }
        loadResidents();

       function unsubscribe(){
       WSDeleteEntity.unsubscribe($rootScope.companyId);
       WSEmergency.unsubscribe($rootScope.companyId);
       WSHouse.unsubscribe($rootScope.companyId);
       WSResident.unsubscribe($rootScope.companyId);
       WSVehicle.unsubscribe($rootScope.companyId);
       WSNote.unsubscribe($rootScope.companyId);
       WSVisitor.unsubscribe($rootScope.companyId);
       }
        function loadResidents() {
            Resident.query({companyId: $rootScope.companyId}, onSuccessResident, onError);
            function onSuccessResident(residents, headers) {
               residentsList = residents;
               loadHouses();
            }
        }

        function loadHouses() {
           House.query({companyId: $rootScope.companyId}, onSuccessHouse, onError);
           function onSuccessHouse(houses, headers) {
              housesList = houses;
              loadVehicules()
           }

        }

        function loadVehicules() {
           Vehicule.query({companyId: $rootScope.companyId}, onSuccessVehicule, onError);
           function onSuccessVehicule(vehicules, headers) {
              vehiculesList = vehicules;
             loadVisitors()
           }
        }

    function loadVisitors() {
           Visitant.query({companyId: $rootScope.companyId}, onSuccessVisitor, onError);
           function onSuccessVisitor(visitors, headers) {
              visitorsList = visitors;
              subscribe();
           }
        }
        function subscribe(){
          if($state.current.name==="main-access-door"){
                WSDeleteEntity.subscribe($rootScope.companyId);
                WSEmergency.subscribe($rootScope.companyId);
                WSHouse.subscribe($rootScope.companyId);
                WSResident.subscribe($rootScope.companyId);
                WSVehicle.subscribe($rootScope.companyId);
                WSNote.subscribe($rootScope.companyId);
                WSVisitor.subscribe($rootScope.companyId);
                WSDeleteEntity.receive().then(null, null, receiveDeletedEntity);
                WSEmergency.receive().then(null, null, receiveEmergency);
                WSHouse.receive().then(null, null, receiveHouse);
                WSResident.receive().then(null, null, receiveResident);
                WSVehicle.receive().then(null, null, receiveVehicle);
                WSNote.receive().then(null, null, receiveHomeService);
                WSVisitor.receive().then(null, null, receiveVisitor);
           }

        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.getVehicule = function(){
            vm.id_number = "";
            if (vm.id_vehicule == "") {
                 $("#vehicule_license_plate").css("text-transform", "none");
                 $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                  vm.show = 4;
            } else {
                 $("#vehicule_license_plate").css("text-transform", "uppercase");

                        vm.show = 3;
                        angular.forEach(vehiculesList, function(item, index) {
                            if (item.licenseplate.toUpperCase() == vm.id_vehicule.toUpperCase()) {
                                vm.vehiculeRegisteredTitle = "Vehículo registrado";
                                vm.colorVehiculeRegistered = "green-font";
                                vm.imageVehiculeState = "success-car-image";
                                $("#vehiculeAccess").fadeIn(100);
                                vm.show = 2;
                                vm.vehicule = item;


                                angular.forEach(housesList, function(itemHouse, index) {
                                    if (itemHouse.id == item.houseId) {
                                        vm.SelectedHouse = itemHouse.housenumber;
                                        housenumber = itemHouse.housenumber;
                                        securityKey = itemHouse.securityKey;
                                        emergencyKey = itemHouse.emergencyKey;
                                    }
                                });
                                if (item.enabled == 0) {
                                    vm.vehiculeRegisteredTitle = "Vehículo no habilitado para ingresar";
                                    vm.colorVehiculeRegistered = "red-font";
                                    vm.imageVehiculeState = "disabled-car-image";
                                }
                            }
                        });

                    }

        }

        vm.getResident = function() {
                vm.id_vehicule = "";
                $("#vehicule_license_plate").css("text-transform", "none");
                $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                if (vm.id_number == "") {
                    vm.show = 4;
                } else {
                    vm.show = 3;
                    angular.forEach(residentsList, function(item, index) {
                        if (item.identificationnumber == vm.id_number) {
                            vm.residentRegisteredTitle = "Residente registrado"
                            vm.colorResidentRegistered = "green-font"
                            console.log(item.enabled)
                            $("#residentAccess").fadeIn(100);
                            vm.show = 1;
                            vm.name = item.name + " " + item.lastname + " " + item.secondlastname;
                            vm.indentification = item.identificationnumber;
                            vm.resident = item;
                            angular.forEach(housesList, function(itemHouse, index) {
                                if (itemHouse.id == item.houseId) {
                                    housenumber = itemHouse.housenumber;
                                    vm.housenumber = itemHouse.housenumber;
                                    securityKey = itemHouse.securityKey;
                                    emergencyKey = itemHouse.emergencyKey;
                                }
                            });
                            if (item.enabled == 0) {
                                vm.residentRegisteredTitle = "Residente no habilitado para ingresar";
                                vm.colorResidentRegistered = "red-font";
                            }
                        }
                    });

//                    if ($scope.id_number.length > 6) {
//
//                        accessFunctions.findRegisteredVisitant($scope.id_number).success(function(data) {
//
//                            if (data == 0) {
//
//                            } else {
//                                $("#loadingIconnn").fadeOut(0);
//                                $("#visitantInvitedtAccess").fadeIn(0);
//                                $scope.show = 10;
//                                $scope.invited_visitant_name = data.name
//                                $scope.invited_visitant_last_name = data.last_name;
//                                $scope.invited_visitant_second_last_name = data.second_last_name;
//                                angular.forEach(housesPrueba, function(itemHouse, index) {
//                                        if (itemHouse.id == data.id_house) {
//                                            $scope.invited_visitant_house_number = itemHouse.house_number;
//                                        }
//                                    })
//                                    // $scope.invited_visitant_house_number = data.id_house;
//                                $scope.invited_visitant_indentification = data.identification_number;
//                                if (data.license_plate == null) {
//                                    $scope.invited_visitant_license_plate = "Ninguna";
//
//                                } else {
//                                    $scope.invited_visitant_license_plate = data.license_plate;
//                                }
//
//
//                            }
//                        });
//                    }
                }
            };
            vm.capitalize = function(){
                if (vm.visitor_license_plate != "") {
                        $("#license_plate").css("text-transform", "uppercase");
                    } else {
                        $("#license_plate").css("text-transform", "none");
                        $("#license_plate").attr("placeholder", "Número placa (sin guiones)");

                    }
            }
            vm.getKeys = function() {
                if (securityKey == null || emergencyKey == null) {
                    toastr["error"]("Esta casa aún no tiene claves de seguridad asignadas");
                } else {
                    bootbox.dialog({
                        message: '<div class="text-center gray-font font-20"> <h1 class="font-30">Casa número <span class="font-30" id="key_id_house"></span></h1></div>\
                        <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de seguridad: <span class="font-20 bold" id="security_key">1134314</span></h1></div>\
                          <div class="text-center gray-font font-20"> <h1 class="font-20">Clave de emergencia: <span class="font-20 bold" id="emergency_key">1134314</span></h1></div>',
                        closeButton: false,
                        buttons: {
                            confirm: {
                                label: 'Ocultar',
                                className: 'btn-success'
                            }
                        },
                    })
                    document.getElementById("key_id_house").innerHTML = "" + housenumber;
                    document.getElementById("security_key").innerHTML = "" + securityKey;
                    document.getElementById("emergency_key").innerHTML = "" + emergencyKey;
                }
            }
            function clearInputs() {
                vm.visitor_id_number = ""
                vm.visitor_name = ""
                vm.visitor_last_name = "";
                vm.visitor_second_last_name = "";
                vm.visitor_license_plate = "";
                vm.house = "";
            }

            vm.searchVisitor = function() {
                vm.show = 5;
                $("#license_plate").css("text-transform", "none");
                $("#license_plate").attr("placeholder", "Número placa (sin guiones)");

                clearInputs();
                if (vm.id_number == undefined || vm.id_number == "") {

                } else {

                    vm.visitor_id_number = vm.id_number;
                    angular.forEach(visitorsList, function(itemVisitor, index) {
                        if (itemVisitor.identificationnumber == vm.visitor_id_number && itemVisitor.isinvited == 3) {
                        console.log('aa');
                            vm.visitor_name = itemVisitor.name;
                            vm.visitor_last_name = itemVisitor.lastname;
                            vm.visitor_second_last_name = itemVisitor.secondlastname;
                            vm.visitor_license_plate = itemVisitor.licenseplate;
                             setHouse(itemVisitor.houseId);


                        }
                    });
                }


                if (vm.id_vehicule == undefined || vm.id_vehicule == "") {} else {
                    vm.visitor_license_plate = vm.id_vehicule;
                    angular.forEach(visitorsList, function(itemVisitor, index) {
                        if (itemVisitor.licenseplate == vm.visitor_license_plate && itemVisitor.isinvited == 3) {
                         vm.visitor_name = itemVisitor.name;
                            vm.visitor_last_name = itemVisitor.lastname;
                            vm.visitor_second_last_name = itemVisitor.secondlastname;
                            vm.visitor_license_plate = itemVisitor.licenseplate;
                            vm.visitor_id_number = itemVisitor.identificationnumber;
                             setHouse(itemVisitor.houseId);
                        }
                    });
                }
            }

            function setHouse(house){
               vm.houses = housesList;
                for(var i=0;i<housesList.length;i++){
                   if(housesList[i].id==house){
                       vm.house = vm.houses[i];
                   }
                }
            }

            vm.insertVisitor = function() {
                var visitant = {
                    name: CommonMethods.capitalizeFirstLetter(vm.visitor_name),
                    lastname: CommonMethods.capitalizeFirstLetter(vm.visitor_last_name),
                    secondlastname: CommonMethods.capitalizeFirstLetter(vm.visitor_second_last_name),
                    identificationnumber: vm.visitor_id_number,
                    licenseplate: vm.visitor_license_plate,
                    companyId: $rootScope.companyId,
                    isinvited: 3,
                    arrivaltime: moment(new Date()).format(),
                    houseId: vm.house.id
                }
                    Visitant.save(visitant, onSaveSuccess, onSaveError);

            }

                function onSaveSuccess (result) {
                vm.show=4;
                    vm.isSaving = false;
                    toastr["success"]("Se registró la entrada del visitante correctamente.");
                }

                  function onSaveError () {
                            vm.isSaving = false;
                        }

            vm.deleteResidentVehiculeSpots = function() {
                $("#vehicule_license_plate").css("text-transform", "none");
                $("#vehicule_license_plate").attr("placeholder", "Número placa (sin guiones)");
                vm.id_number = "";
                vm.id_vehicule = "";
                vm.show = 4;

            }


//BEGIN WEB SOCKETS

//JhiTrackerService.receiveResident().then(null, null, receiveResident);
//JhiTrackerService.receiveVehicle().then(null, null, receiveVehicle);
//JhiTrackerService.receiveHouse().then(null, null, receiveHouse);
//JhiTrackerService.receiveVisitor().then(null, null, receiveVisitor);
//JhiTrackerService.receiveHomeService().then(null, null, receiveHomeService);
//JhiTrackerService.receiveDeletedEntity().then(null, null, receiveDeletedEntity);

function receiveEmergency(emergency){
console.log(emergency);
}
function receiveVisitor(visitor){
if(visitorsList!==undefined){
var result = visitorsList.indexOf(visitor);
if(result!==-1){
visitorsList[result] = visitor;
}else{
visitorsList.push(visitor);
}
}
}
function receiveHomeService(homeService){
console.log(homeService);
}

function receiveResident(resident){
if(residentsList!==undefined){
var result = residentsList.indexOf(resident);
if(result!==-1){
residentsList[result] = resident;
}else{
residentsList.push(resident);
}
}
}

function receiveVehicle(vehicle){
if(vehiculesList!==undefined){
var result = vehiculesList.indexOf(vehicle);
if(result!==-1){
vehiculesList[result] = vehicle;
}else{
vehiculesList.push(vehicle);
}
}
}

function receiveHouse(house){
if(housesList!==undefined){
var result = housesList.indexOf(house);
if(result!==-1){
housesList[result] = house;
}else{
housesList.push(house);
}
}
}
function receiveDeletedEntity(entity){
    switch(entity.type){
        case 'resident':
        if(residentsList!==undefined){
            residentsList = $filter('filter')(residentsList, function (item) {
               return item.id !== entity.id;
           });
         }
        break;
        case 'vehicle':
         if(vehiculesList!==undefined){
        vehiculesList = $filter('filter')(vehiculesList, function (item) {
                       return item.id !== entity.id;
           });
        }
        break;
        case 'visitor':
          if(visitorsList!==undefined){
            visitorsList = $filter('filter')(visitorsList, function (item) {
                 return item.id !== entity.id;
           });
        }
        break;
    }
}
//END WEBSOCKETS
}
})();
