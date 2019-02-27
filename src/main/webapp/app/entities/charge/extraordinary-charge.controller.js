(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ExtraordinaryChargeController', ExtraordinaryChargeController);

    ExtraordinaryChargeController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', 'globalCompany', 'Modal'];

    function ExtraordinaryChargeController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, globalCompany, Modal) {
        var vm = this;
        $rootScope.active = 'extraordinary';
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.datePickerOpenStatus = false;
        vm.isReady = false;
        $rootScope.mainTitle = "Generar Cuota extraordinaria";
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.verificando = false;
        vm.selectedAll = false;
        vm.isDivided = false;
        vm.dividedChargeQuantity = 2;
        vm.totalPerHouseAmmount = 0;
        vm.dividedChargeAmmount = 0;
        vm.currentDate = new Date();
        vm.globalConcept = {
            date: "",
            text: undefined,
            cuota: {
                ammount: undefined,
                valid: true
            },
            type: 2
        };
        vm.createDividedChargeArray = function() {
           var a = [];
            for (var i = 2; i <= 30; i++) {
                a.push(i)
            }
            vm.cuotasNumber = a;
        };
        vm.createDividedChargeArray();

        moment.locale("es");
        moment.addRealMonth = function addRealMonth(d, m) {
            var fm = moment(d).add(m, 'M');
            var fmEnd = moment(fm).endOf('month');
            return d.date() != fm.date() && fm.isSame(fmEnd.format('YYYY-MM-DD')) ? fm.add(m, 'd') : fm;
        }
        vm.isDividedDisable = function(){
            var disabled =  vm.dividedChargeDate != undefined && vm.dividedChargeQuantity>=2 &&  vm.dividedChargeAmmount > 0 && vm.dividedChargeAmmount != undefined && vm.dividedChargeConcept!="" && vm.dividedChargeConcept!=undefined;
            return disabled;
        }
        vm.changeDividedCharge = function () {
            if(!vm.dividedCharge){
                vm.isDivided = false;
            }
        }

        vm.getTotalPerHouses = function(){
            var count = 0 ;
            angular.forEach(vm.houses, function (house, key) {
                if (house.isIncluded == true) {
                    count++;
                }
            })
            return count*vm.totalPerHouseAmmount;
        }
        vm.generateDividedCharges = function () {
            vm.dividedCharges = [];
            var ammount = vm.dividedChargeAmmount;
            var date = vm.dividedChargeDate;
            for (var i = 1; i <= vm.dividedChargeQuantity; i++) {
                var charge = {
                    concept: vm.dividedChargeConcept + " (" + i + "/" + vm.dividedChargeQuantity + ")",
                    ammount: ammount
                };
                if (i != 1) {
                    date = moment.addRealMonth(moment(date), i - 1);
                }
                charge.date = date;
                vm.dividedCharges.push(charge)
            }
            vm.isDivided = true;
            vm.totalPerHouseAmmount = ammount*vm.dividedChargeQuantity;
        }

        vm.selectAll = function () {
            angular.forEach(vm.houses, function (house, i) {
                if (vm.selectedAll == true && house.isdesocupated == 0) {
                    house.isIncluded = true;
                } else {
                    house.isIncluded = false;
                }
            })
        }
        vm.globalCuotaSelected = function () {
            if (vm.globalConcept.cuota.ammount != undefined && vm.globalConcept.cuota.valida == true) {
                Modal.confirmDialog("¿Está seguro que desea modificar la cuota de todas las cuotas?", "",
                    function () {

                        angular.forEach(vm.houses, function (house, i) {

                            house.cuota.ammount = vm.globalConcept.cuota.ammount;

                        })

                    });


            }
        }
        vm.globalConceptSelected = function () {
            if (vm.globalConcept.text != undefined) {

                Modal.confirmDialog("¿Está seguro que desea modificar el concepto de todas las cuotas?", "",
                    function () {
                        angular.forEach(vm.houses, function (house, i) {

                            house.cuota.concept = vm.globalConcept.text;

                        })

                    });

            }
        }
        vm.validate = function (cuota) {
            var s = cuota.ammount;
            var caracteres = ['´', 'Ç', '_', 'ñ', 'Ñ', '¨', ';', '{', '}', '[', ']', '"', "¡", "!", "¿", "<", ">", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ",", ".", "?", "/", "-", "+", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "|"]

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
                cuota.valida = true;
            } else {
                cuota.valida = false
            }
        }
        loadAll();
        vm.verificarCargos = function () {
            var invalid = 0;
            vm.selectedHouses = [];
            angular.forEach(vm.houses, function (house, key) {
                if (house.isIncluded == true) {
                    vm.selectedHouses.push(house)
                }
            })
            angular.forEach(vm.selectedHouses, function (house, key) {
                if (house.cuota.valida == false) {
                    invalid++;
                }
            })

            if (vm.selectedHouses.length == 0) {
                Modal.toast("Debe de seleccionar almenos una casa para realizar una cuota.")
            } else {
                if (invalid == 0) {
                    vm.verificando = true;
                } else {
                    Modal.toast("Porfavor verifica las cuotas ingresadas")
                }
            }
        }
        vm.cancelar = function () {
            vm.verificando = false;
        }

        function buildCharge(house) {
            house.cuota.houseId = parseInt(house.id);
            house.cuota.type = vm.globalConcept.type;
            house.cuota.date = vm.globalConcept.date;
            house.cuota.companyId = globalCompany.getId();
            house.cuota.state = 1;
            house.cuota.deleted = 0;
            return house.cuota;
        }

        function buildChargeDivided(house,dividedCharge) {
            var cuota = {};
            cuota.houseId = parseInt(house.id);
            cuota.type = vm.globalConcept.type;
            cuota.date = dividedCharge.date;
            cuota.companyId = globalCompany.getId();
            cuota.ammount = vm.dividedChargeAmmount;
            cuota.state = 1;
            cuota.deleted = 0;
            cuota.concept = dividedCharge.concept
            return cuota;
        }
        vm.createDues = function(){
            if(!vm.dividedCharge){
                vm.createDuesNormal()
            }else{
                vm.createDuesDivided()
            }
        }
        vm.createDuesNormal = function () {
            var allReady = 0;
            Modal.showLoadingBar();
            angular.forEach(vm.selectedHouses, function (house, i) {
                if (house.cuota.ammount != 0) {
                    Charge.save(buildCharge(house), function (result) {
                        allReady++;
                        if (parseInt(allReady) == parseInt(vm.selectedHouses.length)) {
                            Modal.hideLoadingBar();
                            Modal.toast("Se generaron las cuotas extraordinarias correctamente.")
                            $state.go('extraordinaryCharge', null, {
                                reload: true
                            })
                        }

                    })
                }
            })
        }
        vm.createDuesDivided = function () {
            var allReady = 0;
            Modal.showLoadingBar();
            angular.forEach(vm.selectedHouses, function (house, i) {
                for (var j = 0; j < vm.dividedCharges.length; j++) {
                    var readyCharge = 0;
                    Charge.save(buildChargeDivided(house,vm.dividedCharges[j]), function (result) {
                        readyCharge++;
                        if (parseInt(readyCharge) == parseInt(vm.dividedCharges.length)) {
                            allReady++;
                        }
                        if (parseInt(allReady) == parseInt(vm.selectedHouses.length)) {
                            Modal.hideLoadingBar();
                            Modal.toast("Se generaron las cuotas extraordinarias correctamente.")
                            $state.go('extraordinaryCharge', null, {
                                reload: true
                            })
                        }
                    })
                }

            })
        }

        function loadAll() {
            House.query({
                page: pagingParams.page - 1,
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                AdministrationConfiguration.get({
                    companyId: globalCompany.getId()
                }).$promise.then(function (result) {
                    vm.adminConfig = result;

                })

                vm.globalConcept = {
                    date: "",
                    text: undefined,
                    type: "2"
                };
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    if (value.isdesocupated == 1) {
                        value.cuota = ({
                            ammount: 0,
                            concept: ""
                        })
                    } else {
                        if (value.due == undefined) {
                            value.cuota = ({
                                ammount: 0,
                                concept: ""
                            })

                        } else {
                            value.cuota = ({
                                ammount: 0,
                                concept: ""
                            })

                        }
                    }
                    value.isIncluded = false;
                })
                vm.houses = data;

                vm.page = pagingParams.page;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
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

        function openCalendar(index) {
            vm.datePickerOpenStatus = true;
        }

    }
})();
