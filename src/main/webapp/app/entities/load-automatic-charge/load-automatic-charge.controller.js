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
                vm.isReady = 1;
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
                    vm.charges = XLSX.utils.sheet_to_json(worksheet, {
                        raw: false
                    });
                    showCharges(formatCharges(vm.charges))
                };
                reader.onerror = function (ex) {
                    console.log(ex);
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


                console.log(charges)

            }


            vm.setHouses = function () {
                if (vm.chargesList.length > 0) {
                    for (var i = 0; i < vm.chargesList.length; i++) {
                        vm.chargesList[i].houseId = vm.houseId;
                    }

                }
            }
            vm.saveCharges = function () {
                vm.error = false;
                var total = vm.chargesList.length;
                var count = 0;

                Modal.showLoadingBar();
                for (var i = 0; i < vm.chargesList.length; i++) {
                    console.log(vm.chargesList[i]);
                    Charge.save(vm.chargesList[i], function (result) {
                        count++;
                        if (count == total) {
                            Modal.hideLoadingBar();
                            vm.isReady = 0;
                            Modal.toast("Se han ingresado las cuotas correctamente.")
                        }
                    }, function () {
                        Modal.hideLoadingBar();
                        vm.error = true;
                    })
                }
                if(vm.error){
                    Modal.toast("Se han presentado un error.")
                }
            };

            function formatCharges(charges) {
                var formatedCharges = [];
                for (var i = 0; i < charges.length; i++) {
                    var charge = charges[i];
                    var formateCharge = {};
                    formateCharge.type = defineChargeType(charge);
                    formateCharge.state = 1;
                    formateCharge.deleted = 0;
                    formateCharge.ammount = charge.monto.replace(/,/g, "");

                    formateCharge.date = moment(charge.fecha, 'DD/MM/YYYY')
                    formateCharge.concept = charge.concepto;
                    formateCharge.companyId = globalCompany.getId();
                    formateCharge.houseId = vm.houseId
                    formatedCharges.push(formateCharge)
                }
                return formatedCharges;
            }

            function defineChargeType(charge) {
                switch (charge.tipo) {
                    case "FAC":
                        return 1;
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
