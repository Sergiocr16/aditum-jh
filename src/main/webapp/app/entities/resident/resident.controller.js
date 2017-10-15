(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentController', ResidentController);

    ResidentController.$inject = ['$state','DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope','WSResident','WSDeleteEntity'];

    function ResidentController($state,DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope,WSResident,WSDeleteEntity) {
        $rootScope.active = "residents";
        var enabledOptions = true;
        var vm = this;
         vm.radiostatus=true;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.editResident = function(id){
        var encryptedId = CommonMethods.encryptIdUrl(id)
                   $state.go('resident.edit', {
                       id: encryptedId
                   })
        }

        vm.detailResident = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
                    $state.go('resident-detail', {
                        id: encryptedId
                    })
        }
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.filterAuthorized = 2;
        vm.setAuthorizedView = function(val){
                vm.filterAuthorized = val;
        }
        vm.editResident = function(id){
        var encryptedId = CommonMethods.encryptIdUrl(id)
                   $state.go('resident.edit', {
                       id: encryptedId
                   })
        }



        vm.detailResident = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
                    $state.go('resident-detail', {
                        id: encryptedId
                    })
        }

        vm.changesTitles = function() {
            if (enabledOptions) {
                vm.title = "Residentes habilitados";
                vm.buttonTitle = "Ver residentes deshabilitados";
                vm.actionButtonTitle = "Deshabilitar";
            } else {
                vm.title = "Residentes deshabilitados";
                vm.buttonTitle = "Ver residentes habilitados";
                vm.actionButtonTitle = "Habilitar";
            }
        }
        setTimeout(function(){
         loadHouses();
        },500)


        function loadHouses() {
            House.query({
                companyId: $rootScope.companyId
            }).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
                loadResidents();
            }

        }

        function loadResidents(option) {
            if (enabledOptions) {
                vm.changesTitles();
                Resident.residentsEnabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: $rootScope.companyId,
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Resident.residentsDisabled({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    companyId: $rootScope.companyId,
                }).$promise.then(onSuccess, onError);
            }

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                if (option !== 1) {
                    vm.queryCount = data.length;
                    vm.page = pagingParams.page;
                    vm.residents = formatResidents(data);
                } else {
                    var residentsByHouse = [];
                    vm.residents = data;
                    for (var i = 0; i < vm.residents.length; i++) {
                        if (vm.house.id === vm.residents[i].houseId) {
                            residentsByHouse.push(vm.residents[i])
                        }
                    }
                    vm.residents = formatResidents(residentsByHouse);
                }

                setTimeout(function() {
                          $("#loadingIcon").fadeOut(300);
                }, 400)
                 setTimeout(function() {
                     $("#tableData").fadeIn('slow');
                 },700 )
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        vm.switchEnabledResidents = function() {
            enabledOptions = true;
             vm.radiostatus=true;
            vm.findResidentsByHouse(vm.house);
             $("#radio18").prop("checked", "checked")
        }
        vm.switchDisabledResidents = function() {
            enabledOptions = false;
            vm.findResidentsByHouse(vm.house);
             $("#radio19").prop("checked", "checked")
        }
        vm.findResidentsByHouse = function(house) {
             $("#tableData").fadeOut(0);
             setTimeout(function() {
                 $("#loadingIcon").fadeIn(100);
             }, 200)
            vm.house = house;
            if (house == undefined) {
                loadResidents();
            } else {
                loadResidents(1);
            }
        }

        function formatResidents(residents) {
            var formattedResidents = [];
            for (var i = 0; i < residents.length; i++) {

                for (var e = 0; e < vm.houses.length; e++) {
                    if (residents[i].houseId == vm.houses[e].id) {
                        residents[i].house_id = vm.houses[e].housenumber;
                        residents[i].name = residents[i].name + " " + residents[i].lastname;
                        if(residents[i].email==null){residents[i].email = "No registrado"};
                        if(residents[i].phonenumber==null){residents[i].phonenumber = "No registrado"};
                    }
                }
            }

            return residents;
        }

        vm.deleteResident = function(resident) {
        vm.residentToDelete = resident;
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar al residente " + resident.name + "?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        vm.login = resident.userLogin;
                        Resident.delete({
                            id: resident.id


                        }, onSuccessDelete);


                    }
                }
            });


        };
        function onSuccessDelete (result) {
        console.log(result)
            if(vm.login!==null){
                User.delete({login: vm.login},
                    function () {
                        toastr["success"]("Se ha eliminado el residente correctamente.");
                        loadResidents();
                        WSDeleteEntity.sendActivity({type:'resident',id:vm.residentToDelete.id})
                    });
            } else {
                toastr["success"]("Se ha eliminado el residente correctamente.");
                loadResidents();
                WSDeleteEntity.sendActivity({type:'resident',id:vm.residentToDelete.id})
            }

        }

        vm.disableEnabledResident = function(residentInfo) {

            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + residentInfo.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + residentInfo.name + "?";
            }
            bootbox.confirm({

                message: correctMessage,

                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {

                        CommonMethods.waitingMessage();
                        Resident.get({id: residentInfo.id}).$promise.then(onSuccessGetResident);

                    }
                }
            });
        };


        function onSuccessGetResident (result) {
            enabledDisabledResident(result);
        }

        function enabledDisabledResident(resident){
            if (enabledOptions) {
                resident.enabled = 0;
                Resident.update(resident, onSuccessDisabledResident);
            } else {
                resident.enabled = 1;
                Resident.update(resident, onSuccessEnabledResident);

            }
        }

        function onSuccessDisabledResident(data, headers) {
           WSResident.sendActivity(data);
            if (data.isOwner == 1) {
                User.getUserById({
                    id: data.userId
                }, onSuccessGetDisabledUser);

            } else {
                loadResidents();

                toastr["success"]("Se ha deshabilitado el residente correctamente.");
                bootbox.hideAll();
            }
        }

        function onSuccessGetDisabledUser(data, headers) {
            data.activated = 0;
            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {

                toastr["success"]("Se ha deshabilitado el residente correctamente.");
                bootbox.hideAll();
                loadResidents();
            }
        }


        function onSuccessEnabledResident(data, headers) {
             WSResident.sendActivity(data);
            if (data.isOwner == 1) {
                User.getUserById({
                    id: data.userId
                }, onSuccessGetEnabledUser);

            } else {
                bootbox.hideAll();
                toastr["success"]("Se ha habilitado el residente correctamente.");
                loadResidents();
            }
        }

        function onSuccessGetEnabledUser(data, headers) {
            data.activated = 1;
            User.update(data, onSuccessEnabledUser);
            function onSuccessEnabledUser(data, headers) {

                toastr["success"]("Se ha habilitado el residente correctamente.");
                bootbox.hideAll();
                loadResidents();
            }
        }
        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
