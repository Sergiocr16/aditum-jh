(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChargePerHouseController', ChargePerHouseController);

    ChargePerHouseController.$inject = ['CustomChargeType', '$mdDialog', 'Resident', '$rootScope', '$scope', '$state', 'Charge', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage', 'Modal', '$timeout', 'Principal', 'globalCompany'];

    function ChargePerHouseController(CustomChargeType, $mdDialog, Resident, $rootScope, $scope, $state, Charge, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage, Modal, $timeout, Principal, globalCompany) {

        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.isEditing = false;
        vm.isReady = false;
        vm.downloading = false;

        var houseId;
        Principal.identity().then(function (account) {
            vm.account = account;
            switch (account.authorities[0]) {
                case "ROLE_MANAGER":
                    $rootScope.mainTitle = "Contabilidad filiales";
                    houseId = $localStorage.houseSelected.id
                    break;
                case "ROLE_USER":
                    $rootScope.mainTitle = "Deudas vigentes";
                    $rootScope.active = "chargesResidentAccount";
                    houseId = globalCompany.getHouseId();
                    break;
                case "ROLE_OWNER":
                    $rootScope.mainTitle = "Deudas vigentes";
                    $rootScope.active = "chargesResidentAccount";
                    houseId = globalCompany.getHouseId();
                    break;
            }
            loadAll();
        })

        vm.download = function () {
            vm.downloading = true;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.downloading = false;
                })
            }, 1000)
        };

        vm.createPayment = function () {
            $state.go('generatePayment')
        }
        vm.datePassed = function (cuota) {
            var rightNow = new Date();
            var chargeDate = new Date(moment(cuota.date))
            return ((chargeDate.getTime() > rightNow.getTime()))
        }

        vm.edit = function () {
            var result = {};

            function updateCharge(chargeNumber) {
                if (chargeNumber < vm.charges.length) {
                    var cuota = vm.charges[chargeNumber];
                    if (cuota.leftToPay != 0) {
                        cuota.type = parseInt(cuota.type)
                        cuota.companyId = globalCompany.getId();
                        cuota.ammount = cuota.leftToPay;
                        Charge.update(cuota, function (charge) {
                            result = charge;
                            updateCharge(chargeNumber + 1)
                        })
                    }
                } else {
                    House.get({
                        id: $localStorage.houseSelected.id
                    }, onSuccess)
                }

                function onSuccess(house) {
                    Modal.toast("Se han actualizado las cuotas correctamente.");
                    $rootScope.houseSelected = house;
                    $localStorage.houseSelected = house;
                    loadAll();
                    Modal.hideLoadingBar();
                    vm.isEditing = true;
                }
            }

            var allGood = 0;
            angular.forEach(vm.charges, function (charge, i) {
                if (charge.valida == false) {
                    allGood++;
                }
            })
            if (allGood == 0) {
                Modal.confirmDialog("¿Está seguro que desea modificar las cuotas?", "",
                    function () {
                        Modal.showLoadingBar();
                        updateCharge(0)
                    });

            } else {
                Modal.toast("Alguna de las cuotas tiene un formato inválido.")
            }
        }

        vm.open = function (charge) {
            vm.checkedType = 3;
            vm.chargeSelected = charge;
            vm.residents = [];
            Resident.getOwners({
                page: 0,
                size: 1000,
                companyId: globalCompany.getId(),
                name: " ",
                houseId: charge.houseId
            }, function (residents) {
                vm.residents = residents;
                Resident.getTenants({
                    page: 0,
                    size: 1000,
                    companyId: globalCompany.getId(),
                    name: " ",
                    houseId: charge.houseId
                }, function (tenants) {
                    angular.forEach(tenants, function (tenant, i) {

                        vm.residents.push(tenant)
                    });
                    $mdDialog.show({
                        templateUrl: 'app/entities/charge/charge-send-email-form.html',
                        scope: $scope,
                        preserveScope: true
                    });
                }, onError);


            }, onError);

            function onError() {

            }

        };

        vm.selectPrincipalContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.principalContact == 1) {
                    resident.selected = true;
                }
            });
        }
        vm.selectAllContact = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.email != null) {
                    resident.selected = true;
                } else {
                    resident.selected = false;
                }
            });
        }

        vm.selectTenant = function () {
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.type == 4 && resident.email != null) {
                    resident.selected = true;
                } else {
                    resident.selected = false;
                }
            });
        }

        vm.close = function () {
            $mdDialog.hide();
        };


        vm.sendByEmail = function () {
            Modal.showLoadingBar();
            var residentsToSendEmails = obtainEmailToList().slice(0, -1);
            Charge.sendChargeEmail({
                companyId: globalCompany.getId(),
                houseId: vm.chargeSelected.id,
                emailTo: residentsToSendEmails
            }, function (result) {
                $mdDialog.hide();
                Modal.hideLoadingBar();
                Modal.toast("Se envió la cuota por correo correctamente.");
            });
        };

        function obtainEmailToList() {
            var residentsToSendEmails = "";
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.selected == true) {
                    if (residentsToSendEmails.indexOf(resident) === -1) {
                        residentsToSendEmails = residentsToSendEmails + resident.id + ",";
                    }
                }
            });
            return residentsToSendEmails;
        }


        vm.deleteCharge = function (charge) {

            Modal.confirmDialog("¿Está seguro que desea eliminar la cuota " + charge.concept + "?", "Una vez eliminado no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    charge.deleted = 1;
                    Charge.update(charge, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        House.get({
                            id: result.houseId
                        }, onSuccess)

                        function onSuccess(house) {
                            Modal.hideLoadingBar();
                            Modal.toast("La cuota se ha eliminado correctamente.")
                            $rootScope.houseSelected = house;
                            $localStorage.houseSelected = house;
                            loadAll();
                            vm.isEditing = true;
                        }

                    }

                    function onSaveError() {

                    }
                });

        }
        vm.validCharges = function () {
            var invalido = 0;
            angular.forEach(vm.charges, function (val, index) {
                if (val.leftToPay == 0) {
                    invalido++;
                }
            })
            if (invalido == 0) {
                return true;
            } else {
                return false
            }
        }
        vm.editing = function () {
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.isEditing = true;
                    $('.dating').keydown(function () {
                        return false;
                    });
                    angular.forEach(vm.charges, function (charge, i) {
                        charge.date = new Date(vm.charges[i].date)
                    })
                })
            }, 100)

        }
        vm.createCharge = function () {
            $state.go('houseAdministration.chargePerHouse.new')
        }
        vm.cancel = function () {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            loadAll();
            vm.isEditing = false;
        }
        $scope.$watch(function () {
            return $rootScope.houseSelected;
        }, function () {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            houseId = $localStorage.houseSelected.id
            loadAll();
            vm.isEditing = false;
        });


        vm.getCategory = function (type) {
            if (parseFloat(type) < 7) {
                switch (type) {
                    case "1":
                        return "MANTENIMIENTO"
                        break;
                    case "2":
                        return "EXTRAORDINARIA"
                        break;
                    case "3":
                        return "ÁREAS COMUNES"
                        break;
                    case "5":
                        return "MULTA"
                        break;
                    case "6":
                        return "CUOTA AGUA"
                        break;
                }
            } else {
                for (var i = 0; i < vm.customChargeTypes.length; i++) {
                    var cc = vm.customChargeTypes[i];
                    if (parseFloat(type) == cc.type) {
                        return cc.description;
                    }
                }
            }
        }

        vm.openCalendar = function (charge) {
            charge.openDate = true;
        }

        function loadAll() {
            CustomChargeType.getByCompany({companyId: globalCompany.getId()}, function (result) {
                vm.customChargeTypes = result;

                Charge.queryByHouse({
                    houseId: CommonMethods.encryptS(houseId),
                    sort: sort()
                }, onSuccess, onError);

                function sort() {
                    var result = [];
                    if (vm.predicate !== 'date') {
                        result.push('date,desc');
                    }
                    return result;
                }

                function onSuccess(data, headers) {
                    vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;
                    var countPassedDate = 0;
                    var rightNow = new Date();
                    angular.forEach(data, function (cuota, i) {
                        cuota.openDate = false;
                        cuota.type = cuota.type + ""
                        var chargeDate = new Date(moment(cuota.date))
                        if (chargeDate.getTime() > rightNow.getTime()) {
                            cuota.datePassed = true;
                            if (countPassedDate == 0) {
                                cuota.definedFirstDatePassed = true;
                                countPassedDate++;
                            }
                        }
                        cuota.temporalAmmount = cuota.ammount;
                    });
                    vm.charges = data;
                    vm.page = pagingParams.page;
                    vm.isReady = true;
                }

                function onError(error) {
                    AlertService.error(error.data.message);
                }
            });
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
