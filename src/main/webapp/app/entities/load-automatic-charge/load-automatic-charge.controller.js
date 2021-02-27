(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('LoadAutomaticChargeController', LoadAutomaticChargeController);

        LoadAutomaticChargeController.$inject = ['$mdDialog', '$scope', '$state', 'House', 'globalCompany', '$rootScope', 'DataUtils', 'Modal', 'Charge'];

        function LoadAutomaticChargeController($mdDialog, $scope, $state, House, globalCompany, $rootScope, DataUtils, Modal, Charge) {
            $rootScope.active = "load-automatic-charge";
            var vm = this;
            var file;
            vm.fileName;
            vm.chargesList = [];
            vm.isReady = 0;
            House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);
            vm.creatingCharges = false;
            vm.chargeCount = 0;

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
                vm.progressUpload = "Leyendo archivo..."
                if ($file) {
                    DataUtils.toBase64($file, function (base64Data) {
                        vm.progressUpload = "Convirtiendo datos..."
                        $scope.$apply(function () {
                            vm.displayImage = base64Data;
                            vm.displayImageType = $file.type;
                        });
                    });
                    file = $file;
                    vm.fileName = file.name;
                    vm.isReady = 1;
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
                    vm.progressUpload = "Convirtiendo datos..."
                    /* DO SOMETHING WITH workbook HERE */
                    var first_sheet_name = workbook.SheetNames[0];
                    /* Get worksheet */
                    var worksheet = workbook.Sheets[first_sheet_name];
                    vm.charges = XLSX.utils.sheet_to_json(worksheet, {
                        raw: false
                    });
                    showCharges(formatCharges(vm.charges))
                };
                reader.onerror = function (ex) {
                    Modal.toast("Existe un error con el formato del archivo subido")
                    vm.isReady = 1;
                };
                reader.readAsBinaryString(file);
            };
            vm.charge = {
                type: "1",
                concept: "",
                ammount: "",
                date: "",
                valida: true,
                state: 1,
                deleted: 0
            }


            function showCharges(charges) {
                $scope.$apply(function () {
                    vm.isReady = 2;
                    vm.chargesList = charges;
                })
            }


            vm.setHouses = function () {
                if (vm.chargesList.length > 0) {
                    for (var i = 0; i < vm.chargesList.length; i++) {
                        vm.chargesList[i].houseId = vm.houseId;
                    }

                }
            }
            vm.saveCharges = function () {
                Modal.confirmDialog("¿Está seguro que desea registrar las cuotas?", "", function () {
                    vm.error = false;
                    Modal.showLoadingBar();
                    vm.creatingCharges = true;
                    Modal.toast("Se están registrando las cuotas, por favor espere y no cierre la ventana.")
                    createCharge(vm.chargesList[0], 0, vm.chargesList.length);
                    if (vm.error) {
                        Modal.toast("Se han presentado un error.")
                    }
                })
            };

            function createCharge(charge, count, length) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.chargeCount = count;
                    })
                }, 10)
                if (count < length) {
                    charge.sendEmail = vm.sendEmail;
                    Charge.save(charge, function (result) {
                        count++;
                        if (count == length) {
                            Modal.hideLoadingBar();
                            vm.isReady = 0;
                            vm.creatingCharges = false;
                            Modal.toast("Se han ingresado las cuotas correctamente.")
                        } else {
                            createCharge(vm.chargesList[count], count, vm.chargesList.length)
                        }
                    }, function () {
                        Modal.hideLoadingBar();
                        vm.creatingCharges = false;
                        Modal.actionToastGiantStay("Ocurrio un error en la creación de las cuotas después de la línea "+(parseInt(count)+2));
                        vm.error = true;
                    })
                }
            }

            function validateLineCharge(charge, i) {
                var variable = "";
                var errorCount = 0;
                if (charge.monto == undefined) {
                    variable = "monto";
                    errorCount++;
                } else {
                        var num = parseFloat(charge.monto)
                    if(isNaN(num)){
                        variable = "monto";
                        errorCount++;
                    }
                }
                if (charge.fecha == undefined) {
                    variable = "fecha";
                    errorCount++;
                }
                if (charge.concepto == undefined) {
                    variable = "concepto";
                    errorCount++;
                }
                if (errorCount > 0) {

                }
                return errorCount > 0 ? "Error en la línea " + (parseInt(i) + 2) + " en el campo " + variable : "no"
            }

            function formatCharges(charges) {
                var formatedCharges = [];
                for (var i = 0; i < charges.length; i++) {
                    vm.readingCount = i;
                    var charge = charges[i];
                    if (validateLineCharge(charge, i) == "no") {
                        var formateCharge = {};
                        formateCharge.type = defineChargeType(charge)+"";
                        formateCharge.state = 1;
                        formateCharge.deleted = 0;
                        formateCharge.ammount = charge.monto.replace(/,/g, "");
                        formateCharge.date = moment(charge.fecha, 'DD/MM/YYYY')
                        formateCharge.concept = charge.concepto;
                        formateCharge.companyId = globalCompany.getId();

                        angular.forEach(vm.houses, function (house, key) {
                            if (house.housenumber == charge.filial) {
                                formateCharge.houseId = house.id;
                            }
                        });
                        formatedCharges.push(formateCharge)
                    } else {
                        vm.chargesList = [];
                        vm.isReady = 0;
                        Modal.toast(validateLineCharge(charge, i))
                        break;
                    }
                }
                vm.isReady = 1;
                return formatedCharges;
            }

            function defineChargeType(charge) {
                switch (charge.tipo) {
                    case "FAC":
                        return 1;
                        break;
                    case "EXTRA":
                        return 2;
                        break;
                    case "AGUA":
                        return 6;
                        break;
                    case "INT":
                        return 5;
                        break;
                }
            }
        }
    }

)();
