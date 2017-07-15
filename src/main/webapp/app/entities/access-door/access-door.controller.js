(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorController', AccessDoorController);

    AccessDoorController.$inject = ['Auth','$state','$scope','$rootScope','CommonMethods','AccessDoor', 'Resident' ,'House','Vehicule','Visitant','Note','AlertService', 'Principal','$filter','companyUser','WSDeleteEntity','WSEmergency','WSHouse','WSResident','WSVehicle','WSNote','WSVisitor'];

    function AccessDoorController(Auth,$state,$scope,$rootScope,CommonMethods, AccessDoor, Resident, House,Vehicule,Visitant,Note,AlertService,Principal,$filter,companyUser,WSDeleteEntity,WSEmergency,WSHouse,WSResident,WSVehicle,WSNote,WSVisitor) {
        var vm = this;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        var residentsList, vehiculesList, housesList, emergencyList, visitorsList,invitedList;


        var securityKey, emergencyKey, housenumber;
        vm.hideEmergencyForm = 1;
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
               loadInvited();
           }
    }
        function loadInvited() {
            Visitant.findAllInvited({companyId: $rootScope.companyId}, onSuccessInvited, onError);
            function onSuccessInvited(visitors, headers) {
                invitedList = visitors;
                subscribe();
                loadNotes();
            }
        }
    function loadNotes() {
            moment.locale('es');
            Note.findAll({companyId: $rootScope.companyId},onSuccessNotes, onError);
            function onSuccessNotes(notes, headers) {
              angular.forEach(notes,function(key,note){
                    key.creationdate = moment(key.creationdate).fromNow();

                })
                vm.notes = notes;
                vm.countNotes = vm.notes.length;
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


        vm.deleteDomicilioReports = function(note){
                 Note.delete({
                    id: note.id
                }, onSuccessDelete);
        }

     function onSuccessDelete (result) {
                toastr["success"]("Eliminado");
                loadNotes();
                WSDeleteEntity.sendDeletedEntity({type:'resident',id:result.id})


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
                                var house = vm.setHouse(item.houseId);
                                vm.SelectedHouse = house.housenumber;
                                housenumber = house.housenumber;
                                securityKey = house.securityKey;
                                emergencyKey = house.emergencyKey;
                                if (item.enabled == 0) {
                                    vm.vehiculeRegisteredTitle = "Vehículo no habilitado para ingresar";
                                    vm.colorVehiculeRegistered = "red-font";
                                    vm.imageVehiculeState = "disabled-car-image";
                                }
                            }
                        });
                        if (vm.id_vehicule >= 5) {
                            angular.forEach(invitedList, function(itemVisitor, index) {
                                if (itemVisitor.licenseplate == vm.id_vehicule && itemVisitor.isinvited == 1) {
                                    vm.invited_visitant_name = itemVisitor.name;
                                    vm.invited_visitant_last_name = itemVisitor.lastname;
                                    vm.invited_visitant_second_last_name = itemVisitor.secondlastname;
                                    if (itemVisitor.licenseplate == null ||itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
                                        vm.invited_visitant_license_plate = "Ninguna";

                                    } else {
                                        vm.invited_visitant_license_plate = itemVisitor.licenseplate;
                                    }
                                    vm.invited_visitant_indentification = itemVisitor.identificationnumber;
                                    var house = vm.setHouse(itemVisitor.houseId);
                                    vm.invited_visitant_house_number = house.housenumber;
                                    vm.show = 10;
                                    $("#visitantInvitedtAccess").fadeIn(100);
                                }
                            });

                        }
                    }

        }
        vm.getVisitor = function() {
            if (vm.visitor_id_number == "") {
                clearInputs();
            } else {

                angular.forEach(visitorsList, function(itemVisitor, index) {

                    if (itemVisitor.identificationnumber == vm.visitor_id_number && itemVisitor.isinvited == 3) {

                        vm.visitor_name = itemVisitor.name;
                        vm.visitor_last_name = itemVisitor.lastname;
                        vm.visitor_second_last_name = itemVisitor.secondlastname;
                        vm.visitor_license_plate = itemVisitor.licenseplate;
                        setHouse(itemVisitor.houseId);

                    }
                });
            }
        }

        vm.getKeyInformation = function() {
            var existe=0;
            angular.forEach(housesList, function(item, index) {
                if (item.securityKey == vm.security_key) {
                    existe =1;
                    vm.show = 9;
                    vm.emergencySecurityKeyTitle = "Clave de seguridad";
                    vm.emergency_security_key = item.emergencyKey;
                    vm.key_house_number =item.housenumber;
                    var houseId = item.id;
                    Resident.findResidentesEnabledByHouseId({
                        houseId: houseId
                    }).$promise.then(onSuccessResidents, onError);
                } else if (item.emergencyKey == vm.security_key) {
                    existe =1;
                    vm.show = 9;
                    vm.emergencySecurityKeyTitle = "Clave de emergencia";
                    vm.emergency_security_key = item.securityKey;
                    var houseId = item.id;
                    Resident.findResidentesEnabledByHouseId({
                        houseId: houseId
                    }).$promise.then(onSuccessResidents, onError);

                }
                function onSuccessResidents(data) {
                    vm.residents = data;
                    Vehicule.findVehiculesEnabledByHouseId({
                        houseId: item.id
                    }).$promise.then(onSuccessVehicules, onError);
                }

                function onSuccessVehicules(data) {
                    vm.vehicules = data;
                }

            });

            if(existe==0){
                toastr["error"]("No existe la clave de seguridad o emergencia que ingresó");
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
                    console.log(vm.id_number);
                    angular.forEach(residentsList, function(item, index) {
                        if (item.identificationnumber == vm.id_number) {
                            vm.residentRegisteredTitle = "Residente registrado"
                            vm.colorResidentRegistered = "green-font"
                            $("#residentAccess").fadeIn(100);
                            vm.show = 1;
                            vm.resident = item;
                            var house =vm.setHouse(item.houseId);
                            housenumber = house.housenumber;
                            vm.housenumber = house.housenumber;
                            securityKey = house.securityKey;
                            emergencyKey = house.emergencyKey;
                            if (item.enabled == 0) {
                                vm.residentRegisteredTitle = "Residente no habilitado para ingresar";
                                vm.colorResidentRegistered = "red-font";
                            }
                        }
                    });

                   if (vm.id_number.length >= 6) {
                       angular.forEach(invitedList, function(itemVisitor, index) {


                           if (itemVisitor.identificationnumber == vm.id_number && itemVisitor.isinvited == 1) {
                               console.log('el estado es'+itemVisitor.isinvited);
                               vm.invited_visitant_name = itemVisitor.name;
                               vm.invited_visitant_last_name = itemVisitor.lastname;
                               vm.invited_visitant_second_last_name = itemVisitor.secondlastname;
                               if (itemVisitor.licenseplate == null ||itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
                                   vm.invited_visitant_license_plate = "Ninguna";

                               } else {
                                   vm.invited_visitant_license_plate = itemVisitor.licenseplate;
                               }
                               vm.invited_visitant_indentification = itemVisitor.identificationnumber;
                               var house = vm.setHouse(itemVisitor.houseId);
                               vm.invited_visitant_house_number = house.housenumber;
                               vm.show = 10;
                               $("#visitantInvitedtAccess").fadeIn(100);
                           }
                       });

                   }
                }
            };

        vm.insert_visitant_invited = function() {
            var idHouse;
            angular.forEach(housesList, function(house, index) {

                if (house.housenumber == vm.invited_visitant_house_number) {
                    idHouse = house.id;

                }
            })

            var visitant = {
                name: CommonMethods.capitalizeFirstLetter(vm.invited_visitant_name),
                lastname: CommonMethods.capitalizeFirstLetter(vm.invited_visitant_last_name),
                secondlastname: CommonMethods.capitalizeFirstLetter(vm.invited_visitant_second_last_name),
                identificationnumber: vm.invited_visitant_indentification,
                licenseplate: vm.invited_visitant_license_plate.toUpperCase(),
                companyId: $rootScope.companyId,
                isinvited: 3,
                arrivaltime: moment(new Date()).format(),
                houseId: idHouse
            }
            Visitant.save(visitant, onSaveSuccess, onSaveError);

            function onSaveSuccess (result) {
                vm.show=4;
                vm.isSaving = false;
                visitorsList.push(result);
                toastr["success"]("Se registró la entrada del visitante correctamente.");
            }

        }
            vm.setHouse = function(id){
                var house;
                angular.forEach(housesList, function(itemHouse, index) {
                    if (itemHouse.id == id) {
                        house = itemHouse;
                    }

                });
                return house;

            }
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
                vm.houses = housesList;
                if (vm.id_number == undefined || vm.id_number == "") {
                } else {
                    vm.visitor_id_number = vm.id_number;
                    angular.forEach(visitorsList, function(itemVisitor, index) {
                        if (itemVisitor.identificationnumber == vm.visitor_id_number && itemVisitor.isinvited == 3) {
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
                    licenseplate: vm.visitor_license_plate.toUpperCase(),
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
                 visitorsList.push(result);
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

        vm.getEmergency = function(emergency) {

            var house = vm.setHouse(emergency.houseId);
            vm.house_number_emergency = house.housenumber;
            vm.hideRegisterForm = 1;
            vm.hideEmergencyForm = 2;
            vm.show = 7;



        }

//BEGIN WEB SOCKETS

//JhiTrackerService.receiveResident().then(null, null, receiveResident);
//JhiTrackerService.receiveVehicle().then(null, null, receiveVehicle);
//JhiTrackerService.receiveHouse().then(null, null, receiveHouse);
//JhiTrackerService.receiveVisitor().then(null, null, receiveVisitor);
//JhiTrackerService.receiveHomeService().then(null, null, receiveHomeService);
//JhiTrackerService.receiveDeletedEntity().then(null, null, receiveDeletedEntity);


vm.attendEmergency = function(){
var codeEmegency = $rootScope.companyId+""+vm.emergency.houseId;
vm.emergency.isattended = 1;
    vm.hideRegisterForm = 2;
    vm.hideEmergencyForm = 1;
    toastr["success"]("Se ha reportado al residente que se atenderá la emergencia");
WSEmergency.sendActivityAttended(codeEmegency,vm.emergency);
}


function receiveEmergency(emergency){
    vm.getEmergency(emergency);
    vm.emergency = emergency;

}

var hasExistance = function(array,id) {
var index = undefined;
   angular.forEach(array,function(item,i){
           if (item.id === id) {
               index = i;
           }else{
           index = -1;
          }
   })
return index;
};


function receiveVisitor(visitor){


    if(invitedList!==undefined){
        var result = hasExistance(invitedList,visitor.id)
        if(result!==-1){
            invitedList[result] = visitor;
        }else{
            invitedList.push(visitor);
        }

    }


// if(invitedList!==undefined){
//     var result = undefined;
//     angular.forEach(invitedList,function(item,i){
//         if (item.id === visitor.id) {
//             result = i;
//         }else{
//             result = -1;
//         }
//     });
// if(result!==-1){
//     invitedList[result] = visitor;
// }else{
//     invitedList.push(visitor);
// }
// }
}

function receiveHomeService(homeService){

    if(vm.notes!==undefined){
        var result = hasExistance(vm.notes,homeService.id)

        if(result==undefined||result==-1){
        homeService.creationdate = moment(homeService.creationdate).fromNow();
            vm.notes.push(homeService);
            vm.countNotes = vm.notes.length;
        }

    }
}



function receiveResident(resident){
        if(residentsList!==undefined){
            var result = hasExistance(residentsList,resident.id)
            if(result!==-1){
                  residentsList[result] = resident;
            }else{
                  residentsList.push(resident);
            }
        }
}

function receiveVehicle(vehicle){
if(vehiculesList!==undefined){
var result =  hasExistance(vehiculesList,vehicle.id)
console.log()
if(result!==-1){
vehiculesList[result] = vehicle;
}else{
vehiculesList.push(vehicle);
}
}
}

function receiveHouse(house){
if(housesList!==undefined){
var result = hasExistance(housesList,house.id)
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
            var result = hasExistance(residentsList,entity.id)
            if(result!==-1){
                residentsList[result] = {};
            }
         }
        break;
        case 'vehicle':
         if(vehiculesList!==undefined){
             var result = hasExistance(vehiculesList,entity.id)
             if(result!==-1){
                 vehiculesList[result] = {};
             }
        }
        break;
        case 'visitor':
          if(visitorsList!==undefined){
              var result = hasExistance(visitorsList,entity.id)
              if(result!==-1){
                  visitorsList[result] = {};
              }
        }
        break;
    }
}
//END WEBSOCKETS
}
})();
