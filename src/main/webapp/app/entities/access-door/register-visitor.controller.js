(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('RegisterVisitorController', RegisterVisitorController);

        RegisterVisitorController.$inject = ['Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal'];

        function RegisterVisitorController(Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, PadronElectoral, Destinies, globalCompany, Modal) {
            var vm = this;
            toastr.options = {
                "closeButton": true,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-bottom-full-width",
                "preventDuplicates": true,
                "onclick": null,
                "showDuration": "300",
                "hideDuration": "100000",
                "timeOut": "5000",
                "extendedTimeOut": "1000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "slideDown",
                "hideMethod": "slideUp",
            };
            vm.showLock = true;
            vm.visitorType = 1;
            CommonMethods.validateLetters();
            CommonMethods.validateNumbers();
            CommonMethods.validateSpecialCharacters();
            CommonMethods.validateSpecialCharactersAndVocals();
            vm.showLockCed = false;


            vm.getVisitor = function () {
                if (vm.visitor_id_number !== undefined && vm.visitor_id_number.length >= 9) {
                    Modal.showLoadingBar();
                    PadronElectoral.find(vm.visitor_id_number, personFinded, personNotFinded)
                }

                function personFinded(itemVisitor) {
                    setTimeout(function () {
                        $scope.$apply(function () {
                            vm.showLock = true;
                            vm.visitor_name = itemVisitor.nombre.split(",")[0] + "";
                            vm.visitor_last_name = itemVisitor.nombre.split(",")[1] + "";
                            vm.visitor_second_last_name = itemVisitor.nombre.split(",")[2] + "";
                            findLicensePlate(vm.visitor_id_number);
                            findHouse(vm.visitor_id_number)
                            vm.consultingPadron = false;
                            vm.showLockCed = true;
                            vm.founded = true;
                            Modal.hideLoadingBar();
                        })
                    }, 10)
                }

                function personNotFinded() {
                    vm.encontrado = 0;
                    for (var i = 0; i < $rootScope.visitorsList.length; i++) {
                        var itemVisitor = $rootScope.visitorsList[i];
                        if (itemVisitor.identificationnumber == vm.visitor_id_number && itemVisitor.isinvited == 3) {
                            vm.visitor_name = itemVisitor.name;
                            vm.visitor_last_name = itemVisitor.lastname;
                            vm.visitor_second_last_name = itemVisitor.secondlastname
                            vm.visitor_license_plate = itemVisitor.licenseplate;
                            setHouse(itemVisitor.houseId);
                            vm.encontrado = vm.encontrado + 1;
                        }
                    }
                    setTimeout(function () {
                        $scope.$apply(function () {
                            if (vm.encontrado > 0) {
                                vm.consultingPadron = false;
                                vm.founded = true;
                                vm.showLock = true;
                            } else {
                                toastr["info"]("Por favor ingresarlos manualmente.", "Los datos del visitante no se han encontrado")
                                vm.founded = false;
                                vm.showLock = false;
                            }
                            Modal.hideLoadingBar();
                        })

                    }, 10)

                }

                function findLicensePlate(id) {
                    for (var i = 0; i < $rootScope.visitorsList.length; i++) {
                        var itemVisitor = $rootScope.visitorsList[i];
                        if (itemVisitor.identificationnumber == id && itemVisitor.isinvited == 3) {
                            if (itemVisitor.licenseplate != undefined) {
                                vm.visitor_license_plate = itemVisitor.licenseplate;
                            }
                        }
                    }
                }

            }
            vm.changeDestino = function () {
                vm.house = {};
                setTimeout(function () {
                    $scope.$apply(function () {
                        $(".input-res1").removeClass("md-input-invalid")

                        $(".select-res").removeClass("ng-pristine ng-empty ng-invalid ng-invalid-required ng-touched")
                    })
                }, 10)

            }
            vm.unlocklock = function () {
                vm.found = false;
                vm.showLockCed = false;
            }

            function findHouse(id) {
                angular.forEach($rootScope.visitorsList, function (itemVisitor, index) {
                    if (itemVisitor.identificationnumber == id && itemVisitor.isinvited == 3) {

                        if (itemVisitor.licenseplate != undefined) {
                            vm.visitor_license_plate = itemVisitor.licenseplate;
                        }
                        setHouse(itemVisitor.houseId);
                    }
                });
            }

            function setHouse(house) {
                for (var i = 0; i < $rootScope.houses.length; i++) {
                    if ($rootScope.houses[i].id == house) {
                        vm.house = $rootScope.houses[i];
                    }
                }
            }

            vm.clearAll = function () {
                vm.clearInputs()
                setTimeout(function () {
                    $scope.$apply(function () {
                        $(".input-res").removeClass("md-input-invalid")
                        $(".select-res").removeClass("ng-pristine ng-empty ng-invalid ng-invalid-required ng-touched")
                    })
                }, 10)
            }


            vm.clearInputs = function () {
                vm.visitor_id_number = "";
                vm.visitor_name = ""
                vm.visitor_last_name = "";
                vm.visitor_second_last_name = "";
                vm.visitor_license_plate = "";
                vm.house = {};
                vm.showLockCed = false;
            }

            vm.insertVisitor = function () {
                if (vm.visitor_id_number.length < 9) {
                    toastr["error"]("El formato de la cédula no es correcto, debe de tener al menos 9 dígitos")
                } else {
                    Modal.confirmDialog("¿Está seguro que desea registrar la visita?", "", function () {
                        Modal.showLoadingBar();
                        var visitant = {
                            name: vm.visitor_name.toUpperCase(),
                            lastname: vm.visitor_last_name.toUpperCase(),
                            secondlastname: vm.visitor_second_last_name.toUpperCase(),
                            identificationnumber: vm.visitor_id_number.toUpperCase(),
                            licenseplate: vm.visitor_license_plate!==undefined?vm.visitor_license_plate.toUpperCase():undefined,
                            companyId: globalCompany.getId(),
                            isinvited: 3,
                            arrivaltime: moment(new Date()).format(),
                            houseId: vm.house.id
                        }
                        if (vm.house.id == undefined) {
                            visitant.responsableofficer = vm.house.housenumber;
                        }
                        Visitant.save(visitant, onSaveSuccess, onSaveError);
                    })
                }
            };

            function onSaveSuccess(result) {
                result.house = vm.house;
                console.log(result)
                $rootScope.visitorsList.push(result);
                toastr["success"]("Se registró la entrada del visitante correctamente.");
                vm.clearInputs();
                Modal.hideLoadingBar();
                setTimeout(function () {
                    $scope.$apply(function () {
                        $(".input-res").removeClass("md-input-invalid")
                        $(".select-res").removeClass("ng-pristine ng-empty ng-invalid ng-invalid-required ng-touched")
                    })
                }, 10)
            }

            function onSaveError() {
                toastr["error"]("Ocurrio un error registrando la entrada del visitante.");
                Modal.hideLoadingBar();
                setTimeout(function () {
                    $scope.$apply(function () {
                        $(".input-res").removeClass("md-input-invalid")
                        $(".select-res").removeClass("ng-pristine ng-empty ng-invalid ng-invalid-required ng-touched")
                    })
                }, 10)

            }
        }
    }
)();
