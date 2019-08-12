(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('MacroRegisterVisitorController', MacroRegisterVisitorController);

        MacroRegisterVisitorController.$inject = ['Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AlertService', 'companyUser', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal', 'MacroVisit'];

        function MacroRegisterVisitorController(Auth, $state, $scope, $rootScope, CommonMethods, AlertService, companyUser, PadronElectoral, Destinies, globalCompany, Modal, MacroVisit) {
            var vm = this;

            vm.showLock = true;
            vm.visitorType = 1;
            CommonMethods.validateLetters();
            CommonMethods.validateNumbers();
            CommonMethods.validateSpecialCharacters();
            CommonMethods.validateSpecialCharactersAndVocals();
            vm.showLockCed = false;
            $rootScope.mainTitle = "Registrar visitante";

            Destinies.query(function (destinies) {
                vm.destinies = destinies;
            });

            vm.getVisitor = function () {
                vm.houseSelected = undefined;
                vm.destiny=undefined;
                vm.visitorType = 1;
                if (vm.visitor_id_number !== undefined && vm.visitor_id_number.length >= 9) {
                    Modal.showLoadingBar();
                    if ($rootScope.online) {
                        PadronElectoral.find(vm.visitor_id_number, personFindedPadron, personNotFinded)
                    } else {
                        personNotFinded();
                    }
                }

                function personFindedPadron(person) {
                    MacroVisit.getByMacroIdAndIdentification({
                        macroId: globalCompany.getId(),
                        identification: vm.visitor_id_number
                    }, function (visitor) {
                        setFormPadron(person);
                        setDestiny(visitor);
                    }, function () {
                        setFormPadron(person);
                    });
                }

                function personNotFinded() {
                    MacroVisit.getByMacroIdAndIdentification({
                        macroId: globalCompany.getId(),
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
                if (vm.visitor_license_plate && vm.visitor_id_number == undefined) {
                    Modal.showLoadingBar();
                }
                MacroVisit.getByMacroIdAndPlate({
                    macroId: globalCompany.getId(),
                    plate: vm.visitor_license_plate
                }, function (visitor) {
                    setFormDB(visitor);
                    setDestiny(visitor);
                    Modal.hideLoadingBar();
                }, function () {
                    nothingFound();
                });

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

            function nothingFound(){
                vm.encontrado = 0;
                setTimeout(function () {
                    $scope.$apply(function () {
                        if (vm.encontrado > 0) {
                            vm.consultingPadron = false;
                            vm.founded = true;
                            vm.showLock = true;
                        } else {
                            if ($rootScope.online) {
                                toastr["info"]("Por favor ingresarlos manualmente.", "Los datos del visitante no se han encontrado")
                            } else {
                                toastr["info"]("Por favor ingresarlos los datos del visitante manualmente.", "No hay conexión a internet")
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
                        vm.visitor_last_name = visitor.lastname;
                        vm.visitor_second_last_name = visitor.secondlastname;
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
                        vm.condominiumSelected = findCompany(visitor.companyId);
                        setTimeout(function () {
                            $scope.$apply(function () {
                                if (visitor.houseId !== null) {
                                    vm.houseSelected = visitor.houseId;
                                } else {
                                    vm.visitorType = 2;
                                    vm.destiny = visitor.destiny
                                }
                            })
                        }, 100)
                    })
                }, 10)
            }

            function findCompany(companyId) {
                for (var i = 0; i < $rootScope.macroCondominium.companies.length; i++) {
                    if ($rootScope.macroCondominium.companies[i].id == companyId) {
                        return $rootScope.macroCondominium.companies[i];
                    }
                }
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
                vm.showLockCed = false;
                $rootScope.id_number = undefined;
                $rootScope.id_vehicule = undefined;
            }

            vm.insertVisitor = function () {
                if (vm.visitor_id_number.length < 9) {
                    toastr["error"]("El formato de la cédula no es correcto, debe de tener al menos 9 dígitos")
                } else {
                    Modal.confirmDialog("¿Está seguro que desea registrar la visita?", "", function () {
                        Modal.showLoadingBar();
                        var visitor = {
                            name: vm.visitor_name.toUpperCase(),
                            lastname: vm.visitor_last_name.toUpperCase(),
                            secondlastname: vm.visitor_second_last_name.toUpperCase(),
                            identificationnumber: vm.visitor_id_number.toUpperCase(),
                            licenseplate: vm.visitor_license_plate !== undefined ? vm.visitor_license_plate.toUpperCase() : undefined,
                            macroCondominiumId: globalCompany.getId(),
                            companyId: vm.condominiumSelected.id,
                            status: 1,
                            destiny: vm.destiny,
                            arrivaltime: moment(new Date()).format(),
                            houseId: vm.houseSelected
                        }
                        MacroVisit.save(visitor, onSaveSuccess, onSaveError);
                    })
                }
            };

            function onSaveSuccess(result) {
                toastr["success"]("Se registró la entrada del visitante correctamente.");
                vm.clearInputs();
                Modal.hideLoadingBar();
                vm.houseSelected = undefined;
                vm.destiny=undefined;
                vm.visitorType = 1;
                setTimeout(function () {
                    $scope.$apply(function () {
                        $(".input-res").removeClass("md-input-invalid")
                        $(".select-res").removeClass("ng-pristine ng-empty ng-invalid ng-invalid-required ng-touched")
                    })
                }, 10)
            }

            function onSaveError() {
                toastr["info"]("Se registrará la visita una vez la conexión haya vuelto.", "No hay conexión a internet");
                Modal.hideLoadingBar();
                vm.clearInputs();
                vm.houseSelected = undefined;
                vm.destiny=undefined;
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
