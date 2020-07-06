(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('LoadAutomaticVisitorController', LoadAutomaticVisitorController);

        LoadAutomaticVisitorController.$inject = ['$mdDialog', '$scope', '$state', 'House', 'globalCompany', '$rootScope', 'DataUtils', 'Modal', '$timeout', 'VisitantInvitation', 'InvitationSchedule'];

        function LoadAutomaticVisitorController($mdDialog, $scope, $state, House, globalCompany, $rootScope, DataUtils, Modal, $timeout, VisitantInvitation, InvitationSchedule) {
            $rootScope.active = "load-automatic-visitor";
            var vm = this;
            var file;
            vm.fileName;
            vm.chargesList = [];
            vm.isReady = 0;
            House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
            }

            vm.clearSearchTerm = function () {
                vm.searchTerm = '';
            };
            vm.searchTerm;
            vm.typingSearchTerm = function (ev) {
                ev.stopPropagation();
            }

            vm.uploadFile = function ($file) {

                if ($file && $file.$error === 'pattern') {
                    return;
                }
                if ($file) {
                    DataUtils.toBase64($file, function (base64Data) {
                        $scope.$apply(function () {
                            vm.displayImage = base64Data;
                            vm.displayImageType = $file.type;
                        });
                    });
                    file = $file;
                    vm.fileName = file.name;
                    vm.parseExcel();
                }
            };

            vm.parseExcel = function () {
                var reader = new FileReader();
                reader.onload = function (e) {
                    var data = e.target.result;
                    var workbook = XLSX.read(data, {
                        type: 'binary'
                    });

                    /* DO SOMETHING WITH workbook HERE */
                    var first_sheet_name = workbook.SheetNames[0];
                    /* Get worksheet */
                    var worksheet = workbook.Sheets[first_sheet_name];
                    vm.visitors = XLSX.utils.sheet_to_json(worksheet, {
                        raw: false
                    });
                    console.log(vm.visitors)
                    formatVisitants(vm.visitors)
                };
                reader.onerror = function (ex) {
                    console.log(ex);
                };
                reader.readAsBinaryString(file);
                vm.isReady = 1;
            };


            vm.saveVisitors = function () {
                Modal.confirmDialog("¿Está seguro que desea registrar los visitantes?", "", function () {
                    vm.error = false;
                    Modal.showLoadingBar();
                    Modal.toast("Se están registrando los visitantes, por favor espere y no cierre la ventana.")
                    createVisitor(vm.visitorsFormatted[0], 0, vm.visitorsFormatted.length);
                    if (vm.error) {
                        Modal.toast("Se han presentado un error.")
                    }
                })
            };

            function createVisitor(visitor, count, length) {
                if (count < length) {
                    VisitantInvitation.save(visitor, function (result) {
                        if (result.hasschedule == 1) {
                            InvitationSchedule.save(defineInvitationSchedule(vm.visitorsFormatted[count], result.id), function () {
                                count++;
                                if (count == length) {
                                    Modal.hideLoadingBar();
                                    vm.isReady = 0;
                                    Modal.toast("Se han ingresado los visitantes correctamente.")
                                } else {
                                    createVisitor(vm.visitorsFormatted[count], count, vm.visitorsFormatted.length)
                                }
                            }, function () {
                                Modal.hideLoadingBar();
                                vm.error = true;
                            });
                        } else {
                            count++;
                            if (count == length) {
                                Modal.hideLoadingBar();
                                vm.isReady = 0;
                                Modal.toast("Se han ingresado los visitantes correctamente.")
                            } else {
                                createVisitor(vm.visitorsFormatted[count], count, vm.visitorsFormatted.length)
                            }
                        }
                    }, function () {
                        Modal.hideLoadingBar();
                        vm.error = true;
                    })
                }
            }

            function formatVisitants(visitors) {
                vm.visitorsFormatted = [];
                for (var i = 0; i < visitors.length; i++) {
                    var visitor = visitors[i];
                    if (visitor.Invitado != undefined) {
                        if (visitor.Invitado.toUpperCase().trim() != "N/A") {
                            var visitorFormatted = {
                                companyId: "1",
                                found: 1,
                                hasschedule: 0,
                                houseId: null,
                                identificationnumber: "",
                                invitationlimittime: null,
                                invitationstartingtime: null,
                                lastname: "",
                                licenseplate: "",
                                name: "",
                                secondlastname: "",
                                status: 1,
                                type: "9",
                                validIdentification: 1,
                                validPlateNumber: 1,
                            };
                            var fullName = defineName(visitor, visitorFormatted);
                            visitorFormatted.name = fullName.name;
                            visitorFormatted.lastname = fullName.lastname;
                            visitorFormatted.secondlastname = fullName.secondlastname;
                            var invitationSchedule = null;
                            var notUnique = false;
                            if (visitor.Tipo.toUpperCase().trim() == "PERMANENTE") {
                                visitorFormatted.invitationstartingtime = moment(new Date()).format();
                                var d = new Date();
                                var year = d.getFullYear();
                                var month = d.getMonth();
                                var day = d.getDate();
                                var dPlus2years = new Date(year + 2, month, day);
                                visitorFormatted.invitationlimittime = moment(dPlus2years).format();
                                visitorFormatted.hasschedule = 0;
                            } else if (visitor.Tipo.toUpperCase().trim() == "RECURRENTE") {
                                visitorFormatted.hasschedule = 1;
                            } else {
                                visitorFormatted.notUnique = true;
                            }
                            angular.forEach(vm.houses, function (house, key) {
                                if (house.housenumber == visitor.Filial) {
                                    visitorFormatted.houseId = house.id;
                                }
                            });
                            visitorFormatted.visitor = visitor;
                            vm.visitorsFormatted.push(visitorFormatted)
                        }
                    }
                }
                $timeout(function () {
                    vm.isReady = 2;
                }, 10)
            }

            function defineInvitationSchedule(visitorFormatted, id) {
                var daysA = visitorFormatted.visitor.Dias.split(",");
                var invistationSchedule = {
                    domingo: null,
                    jueves: null,
                    lunes: null,
                    martes: null,
                    miercoles: null,
                    sabado: null,
                    viernes: null,
                    visitantInvitationId: id
                }
                for (var i = 0; i < daysA.length; i++) {
                    findDay(daysA[i], invistationSchedule)
                }
                return invistationSchedule;
            }

            function findDay(day, invitationSchedule) {
                var day = day.toUpperCase().trim();
                switch (day) {
                    case "LUNES":
                        invitationSchedule.lunes = "00:01-23:59";
                        break;
                    case "MARTES":
                        invitationSchedule.martes = "00:01-23:59";
                        break;
                    case "MIERCOLES":
                        invitationSchedule.miercoles = "00:01-23:59";
                        break;
                    case "JUEVES":
                        invitationSchedule.jueves = "00:01-23:59";
                        break;
                    case "VIERNES":
                        invitationSchedule.viernes = "00:01-23:59";
                        break;
                    case "SABADO":
                        invitationSchedule.sabado = "00:01-23:59";
                        break;
                    case "DOMINGO":
                        invitationSchedule.domingo = "00:01-23:59";
                        break;
                }
            }

            function defineName(visitor) {
                var nameA = visitor.Invitado.split(" ");
                var visitorName = {name: "", lastname: "", secondlastname: ""}
                for (var i = 0; i < nameA.length; i++) {
                    nameA[i] = nameA[i].toUpperCase();
                }
                if (nameA.length == 1) {
                    visitorName.name = nameA[0];
                    return visitorName;
                }
                if (nameA.length == 2) {
                    visitorName.name = nameA[0];
                    visitorName.lastname = nameA[1];
                    return visitorName;
                }
                if (nameA.length == 3) {
                    visitorName.name = nameA[0];
                    visitorName.lastname = nameA[1];
                    visitorName.secondlastname = nameA[2];
                    return visitorName;
                }
                if (nameA.length > 3) {
                    visitorName.name = nameA[0] + " " + nameA[1];
                    visitorName.lastname = nameA[2];
                    visitorName.secondlastname = nameA[3];
                    return visitorName;
                }
            }
        }
    }

)();
