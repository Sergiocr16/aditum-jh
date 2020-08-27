(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('RegisterVisitorController', RegisterVisitorController);

        RegisterVisitorController.$inject = ['Resident', 'VisitantInvitation', 'Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AlertService', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal', 'Visitant'];

        function RegisterVisitorController(Resident, VisitantInvitation, Auth, $state, $scope, $rootScope, CommonMethods, AlertService, PadronElectoral, Destinies, globalCompany, Modal, Visitant) {
            var vm = this;
            vm.showLock = true;
            vm.visitorType = 1;
            CommonMethods.validateLetters();
            CommonMethods.validateNumbers();
            CommonMethods.validateSpecialCharacters();
            CommonMethods.validateSpecialCharactersAndVocals();
            vm.showLockCed = false;
            $rootScope.mainTitle = "Registrar visitante";
            vm.foundVisitantInvited = false;
            vm.loadedResidentsInfo = false;

            vm.save = save;
            Modal.enteringForm(save);
            $scope.$on("$destroy", function () {
                Modal.leavingForm();
            });
            vm.clearSearchTerm = function () {
                vm.searchTerm = '';
            };
            vm.searchTerm;
            vm.typingSearchTerm = function (ev) {
                ev.stopPropagation();
            }
            vm.getVisitor = function () {
                vm.houseSelected = undefined;
                vm.destiny = undefined;
                vm.visitorType = 1;
                if (vm.visitor_id_number !== undefined && vm.visitor_id_number.length >= 8) {
                    Modal.showLoadingBar();
                    if ($rootScope.online) {
                        VisitantInvitation.getActiveInvitedByCompanyFilter({
                            page: vm.page,
                            size: 1,
                            sort: sortResidents(),
                            companyId: globalCompany.getId(),
                            name: vm.visitor_id_number,
                            houseId: "empty",
                            owner: "empty",
                            enabled: 1,
                        }, function (data) {
                            var invited = data[0];
                            if (invited != undefined) {
                                if (invited.id != null) {
                                    vm.foundVisitantInvited = true;
                                    setFormDB(invited);
                                    setDestiny(invited);
                                    vm.loadResidentInfo();
                                    Modal.hideLoadingBar();
                                } else {
                                    vm.foundVisitantInvited = false;
                                    PadronElectoral.find(vm.visitor_id_number, personFindedPadron, personNotFinded)
                                }
                            } else {
                                vm.foundVisitantInvited = false;
                                PadronElectoral.find(vm.visitor_id_number, personFindedPadron, personNotFinded)
                            }
                        }, function () {
                        });
                    } else {
                        personNotFinded();
                    }
                }

                function sortResidents() {
                    var result = [];
                    if (vm.predicate !== 'name') {
                        result.push('name,asc');
                    }
                    return result;
                }

                function personFindedPadron(person) {
                    Visitant.getByCompanyIdAndIdentification({
                        companyId: globalCompany.getId(),
                        identification: vm.visitor_id_number
                    }, function (visitor) {
                        setFormPadron(person);
                        setDestiny(visitor);
                    }, function () {
                        setFormPadron(person);
                    });
                }

                function personNotFinded() {
                    Visitant.getByCompanyIdAndIdentification({
                        companyId: globalCompany.getId(),
                        identification: vm.visitor_id_number
                    }, function (visitor) {
                        setFormDB(visitor);
                        setDestiny(visitor);
                    }, function () {
                        nothingFound();
                    });
                }
            };
            vm.getVisitorByPlate = function () {
                if (vm.visitor_id_number === undefined || vm.visitor_id_number === "") {
                    if (vm.visitor_license_plate) {
                        Modal.showLoadingBar();
                        Visitant.getByCompanyIdAndPlate({
                            companyId: globalCompany.getId(),
                            plate: vm.visitor_license_plate
                        }, function (visitor) {
                            setFormDB(visitor);
                            setDestiny(visitor);
                            Modal.hideLoadingBar();
                        }, function () {
                            // nothingFound();
                            vm.encontrado = 0;
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    if (vm.encontrado > 0) {
                                        vm.consultingPadron = false;
                                        vm.founded = true;
                                        vm.showLock = true;
                                    } else {
                                        vm.founded = false;
                                        vm.showLock = false;
                                    }
                                })
                            }, 10)
                            Modal.hideLoadingBar();
                        });
                    }
                }
            };

            function setFormPadron(person) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.showLock = true;
                        vm.visitor_name = person.nombre.split(",")[0] + "";
                        vm.visitor_last_name = person.nombre.split(",")[1] + "";
                        vm.visitor_second_last_name = person.nombre.split(",")[2] + "";
                        vm.consultingPadron = false;
                        vm.showLockCed = true;
                        vm.founded = true;
                        Modal.hideLoadingBar();
                    })
                }, 10)
            }

            function nothingFound() {
                vm.encontrado = 0;
                setTimeout(function () {
                    $scope.$apply(function () {
                        if (vm.encontrado > 0) {
                            vm.consultingPadron = false;
                            vm.founded = true;
                            vm.showLock = true;
                        } else {
                            if ($rootScope.online) {
                                Modal.toastGiant("Los datos del visitante no se han encontrado")
                            } else {
                                Modal.toastGiant("No hay conexión a internet,ingresa los datos del visitante manualmente")
                            }
                            vm.founded = false;
                            vm.showLock = false;
                        }
                    })
                }, 10)
                Modal.hideLoadingBar();
            }

            function setFormDB(visitor) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.showLock = true;
                        vm.visitor_name = visitor.name;
                        vm.proveedor = visitor.proveedor;
                        vm.visitor_last_name = visitor.lastname;
                        vm.visitor_second_last_name = visitor.secondlastname;
                        vm.visitor_id_number = visitor.identificationnumber;
                        vm.consultingPadron = false;
                        vm.showLockCed = true;
                        vm.founded = true;
                        Modal.hideLoadingBar();
                    })
                }, 10)
            }


            function setDestiny(visitor) {
                if (visitor.licenseplate != null) {
                    if (vm.visitor_license_plate === undefined || vm.visitor_license_plate === "") {
                        vm.visitor_license_plate = visitor.licenseplate;
                    }
                }
                setTimeout(function () {
                    $scope.$apply(function () {
                        setTimeout(function () {
                            $scope.$apply(function () {
                                if (visitor.houseId !== null) {
                                    vm.houseSelected = [visitor.houseId];
                                } else {
                                    vm.visitorType = 2;
                                    vm.destiny = visitor.responsableofficer
                                }
                                vm.loadResidentInfo();
                            })
                        }, 100)
                    })
                }, 10)
            }

            vm.visitorProveedor = function (visitor) {
                if (visitor == null || visitor == undefined || visitor == "") {
                    return false;
                }
                return true;
            }
            vm.changeDestino = function () {
                vm.house = {};
                vm.houseSelected = undefined;
                vm.destiny = undefined;
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
                vm.houseSelected = [];
                vm.loadedResidentsInfo = false;
                vm.residentsInfo = [];
                vm.observation = null;
                vm.proveedor = null;
                vm.foundVisitantInvited = false;
                vm.showLockCed = false;
                $rootScope.id_number = undefined;
                $rootScope.id_vehicule = undefined;
            }
            vm.loadResidentInfo = function () {
                if (vm.houseSelected != undefined) {
                    vm.loadResidents(vm.houseSelected[vm.houseSelected.length - 1])
                }
            }

            vm.loadResidents = function (houseId) {
                for (var i = 0; i < $rootScope.houses.length; i++) {
                    if ($rootScope.houses[i].id == houseId) {
                        var house = $rootScope.houses[i];
                        vm.houseInfo = house.housenumber;
                        vm.phoneFijo = house.extension != null ? house.extension : "No definido";
                    }
                }
                vm.loadingResident = true;
                Resident.getResidents({
                    page: 0,
                    size: 100,
                    companyId: globalCompany.getId(),
                    name: " ",
                    houseId: houseId,
                    owner: "empty",
                    enabled: 1,
                }, function (data) {
                    vm.residentsInfo = [];
                    vm.loadedResidentsInfo = true;
                    for (var i = 0; i < data.length; i++) {
                        var resident = data[i];
                        if (resident.type == 1) {
                            resident.type = "Propietario residente";
                        } else if (resident.type == 2) {
                            resident.type = "Propietario arrendador";
                        } else if (resident.type == 3) {
                            resident.type = "Residente";
                        } else if (resident.type == 4) {
                            resident.type = "Inquilino";
                        }
                        if (resident.phonenumber == "" || resident.phonenumber == null) {
                            resident.phonenumber = "No registrado";
                        }
                        vm.residentsInfo.push(resident)
                    }

                }, function () {
                    Modal.toast("Error obteniendo los residentes")
                });
            }


            function save() {
                var valid = false;
                if (vm.visitor_id_number.length < 8) {
                    Modal.toastGiant("El formato de la cédula no es correcto, debe de tener al menos 8 dígitos")
                } else {
                    if (vm.visitorType == 1) {
                        if (vm.houseSelected === undefined) {
                            Modal.toastGiant("Debe seleccionar la filial que visita")
                        } else {
                            valid = true;
                        }
                    } else {
                        if (vm.destiny === undefined) {
                            Modal.toastGiant("Debe seleccionar el destino que visita")
                        } else {
                            valid = true;
                        }
                    }
                    if (valid) {
                        Modal.confirmDialog("¿Está seguro que desea registrar la visita?", "", function () {
                            Modal.showLoadingBar();
                            if (vm.visitorType == 2) {
                                var visitor = {
                                    name: vm.visitor_name.toUpperCase(),
                                    lastname: vm.visitor_last_name.toUpperCase(),
                                    secondlastname: vm.visitor_second_last_name !== undefined ? vm.visitor_second_last_name.toUpperCase() : undefined,
                                    identificationnumber: vm.visitor_id_number.toUpperCase(),
                                    licenseplate: vm.visitor_license_plate !== undefined ? vm.visitor_license_plate.toUpperCase() : undefined,
                                    companyId: globalCompany.getId(),
                                    isinvited: 4,
                                    responsableofficer: vm.destiny,
                                    arrivaltime: moment(new Date()).format(),
                                    houseId: undefined,
                                    observation: vm.observation,
                                    proveedor: vm.proveedor,
                                }
                                visitor.responsableofficer = vm.destiny;
                                visitor.houseId = undefined;
                                Visitant.save(visitor, onSaveSuccess, onSaveError);
                            } else {
                                for (var i = 0; i < vm.houseSelected.length; i++) {
                                    var visitor = {
                                        name: vm.visitor_name.toUpperCase(),
                                        lastname: vm.visitor_last_name.toUpperCase(),
                                        secondlastname: vm.visitor_second_last_name !== undefined && vm.visitor_second_last_name !== null ? vm.visitor_second_last_name.toUpperCase() : undefined,
                                        identificationnumber: vm.visitor_id_number.toUpperCase(),
                                        licenseplate: vm.visitor_license_plate !== undefined ? vm.visitor_license_plate.toUpperCase() : undefined,
                                        companyId: globalCompany.getId(),
                                        isinvited: 4,
                                        responsableofficer: vm.destiny,
                                        arrivaltime: moment(new Date()).format(),
                                        houseId: vm.houseSelected[i],
                                        observation: vm.observation,
                                        proveedor: vm.proveedor,
                                    }
                                    Visitant.save(visitor, onSaveSuccess, onSaveError);
                                }
                            }
                        })
                    }
                }
            };

            function onSaveSuccess(result) {
                Modal.toastGiant("Se registró la entrada del visitante correctamente.");
                vm.clearInputs();
                Modal.hideLoadingBar();
                vm.houseSelected = [-1];
                vm.destiny = undefined;
                vm.visitorType = 1;
                setTimeout(function () {
                    $scope.$apply(function () {
                        $(".input-res").removeClass("md-input-invalid")
                        $(".select-res").removeClass("ng-pristine ng-empty ng-invalid ng-invalid-required ng-touched")
                    })
                }, 10)
            }

            function onSaveError() {
                Modal.toastGiant("No hay conexión a internet, Se registrará la visita una vez la conexión haya vuelto");
                Modal.hideLoadingBar();
                vm.clearInputs();
                vm.houseSelected = -1;
                vm.destiny = undefined;
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
