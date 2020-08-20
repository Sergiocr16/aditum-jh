(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantDialogController', VisitantDialogController);

    VisitantDialogController.$inject = ['$localStorage', 'InvitationSchedule', 'VisitantInvitation', '$state', '$timeout', '$interval', '$scope', '$stateParams', 'Visitant', 'House', 'Company', 'Principal', '$rootScope', 'CommonMethods', 'WSVisitorInvitation', 'WSDeleteEntity', 'PadronElectoral', 'globalCompany', 'Modal'];

    function VisitantDialogController($localStorage, InvitationSchedule, VisitantInvitation, $state, $timeout, $interval, $scope, $stateParams, Visitant, House, Company, Principal, $rootScope, CommonMethods, WSVisitorInvitation, WSDeleteEntity, PadronElectoral, globalCompany, Modal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        Principal.identity().then(function (account) {
            vm.adminInfo = account;
            switch (account.authorities[0]) {
                case "ROLE_USER":
                    vm.userType = 1;
                    break;
                case "ROLE_OWNER":
                    vm.userType = 1;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    break;
            }
        });

        vm.plates = [];

        vm.addPlate = function () {
            vm.plates.push({plate: undefined, valid: true});
        }
        vm.deletePlate = function (plate) {
            CommonMethods.deleteFromArray(plate, vm.plates)
        }
        vm.addPlate();
        vm.visitor = {};
        vm.clear = clear;
        $rootScope.active = "reportInvitation";
        $rootScope.mainTitle = "Reportar visitante"
        vm.datePickerOpenStatus = {};
        vm.openCalendarInit = openCalendarInit;
        vm.openCalendarFinal = openCalendarFinal;
        vm.save = save;
        vm.timeFormat = 0;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.houses = House.query();
        vm.companies = Company.query();
        vm.dates = {
            initial_time: new Date(),
            final_time: new Date()
        };
        vm.visitor = {type: "9", found: 0, validIdentification: 1, validPlateNumber: 1};

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


        angular.element(document).ready(function () {

            vm.formatInitPickers();
        });
        vm.validPlate = function (visitor) {
            if (hasCaracterEspecial(visitor.licenseplate) || hasWhiteSpace(visitor.licenseplate)) {
                visitor.validPlateNumber = 0;
            } else {
                visitor.validPlateNumber = 1;
            }
        }

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
        vm.formatInitPickers = function () {

            var currentDate = new Date();
//            FECHAS
            vm.dates.initial_date = new Date();
            vm.dates.final_date = new Date();
            vm.minInitialDate = currentDate;

//            HORAS
            vm.dates.initial_time = new Date(1970, 0, 1, currentDate.getHours(), currentDate.getMinutes(), 0);
            vm.dates.final_time = new Date(1970, 0, 1, currentDate.getHours(), currentDate.getMinutes() + 30, 0);
            vm.minInitialTime = moment(new Date(1970, 0, 1, currentDate.getHours(), currentDate.getMinutes(), 0)).format('HH:mm');
            setTimeout(function () {
                vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm');
                vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm');

            }, 300)
        };

        vm.updateDatePicker = function () {
            vm.initialDateMinMax = moment(vm.dates.initial_date).format("YYYY-MM-DD");
            vm.finalDateMinMax = moment(vm.dates.final_date).format("YYYY-MM-DD");
        };

        vm.updateTimePicker = function () {
            vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm');
            vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm');
        };

        function hasWhiteSpace(s) {
            function tiene(s) {
                return /\s/g.test(s);
            }

            if (tiene(s) || s == undefined) {
                return true
            }
            return false;
        }

        function haswhiteCedula(s) {
            return /\s/g.test(s);
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

        function hasCaracterEspecial(s) {
            var caracteres = ["`", ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^", "!"]
            var invalido = 0;
            angular.forEach(caracteres, function (val, index) {
                if (s != undefined) {
                    for (var i = 0; i < s.length; i++) {
                        if (s.charAt(i) == val) {
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

        vm.validate = function (visitor) {
            var invalido = 0;
            var invalidCed = false;
            var invalidPlate = false;
            var invalidCedLength = false;
            if (visitor.name == undefined || visitor.lastname == undefined || visitor.secondlastname == undefined) {
                invalido++;
            } else if (hasCaracterEspecial(visitor.name) || hasCaracterEspecial(visitor.lastname) || hasCaracterEspecial(visitor.secondlastname) || vm.hasNumbersOrSpecial(visitor.name) || vm.hasNumbersOrSpecial(visitor.lastname) || vm.hasNumbersOrSpecial(visitor.secondlastname)) {
                invalido++;
            } else if (hasCaracterEspecial(visitor.licenseplate) || haswhiteCedula(visitor.licenseplate)) {
                invalidPlate = true;
                visitor.validPlateNumber = 0;
            } else {
                visitor.validPlateNumber = 1;
            }
            if (visitor.identificationnumber != undefined) {
                if (hasCaracterEspecial(visitor.identificationnumber || haswhiteCedula(visitor.identificationnumber))) {
                    visitor.validIdentification = 0;
                    invalidCed = true;
                } else {
                    if (visitor.type == "9" && visitor.identificationnumber.length < 9 || hasLetter(visitor.identificationnumber && visitor.type == "9")) {
                        visitor.validIdentification = 0;
                        invalidCedLength = true;
                    } else {
                        visitor.identificationnumber = visitor.identificationnumber.replace(/\s/g, '')
                        visitor.validIdentification = 1;
                    }
                }
            }
            return {
                errorNombreInvalido: invalido,
                errorCedula: invalidCed,
                errorCedulaCorta: invalidCedLength,
                errorPlaca: invalidPlate
            }
        }
        vm.validArray = function (visitors) {
            var nombreError = 0;
            var errorCedula = 0;
            var errorPlaca = 0;
            var errorCedLength = 0;
            angular.forEach(visitors, function (visitor, i) {
                var visitorValidation = vm.validate(visitor)

                if (visitorValidation.errorCedula) {
                    errorCedula++;
                }
                if (visitorValidation.errorNombreInvalido > 0) {
                    nombreError++;
                }
                if (visitorValidation.errorPlaca) {
                    errorPlaca++;
                }
                if (visitorValidation.errorCedulaCorta) {
                    errorCedLength++;
                }
            })
            if (errorCedula > 0) {
                Modal.toast("No puede ingresar ningún caracter especial o espacio en blanco en la cédula.");

            }
            if (errorPlaca > 0) {
                Modal.toast("No puede ingresar ningún caracter especial o espacio en blanco en el número de placa");

            }
            if (nombreError > 0) {
                Modal.toast("No puede ingresar ningún caracter especial o número en el nombre.");

            }
            if (errorCedLength > 0) {
                Modal.toast("Si la nacionalidad es costarricense, debe ingresar el número de cédula igual que aparece en la cédula de identidad para obtener la información del padrón electoral de Costa Rica. Ejemplo: 10110111.");
            }

            if (errorCedula == 0 && errorPlaca == 0 && nombreError == 0 && errorCedLength == 0) {
                return true;
            } else {
                return false;
            }

        }

        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();


        $timeout(function () {
//            angular.element('#focusMe').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.showMessageIntitial = function () {
            Modal.toast("La fecha y hora inicial debe ser en el futuro, y no puede ser mayor a la fecha final");
        }
        vm.showMessageFinal = function () {
            Modal.toast("La fecha y hora final debe de ser en el futuro, y no puede ser menor a la fecha inicial");
        }

        function isValidDates() {
            function invalidDates() {
                Modal.toast("Tus fechas no tienen el formato adecuado, intenta nuevamente");
                vm.formatInitPickers()
                Modal.hideLoadingBar()
                return false;
            }

            if (vm.dates.final_time == undefined || vm.dates.initial_time == undefined || vm.dates.initial_date == undefined || vm.dates.final_date == undefined) {
                return invalidDates();
            } else {
                if (vm.formatDate(vm.dates.initial_date, vm.dates.initial_time).getTime() >= vm.formatDate(vm.dates.final_date, vm.dates.final_time).getTime()) {
                    return invalidDates();
                }
            }
            return true;
        }

        vm.formatDate = function (date, time) {
            return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), 0, 0);
        }

        function formatVisitor(visitor) {

            if (vm.timeFormat == 0) {
                visitor.invitationstartingtime = vm.formatDate(vm.dates.initial_date, vm.dates.initial_time);
                visitor.invitationlimittime = vm.formatDate(vm.dates.final_date, vm.dates.final_time);
                visitor.hasschedule = 0;
            } else {
                visitor.hasschedule = 1;
            }

            visitor.status = 1;
            if (vm.userType == 1) {
                visitor.houseId = globalCompany.getHouseId();
            } else {
                visitor.adminId = vm.adminInfo.id;
                visitor.destiny = "Oficina de administrador";
            }


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
            visitor.secondlastname = visitor.secondlastname.toUpperCase();
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
            if (vm.timeFormat == 0) {
                var arrayVisitor = [];
                arrayVisitor.push(vm.visitor);
                if (vm.validArray(arrayVisitor)) {
                    save();
                }
            } else if (vm.timeFormat == 1) {

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

        function formatPlate() {
            vm.visitor.licenseplate = "";
            for (var i = 0; i < vm.plates.length; i++) {
                var plate = vm.plates[i];
                if(plate.licenseplate != undefined){
                    if (plate.valid) {
                        vm.visitor.licenseplate = vm.visitor.licenseplate + plate.licenseplate.toUpperCase();
                        if(i+1<vm.plates.length){
                            vm.visitor.licenseplate = vm.visitor.licenseplate + " / ";
                        }
                    }
                }
            }
        }

        function save() {
            $localStorage.timeFormat = vm.timeFormat;
            Modal.confirmDialog("¿Está seguro que desea reportar este visitante?", "", function () {
                formatPlate();
                console.log(vm.visitor);
                if (vm.timeFormat == 0) {
                    if (isValidDates()) {
                        findIfVisitandExists();
                    }
                } else {
                    findIfVisitandExists();
                }


            })
        }

        function findIfVisitandExists() {
            VisitantInvitation.findInvitedByHouseAndIdentificationNumber({
                identificationNumber: vm.visitor.identificationnumber,
                houseId: globalCompany.getHouseId(),
                companyId: globalCompany.getId(),
                hasSchedule: vm.timeFormat
            }, visitantExistsInBD, visitantNoExistsInBD)
        }

        function visitantExistsInBD(data) {

            Modal.confirmDialog("Un visitante con la cédula " + vm.visitor.identificationnumber + " ya se ha invitado con anterioridad.", " ¿Desea renovar su invitación y actualizar sus datos?", function () {
                Modal.showLoadingBar();

                vm.visitor.id = data.id;
                formatVisitor(vm.visitor);
                VisitantInvitation.update(vm.visitor, onSuccess, onSaveError);

                function onSuccess(data) {
                    WSVisitorInvitation.sendActivity(data);
                    Modal.hideLoadingBar();
                    if (vm.timeFormat == 1) {
                        var invitationSchedule = formateTimesSchedule(data);
                        InvitationSchedule.findSchedulesByInvitation({
                            invitationId: data.id
                        }, function (schedule) {
                            invitationSchedule.id = schedule[0].id;
                            InvitationSchedule.update(invitationSchedule, function () {
                                $state.go('visitant-invited-user');

                                Modal.toast("Se ha renovado la invitación de " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
                            }, onSaveError);
                        });

                    } else {
                        $state.go('visitant-invited-user')
                        Modal.toast("Se ha renovado la invitación de " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
                    }


                }

            })
        }

        function visitantNoExistsInBD(result) {
            Modal.showLoadingBar();
            formatVisitor(vm.visitor);
            vm.isSaving = true;
            VisitantInvitation.save(vm.visitor, onSaveSuccess, onSaveError);

        }

        function onSaveSuccess(result) {
            WSVisitorInvitation.sendActivity(result);
            $scope.$emit('aditumApp:visitorUpdate', result);
            if (vm.timeFormat == 0) {
                finalOnSuccess();
            } else {
                var invitationSchedule = formateTimesSchedule(result);
                console.log(invitationSchedule);
                InvitationSchedule.save(invitationSchedule, finalOnSuccess, onSaveError);

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

        function finalOnSuccess() {
            $state.go('visitant-invited-user');
            Modal.hideLoadingBar();
            Modal.toast("Se ha reportado como visitante invitado a " + vm.visitor.name + " " + vm.visitor.lastname + " " + "exitosamente");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
            Modal.toast("Un error inesperado ocurrió");
            Modal.hideLoadingBar();
        }


        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendarInit(date) {
            vm.showMessageIntitial();
            vm.datePickerOpenStatus[date] = true;
        }

        function openCalendarFinal(date) {
            vm.showMessageFinal();
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
