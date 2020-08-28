(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantRenewWithScheduleController', VisitantRenewWithScheduleController);

    VisitantRenewWithScheduleController.$inject = ['$localStorage','Destinies', '$uibModalInstance', 'entity', 'InvitationSchedule', 'VisitantInvitation', '$state', '$timeout', '$interval', '$scope', '$stateParams', 'Visitant', 'House', 'Company', 'Principal', '$rootScope', 'CommonMethods', 'WSVisitorInvitation', 'WSDeleteEntity', 'PadronElectoral', 'globalCompany', 'Modal'];

    function VisitantRenewWithScheduleController($localStorage,Destinies, $uibModalInstance, entity, InvitationSchedule, VisitantInvitation, $state, $timeout, $interval, $scope, $stateParams, Visitant, House, Company, Principal, $rootScope, CommonMethods, WSVisitorInvitation, WSDeleteEntity, PadronElectoral, globalCompany, Modal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.visitor = {};
        vm.clear = clear;
        $rootScope.active = "reportInvitation";
        $rootScope.mainTitle = "Renovar invitación"
        House.getAllHousesClean({companyId: globalCompany.getId()}, function (data) {
            vm.houses = data;
        });
        vm.clearSearchTerm = function () {
            vm.searchTerm = '';
        };
        vm.searchTerm;
        vm.typingSearchTerm = function (ev) {
            ev.stopPropagation();
        }
        Destinies.query(function (destinies) {
            vm.destinies = destinies;
        });
        vm.save = save;
        vm.timeFormat = 2;
        vm.visitor = entity;
        vm.visitor.type = "9";
        vm.visitorType = 1
        vm.houseSelected = vm.visitor.houseId;
        vm.visitor.validIdentification = 1;
        vm.visitor.validPlateNumber = 1;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.houses = House.query();
        vm.companies = Company.query();

        InvitationSchedule.findSchedulesByInvitation({
            invitationId: vm.visitor.id
        }, onSuccessSchedule);
        vm.validPlateArray = function (plate) {
            var plateN = plate.licenseplate
            if (plateN == undefined) {
                plate.valid = true;
            } else {
                if (hasCaracterEspecial(plateN) || hasWhiteSpace(plateN)) {
                    plate.valid = false;
                } else {
                    plate.valid = true;
                }
            }
        }

        vm.validPlateAllArray = function () {
            var valid = 0;
            for (var i = 0; i < vm.plates.length; i++) {
                var plate = vm.plates[i];
                if (plate.valid) {
                    valid++;
                }
            }
            return valid == vm.plates.length;
        }

        function loadPlates() {
            if (vm.visitor.licenseplate != null) {
                var lc = vm.visitor.licenseplate.split("/");
                for (var i = 0; i < lc.length; i++) {
                    vm.plates.push({plate: undefined, licenseplate: lc[i].trim(), valid: true})
                }
            } else {
                vm.plates.push({plate: undefined, licenseplate: undefined, valid: true})
            }
        }

        vm.plates = [];
        loadPlates();
        vm.addPlate = function () {
            vm.plates.push({plate: undefined, valid: true});
        }
        vm.deletePlate = function (plate) {
            CommonMethods.deleteFromArray(plate, vm.plates)
        }
        vm.initialDate = new Date(1970, 0, 1, 16, 0, 0)
        vm.finalDate = new Date(1970, 0, 1, 16, 30, 0)
        vm.daysOfWeek = [{day: 'Lunes', selected: false, initialTime: vm.initialDate, finalTime: vm.finalDate}, {
            day: 'Martes',
            selected: false,
            initialTime: vm.initialDate, finalTime: vm.finalDate
        }, {day: 'Miercoles', selected: false, initialTime: vm.initialDate, finalTime: vm.finalDate}, {
            day: 'Jueves',
            selected: false,
            initialTime: vm.initialDate, finalTime: vm.finalDate
        }, {day: 'Viernes', selected: false, initialTime: vm.initialDate, finalTime: vm.finalDate}, {
            day: 'Sábado',
            selected: false,
            initialTime: vm.initialDate, finalTime: vm.finalDate
        }, {day: 'Domingo', selected: false, initialTime: vm.initialDate, finalTime: vm.finalDate}];

        vm.selectDay = function (index) {
            vm.spaceInvalid3 = false;
            vm.daysOfWeek[index].selected = !vm.daysOfWeek[index].selected;

        };

        vm.validateHoursPerDay = function (item) {
            if (item.initialTime !== undefined && item.finalTime !== undefined) {
                if (item.initialTime >= item.finalTime) {
                    item.isValid = false;
                    Modal.toast("Debe seleccionar una hora final posterior a la hora anterior");
                } else {
                    item.isValid = true;

                }
            }
        };

        function formatDatesOfSchedule(date) {
            var times = date.split("-");
            var hours = times[0].split(":");
            hours.push(times[1].split(":")[0])
            hours.push(times[1].split(":")[1])
            return hours;
        }

        function onSuccessSchedule(data) {
            vm.scheduleId = data[0].id;
            if (data[0].lunes !== null) {
                vm.daysOfWeek[0].selected = true;
                vm.lunesSelected = true;
                var hours = formatDatesOfSchedule(data[0].lunes);
                vm.daysOfWeek[0].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[0].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);

            }
            if (data[0].martes !== null) {
                vm.daysOfWeek[1].selected = true;
                vm.martesSelected = true;
                var hours = formatDatesOfSchedule(data[0].martes);
                vm.daysOfWeek[1].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[1].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);
            }
            if (data[0].miercoles !== null) {
                vm.daysOfWeek[2].selected = true;
                vm.miercolesSelected = true;
                var hours = formatDatesOfSchedule(data[0].miercoles);
                vm.daysOfWeek[2].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[2].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);
            }
            if (data[0].jueves !== null) {
                vm.daysOfWeek[3].selected = true;
                vm.juevesSelected = true;
                var hours = formatDatesOfSchedule(data[0].jueves);
                vm.daysOfWeek[3].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[3].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);

            }
            if (data[0].viernes !== null) {
                vm.daysOfWeek[4].selected = true;
                vm.viernesSelected = true;
                var hours = formatDatesOfSchedule(data[0].viernes);
                vm.daysOfWeek[4].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[4].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);
            }
            if (data[0].sabado !== null) {
                vm.daysOfWeek[5].selected = true;
                vm.sabadoSelected = true;
                var hours = formatDatesOfSchedule(data[0].sabado);
                vm.daysOfWeek[5].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[5].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);
            }
            if (data[0].domingo !== null) {
                vm.daysOfWeek[6].selected = true;
                vm.domingoSelected = true;
                var hours = formatDatesOfSchedule(data[0].domingo);
                vm.daysOfWeek[6].initialTime = new Date(1970, 0, 1, parseInt(hours[0]), parseInt(hours[1]), 0);
                vm.daysOfWeek[6].finalTime = new Date(1970, 0, 1, parseInt(hours[2]), parseInt(hours[3]), 0);
            }
            vm.isReady = true;
        }

        vm.validPlate = function (visitor) {
            if (hasCaracterEspecial(visitor.licenseplate) || hasWhiteSpace(visitor.licenseplate)) {
                visitor.validPlateNumber = 0;
            } else {
                visitor.validPlateNumber = 1;
            }
        }
        vm.hasNumbersOrSpecial = function (s) {
            var caracteres = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^", "!"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase()) {
                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                return false;
            } else {
                return true;
            }
        }

        vm.findInPadron = function (visitor) {

            if (visitor.identificationnumber != undefined || visitor.identificationnumber != "") {
                if (hasCaracterEspecial(visitor.identificationnumber) || haswhiteCedula(visitor.identificationnumber) || visitor.type == "9" && hasLetter(visitor.identificationnumber)) {
                    visitor.validIdentification = 0;
                } else {
                    visitor.validIdentification = 1;
                }

                if (visitor.type == "9" && visitor.identificationnumber != undefined) {
                    if (visitor.identificationnumber.trim().length == 9) {
                        PadronElectoral.find(visitor.identificationnumber, function (person) {
                            setTimeout(function () {
                                $scope.$apply(function () {
                                    var nombre = person.nombre.split(",");
                                    visitor.name = nombre[0];
                                    visitor.lastname = nombre[1];
                                    visitor.secondlastname = nombre[2];
                                    visitor.found = 1;
                                })
                            }, 100)
                        }, function () {

                        })


                    } else {
                        setTimeout(function () {
                            $scope.$apply(function () {
                                visitor.found = 0;
                            })
                        }, 100)
                    }
                } else {
                    visitor.found = 0;
                }
            }
        }


        function hasWhiteSpace(s) {
            function tiene(s) {
                return /\s/g.test(s);
            }

            if (tiene(s) || s == undefined) {
                return true
            }
            return false;
        }

        function hasLetter(s) {
            var caracteres = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i).toUpperCase() == val.toUpperCase()) {

                            invalido++;
                        }
                    }
                }
            })
            if (invalido == 0) {
                return false;
            } else {
                return true;
            }
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
        function formatPlate() {
            vm.visitor.licenseplate = "";
            for (var i = 0; i < vm.plates.length; i++) {
                var plate = vm.plates[i];
                if (plate.licenseplate != undefined) {
                    if (plate.valid) {
                        vm.visitor.licenseplate = vm.visitor.licenseplate + plate.licenseplate.toUpperCase();
                        if (i + 1 < vm.plates.length) {
                            vm.visitor.licenseplate = vm.visitor.licenseplate + " / ";
                        }
                    }
                }
            }
        }
        function formatVisitor(visitor) {
            if (vm.timeFormat == 1) {
                visitor.invitationstartingtime = vm.formatDate(vm.dates.initial_date, vm.dates.initial_time);
                visitor.invitationlimittime = vm.formatDate(vm.dates.final_date, vm.dates.final_time);
                visitor.hasschedule = 0;
            } else {
                visitor.hasschedule = 1;
            }
            formatPlate();
            visitor.status = 1;
            vm.visitor.houseId = vm.houseSelected;
            visitor.companyId = globalCompany.getId();
            if (visitor.licenseplate != undefined) {
                visitor.licenseplate = visitor.licenseplate.toUpperCase();
            }
            if (visitor.identificationnumber != undefined) {
                visitor.identificationnumber = visitor.identificationnumber.toUpperCase();
            }
            if (visitor.licenseplate == "") {
                visitor.licenseplate = undefined;
            }
            visitor.name = visitor.name.toUpperCase();
            visitor.lastname = visitor.lastname.toUpperCase();
            visitor.secondlastname = visitor.secondlastname!=null?visitor.secondlastname.toUpperCase():"";
            return visitor;
        }

        vm.isAnyDaySelected = function () {

            var selectedDays = 0;
            angular.forEach(vm.daysOfWeek, function (item, key) {
                if (item.selected) {
                    selectedDays++;
                }

            })
            if (selectedDays > 0) {

                return true;
            } else {

                return false;
            }
        }

        vm.validateForm = function () {
            if (vm.timeFormat == 1) {
                var arrayVisitor = [];
                arrayVisitor.push(vm.visitor);
                if (vm.validArray(arrayVisitor)) {
                    save();
                }
            } else if (vm.timeFormat == 2) {

                if (!vm.isAnyDaySelected()) {
                    setTimeout(function () {
                        $scope.$apply(function () {
                            vm.spaceInvalid3 = true;
                        });
                    }, 200);
                    Modal.toast("Debe seleccionar al menos un día permitido para el ingreso del visitante");
                } else {
                    if (vm.isAllHoursValid() == false) {
                        Modal.toast("Debe corregir las horas permitidas para el ingreso del visitante");
                    } else {

                        save();
                    }
                }

            }
        };

        vm.isAllHoursValid = function () {

            var invalid = 0;
            angular.forEach(vm.daysOfWeek, function (item, key) {
                if (item.isValid == false) {
                    invalid++;
                }
            });
            if (invalid > 0) {

                return false;
            } else {

                return true;
            }
        };

        function save() {
            Modal.confirmDialog("¿Está seguro que desea reportar este visitante?", "", function () {
                Modal.showLoadingBar();
                formatVisitor(vm.visitor);
                vm.isSaving = true;
                VisitantInvitation.update(vm.visitor, onSaveSuccess, onSaveError);

            })
        }


        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:visitorUpdate', result);
            $localStorage.infoHouseNumber.id = result.houseId;
            VisitantInvitation.update(vm.visitor, onSuccess, onSaveError);
            function onSuccess(data) {
                WSVisitorInvitation.sendActivity(data);
                var invitationSchedule = formateTimesSchedule(data);
                InvitationSchedule.findSchedulesByInvitation({
                    invitationId: data.id
                }, function (schedule) {
                    console.log(schedule);
                    invitationSchedule.id = schedule[0].id;
                    InvitationSchedule.update(invitationSchedule, function () {
                        Modal.hideLoadingBar();
                        Modal.toast("Se ha renovado la invitación de " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
                        $scope.$emit('aditumApp:visitantUpdate', result);
                        $state.reload();
                        $uibModalInstance.close(result);
                    }, onSaveError);
                });


            }

        }

        function formateTimesSchedule(visitant) {
            var invitationSchedule = {};
            if (vm.daysOfWeek[0].selected) {

                var lunesInitial = vm.daysOfWeek[0].initialTime + "";
                var lunesFinal = vm.daysOfWeek[0].finalTime + "";
                invitationSchedule.lunes = lunesInitial.split(" ")[4].split(":")[0] + ":" + lunesInitial.split(" ")[4].split(":")[1] + "-" + lunesFinal.split(" ")[4].split(":")[0] + ":" + lunesFinal.split(" ")[4].split(":")[1];

            } else {
                invitationSchedule.lunes = null;
            }

            if (vm.daysOfWeek[1].selected) {

                var martesInitial = vm.daysOfWeek[1].initialTime + "";
                var martesFinal = vm.daysOfWeek[1].finalTime + "";
                invitationSchedule.martes = martesInitial.split(" ")[4].split(":")[0] + ":" + martesInitial.split(" ")[4].split(":")[1] + "-" + martesFinal.split(" ")[4].split(":")[0] + ":" + martesFinal.split(" ")[4].split(":")[1];
            } else {
                invitationSchedule.martes = null;
            }

            if (vm.daysOfWeek[2].selected) {

                var miercolesInitial = vm.daysOfWeek[2].initialTime + "";
                var miercolesFinal = vm.daysOfWeek[2].finalTime + "";
                invitationSchedule.miercoles = miercolesInitial.split(" ")[4].split(":")[0] + ":" + miercolesInitial.split(" ")[4].split(":")[1] + "-" + miercolesFinal.split(" ")[4].split(":")[0] + ":" + miercolesFinal.split(" ")[4].split(":")[1];
            } else {
                invitationSchedule.miercoles = null;
            }

            if (vm.daysOfWeek[3].selected) {
                var juevesInitial = vm.daysOfWeek[3].initialTime + "";
                var juevesFinal = vm.daysOfWeek[3].finalTime + "";
                invitationSchedule.jueves = juevesInitial.split(" ")[4].split(":")[0] + ":" + juevesInitial.split(" ")[4].split(":")[1] + "-" + juevesFinal.split(" ")[4].split(":")[0] + ":" + juevesFinal.split(" ")[4].split(":")[1];
            } else {
                invitationSchedule.jueves = null;
            }

            if (vm.daysOfWeek[4].selected) {
                var viernesInitial = vm.daysOfWeek[4].initialTime + "";
                var viernesFinal = vm.daysOfWeek[4].finalTime + "";
                invitationSchedule.viernes = viernesInitial.split(" ")[4].split(":")[0] + ":" + viernesInitial.split(" ")[4].split(":")[1] + "-" + viernesFinal.split(" ")[4].split(":")[0] + ":" + viernesFinal.split(" ")[4].split(":")[1];
            } else {
                invitationSchedule.viernes = null;
            }

            if (vm.daysOfWeek[5].selected) {
                var sabadoInitial = vm.daysOfWeek[5].initialTime + "";
                var sabadoFinal = vm.daysOfWeek[5].finalTime + "";
                invitationSchedule.sabado = sabadoInitial.split(" ")[4].split(":")[0] + ":" + sabadoInitial.split(" ")[4].split(":")[1] + "-" + sabadoFinal.split(" ")[4].split(":")[0] + ":" + sabadoFinal.split(" ")[4].split(":")[1];
            } else {
                invitationSchedule.sabado = null;
            }

            if (vm.daysOfWeek[6].selected) {
                var domingoInitial = vm.daysOfWeek[6].initialTime + "";
                var domingoFinal = vm.daysOfWeek[6].finalTime + "";
                invitationSchedule.domingo = domingoInitial.split(" ")[4].split(":")[0] + ":" + domingoInitial.split(" ")[4].split(":")[1] + "-" + domingoFinal.split(" ")[4].split(":")[0] + ":" + domingoFinal.split(" ")[4].split(":")[1];
            } else {
                invitationSchedule.domingo = null;
            }

            invitationSchedule.visitantInvitationId = visitant.id;
            return invitationSchedule;
        }

        function onSaveError() {
            vm.isSaving = false;
            Modal.toast("Un error inesperado ocurrió");
            Modal.hideLoadingBar();
        }

    }
})();
