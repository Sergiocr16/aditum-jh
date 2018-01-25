(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorInvitedByHouseController', AccessDoorInvitedByHouseController);

    AccessDoorInvitedByHouseController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Visitant','$rootScope','CommonMethods'];

    function AccessDoorInvitedByHouseController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Visitant,$rootScope,CommonMethods ) {
        var vm = this;

        vm.house = entity;
        vm.clear = clear;
 vm.loading = true;
        vm.visitantByHouseList = [];
        setTimeout(function(){
         loadAll();
        },500)
                var hasExistance = function(array, id) {
                    var index = undefined;
                    angular.forEach(array, function(item, i) {
                        if (parseInt(item.id) === parseInt(id)) {
                            index = i;
                        } else {
                            index = -1;
                        }
                    })
                    return index;
                };
            var visitantByHouseList = [];
            vm.loadingVisitantByHouseIndex = 1
        vm.verifyVisitantInivitedDate = function(visitant) {
            var currentTime = new Date(moment(new Date()).format("YYYY-MM-DD") + "T" + moment(new Date()).format("HH:mm:ss") + "-06:00").getTime();
            var initTime = new Date(visitant.invitationstaringtime).getTime();
            var finishTime = new Date(visitant.invitationlimittime).getTime();


            if (initTime <= currentTime && currentTime <= finishTime) {
                return true;
            } else {
                visitant.isinvited = 2;
                Visitant.update(visitant, function() {})
                if (visitantByHouseList !== undefined) {
                    var result = hasExistance(visitantByHouseList, visitant.id)
                    if (result !== -1) {
                        visitantByHouseList[result] = {};
                    }
                }
                return false;
            }
        }
            function loadAll() {
                Visitant.findInvitedByHouse({
                    companyId: $rootScope.companyId,
                    houseId: vm.house.id
                }).$promise.then(onSuccess);

                function onSuccess(data) {

                    $("#loandingVisitantByHouseIndex").fadeOut(0);
                    $("#visitantByHouseIndex").fadeIn(400);

                    angular.forEach(data, function(itemVisitor, key) {
                        if (itemVisitor.isinvited == 1) {
                            var visitantInvited = {}
                            if (vm.verifyVisitantInivitedDate(itemVisitor)) {
                                visitantInvited.id = itemVisitor.id;
                                visitantInvited.name = itemVisitor.name;
                                visitantInvited.last_name = itemVisitor.lastname;
                                visitantInvited.second_last_name = itemVisitor.secondlastname;
                                visitantInvited.invitation_staring_time = itemVisitor.invitationstaringtime;
                                visitantInvited.invitation_limit_time = itemVisitor.invitationlimittime;
                                if (itemVisitor.licenseplate == null || itemVisitor.licenseplate == undefined || itemVisitor.licenseplate == "") {
                                    visitantInvited.hasLicense = false;
                                } else {
                                    visitantInvited.license_plate = itemVisitor.licenseplate;
                                    visitantInvited.hasLicense = true;
                                }
                                if (itemVisitor.identificationnumber == null || itemVisitor.identificationnumber == undefined || itemVisitor.identificationnumber == "") {
                                    visitantInvited.hasIdentification = false;
                                } else {
                                    visitantInvited.indentification = itemVisitor.identificationnumber;
                                    visitantInvited.hasIdentification = true;
                                }
                                vm.visitantListHouse = vm.house;
                                if (vm.house.housenumber == 9999) {
                                    vm.visitantListHouse = "Oficina";
                                }
                                visitantInvited.house_number = vm.house.housenumber;
                                visitantByHouseList.push(visitantInvited);
                            }
                        }
                    })
                    vm.visitantByHouseList = visitantByHouseList;
                    vm.loading = false;
                }

                function onError(error) {
                vm.loading = false;
                    AlertService.error(error.data.message);
                }
            }
        vm.registerVisitantFromVisitantsList = function(visitant) {



            if (visitant.indentification == "" && visitant.hasIdentification == false || visitant.indentification == null && visitant.hasIdentification == false || visitant.indentification == undefined && visitant.hasIdentification == false) {
                toastr["error"]("Debe ingresar el número de cédula para registrar la visita.");
                $("#" + visitant.id).css({
                    "border-color": "red",
                    "border-weight": "2px",
                    "border-style": "solid"
                });
            } else {



                vm.visitantToInsert = visitant;
                bootbox.confirm({
                    message: "¿Está seguro que desea registrar la visita de " + visitant.name + " " + visitant.last_name + "?",
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
                            vm.insertingVisitant = 1;
                            var temporalLicense;

                            if (vm.visitantToInsert.license_plate !== undefined) {
                                temporalLicense = vm.visitantToInsert.license_plate.toUpperCase();
                            }
                            var visitante = {
                                name: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.name),
                                lastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.last_name),
                                secondlastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.second_last_name),
                                identificationnumber: vm.visitantToInsert.indentification.toUpperCase(),
                                licenseplate: temporalLicense,
                                companyId: $rootScope.companyId,
                                isinvited: 3,
                                arrivaltime: moment(new Date()).format(),
                                houseId: vm.visitantListHouse.id
                            }

                            Visitant.save(visitante, onSaveSuccess, onSaveError);

                            function onSaveSuccess(result) {
                                if (visitant.hasLicense == false) {
                                    temporalLicense = null;
                                }
                                if (visitant.hasLicense == false || visitant.hasLicense == true) {

                                    var visitante2 = {
                                        id: vm.visitantToInsert.id,
                                        name: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.name),
                                        lastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.last_name),
                                        secondlastname: CommonMethods.capitalizeFirstLetter(vm.visitantToInsert.second_last_name),
                                        identificationnumber: vm.visitantToInsert.indentification.toUpperCase(),
                                        licenseplate: temporalLicense,
                                        companyId: $rootScope.companyId,
                                        isinvited: 1,
                                        invitationstaringtime: vm.visitantToInsert.invitation_staring_time,
                                        invitationlimittime: vm.visitantToInsert.invitation_limit_time,
                                        houseId: vm.visitantListHouse.id
                                    }
//                                    visitantByHouseList.push(result);
                                    Visitant.update(visitante2, onUpdateSuccess, onUpdateError);
                                }

                            }

                            function onUpdateSuccess(result) {
                                vm.insertingVisitant = 2;
                                toastr["success"]("Se registró la entrada del visitante correctamente.");

                            }

                            function onUpdateError(error) {
                                AlertService.error(error.data.message);
                            }
                        }
                    }
                });

            }

        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:accessDoorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
