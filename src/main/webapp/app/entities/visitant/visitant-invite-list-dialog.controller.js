(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInviteListDialogController', VisitantInviteListDialogController);

    VisitantInviteListDialogController.$inject = ['VisitantInvitation','$state', '$timeout', '$interval', '$scope', '$stateParams', 'Visitant', 'House', 'Company', 'Principal', '$rootScope', 'CommonMethods', 'WSVisitor', 'WSDeleteEntity', 'PadronElectoral', 'companyUser', 'globalCompany', 'Modal'];

    function VisitantInviteListDialogController(VisitantInvitation, $state, $timeout, $interval, $scope, $stateParams, Visitant, House, Company, Principal, $rootScope, CommonMethods, WSVisitor, WSDeleteEntity, PadronElectoral, companyUser, globalCompany, Modal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.clear = clear;
        $rootScope.active = "reportInvitationList";
        $rootScope.mainTitle = "Reportar reunión o fiesta";
        vm.datePickerOpenStatus = {};
        vm.openCalendarInit = openCalendarInit;
        vm.openCalendarFinal = openCalendarFinal;
        vm.save = save;

        vm.houses = House.query();
        vm.companies = Company.query();
        vm.visitors = [];
        vm.idLength = 9;
        vm.countSaved = 0;
        vm.dates = {
            initial_time: new Date(),
            final_time: new Date()
        };
        angular.element(document).ready(function () {

            vm.formatInitPickers();

            vm.addVisitor();

            CommonMethods.validateLetters();
            CommonMethods.validateNumbers();
        });

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

        function hasCaracterEspecial(s) {
            var caracteres = [",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^"]
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

        vm.hasNumbersOrSpecial = function (s) {
            var caracteres = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ",", ".", "-", "$", "@", "(", ")", "=", "+", "/", ":", "%", "*", "'", "", ">", "<", "?", "¿", "#", "!", "}", "{", '"', ";", "_", "^"]
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

        vm.validPlate = function (visitor) {
            if (hasCaracterEspecial(visitor.licenseplate) || hasWhiteSpace(visitor.licenseplate)) {
                visitor.validPlateNumber = 0;
            } else {
                visitor.validPlateNumber = 1;
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
        vm.addVisitor = function () {
            var visitor = {
                id: null,
                name: null,
                lastname: null,
                secondlastname: null,
                identificationnumber: null,
                arrivaltime: null,
                invitationstaringtime: null,
                invitationlimittime: null,
                licenseplate: null,
                isinvited: 1,
                responsableofficer: null,
                houseId: globalCompany.getHouseId(),
                companyId: globalCompany.getId(),
                type: "9",
                found: 0,
                validIdentification: 1,
                validPlateNumber: 1
            }
            vm.visitors.push(visitor)
        }

        vm.deleteVisitor = function (index) {
            vm.visitors.splice(index, 1);
        }

        vm.formatInitPickers = function () {

            var currentDate = new Date();
//            FECHAS
            vm.dates.initial_date = new Date();
            vm.dates.final_date = new Date();
            vm.minInitialDate = moment(currentDate).format("YYYY-MM-DD")

//            HORAS
            vm.dates.initial_time = new Date(1970, 0, 1, currentDate.getHours(), currentDate.getMinutes(), 0)
            vm.dates.final_time = new Date(1970, 0, 1, currentDate.getHours(), currentDate.getMinutes() + 30, 0)
            vm.minInitialTime = moment(new Date(1970, 0, 1, currentDate.getHours(), currentDate.getMinutes(), 0)).format('HH:mm')
            setTimeout(function () {
                vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm')
                vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm')
            }, 300)
        }

        vm.updateDatePicker = function () {
            vm.initialDateMinMax = moment(vm.dates.initial_date).format("YYYY-MM-DD")
            vm.finalDateMinMax = moment(vm.dates.final_date).format("YYYY-MM-DD")

        }

        vm.updateTimePicker = function () {
            vm.initialTimeMinMax = moment(vm.dates.initial_time).format('HH:mm')
            vm.finalTimeMinMax = moment(vm.dates.final_time).format('HH:mm')

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


        $timeout(function () {
//            angular.element('#focusMe').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        vm.showMessageIntitial = function () {
            Modal.toast("La fecha y hora inicial debe ser en el futuro, y no puede ser mayor a la fecha final", "Toma en consideración");
        }
        vm.showMessageFinal = function () {
            Modal.toast("La fecha y hora final debe de ser en el futuro, y no puede ser menor a la fecha inicial", "Toma en consideración");
        }

        function isValidDates() {

            function invalidDates() {
                Modal.toast("Tus fechas no tienen el formato adecuado, intenta nuevamente", "Ups!");
                vm.formatInitPickers()
                Modal.hideLoadingBar();
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
            visitor.status = 1;
            visitor.hasschedule = 0;
            visitor.houseId = globalCompany.getHouseId();
            visitor.invitationstartingtime = vm.formatDate(vm.dates.initial_date, vm.dates.initial_time);
            visitor.invitationlimittime = vm.formatDate(vm.dates.final_date, vm.dates.final_time);
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

        vm.validArray = function () {
            var nombreError = 0;
            var errorCedula = 0;
            var errorPlaca = 0;
            var errorCedLength = 0;
            angular.forEach(vm.visitors, function (visitor, i) {
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

        function save() {
            if (vm.validArray()) {

                if (isValidDates()) {
                    Modal.confirmDialog("¿Está seguro que desea reportar a todos estos visitantes?", "", function () {
                        Modal.showLoadingBar();
                        angular.forEach(vm.visitors, function (val, i) {
                            var newVisitor = formatVisitor(val);
                            vm.isSaving = true;
console.log(newVisitor)
                            VisitantInvitation.save(newVisitor, onSaveSuccess, onSaveError);
                        })
                    })
                }
            }
        }

        function onSaveSuccess(result) {
            WSVisitor.sendActivity(result);
            $scope.$emit('aditumApp:visitantUpdate', result);
            $state.go('visitant-invited-user')
            vm.countSaved++;
            Modal.hideLoadingBar();

            if (vm.countSaved == vm.visitors.length) {
                Modal.toast("Se han reportado todos los invitados correctamente");
                vm.isSaving = false;
            }
        }

        function onSaveError() {
            vm.isSaving = false;
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
