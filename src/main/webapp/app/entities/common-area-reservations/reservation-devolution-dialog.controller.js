
(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationDevolutionDialogReservations', ReservationDevolutionDialogReservations);

    ReservationDevolutionDialogReservations.$inject = ['egress','Banco','globalCompany','EgressCategory','$timeout', '$scope', '$stateParams', 'entity', 'CommonAreaReservations','Resident','House','CommonArea','Charge','$rootScope','Modal','$state','Egress'];

    function ReservationDevolutionDialogReservations (egress,Banco,globalCompany,EgressCategory,$timeout, $scope, $stateParams, entity, CommonAreaReservations,Resident,House,CommonArea,Charge,$rootScope,Modal,$state,Egress) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.commonAreaReservations = entity;
        vm.isReady = false;
        vm.minDate = new Date();
        vm.sendEmail = false;
        vm.charge = {
            type: "3",
            concept: "",
            ammount: vm.commonAreaReservations.reservationCharge,
            devolution: vm.commonAreaReservations.devolutionAmmount,
            date: null,
            valida: true,
            state: 1,
            deleted: 0
        };

        vm.egress = egress;
        vm.egress.total = vm.commonAreaReservations.devolutionAmmount;
        loadInfo();

        function loadInfo(){
            House.get({
                id:  vm.commonAreaReservations.houseId
            }, function(result) {
                vm.commonAreaReservations.houseNumber = result.housenumber;
                Resident.get({
                    id: vm.commonAreaReservations.residentId
                }, function(result) {
                    vm.commonAreaReservations.residentName = result.name + " " + result.lastname;
                    CommonArea.get({
                        id: vm.commonAreaReservations.commonAreaId
                    }, function(result) {
                        vm.commonAreaReservations.commonAreaName = result.name ;
                        vm.commonAreaReservations.schedule = formatScheduleTime(vm.commonAreaReservations.initialTime, vm.commonAreaReservations.finalTime);
                        Charge.get({
                            id: vm.commonAreaReservations.chargeIdId
                        }, function(result) {
                            vm.charge.date = result.date;
                            vm.charge.concept = result.concept;
                            EgressCategory.allCategoriesIncludingDevolution({
                                companyId: globalCompany.getId()
                            }).$promise.then(onSuccessEgressCategories);


                        });
                        vm.isReady = true;
                    })
                })
            })

        }
        function onSuccessEgressCategories(data, headers) {
            vm.searchQuery = null;
            console.log(data);
            angular.forEach(data, function (value, key) {
                console.log(value.category);
                if(value.category==="Devolución de dinero"){
                    vm.egress.category = value.id;
                }
            });
            Banco.query({companyId: globalCompany.getId()}).$promise.then(onSuccessBancos);
        }
        function onSuccessBancos(data, headers) {
            vm.isReady = true;
            vm.bancos = data;
        }
        function formatScheduleTime(initialTime, finalTime){
            var times = [];
            times.push(initialTime);
            times.push(finalTime);
            angular.forEach(times,function(value,key){
                if(value==0){
                    times[key] = "12:00AM"
                }else if(value<12){
                    times[key] = value + ":00AM"
                }else if(value>12){
                    times[key] = parseInt(value)-12 + ":00PM"
                }else if(value==12){
                    times[key] = value + ":00PM"
                }

            });
            return times[0] + " - " + times[1]
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function onSaveError () {
            vm.isSaving = false;
        }

        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        };

        function createEgress () {
            Modal.showLoadingBar();
            vm.isSaving = true;
            vm.egress.date = vm.egress.paymentDate;
            vm.egress.expirationDate = vm.egress.paymentDate;
            vm.egress.companyId = globalCompany.getId();
            vm.egress.concept = "Devolución de depósitos por uso de áreas comunes";
            vm.egress.proveedor = 0;
            vm.egress.billNumber = "0";
            vm.egress.state = 5;
            console.log(vm.egress);
            Egress.save(vm.egress, function (result) {
                vm.commonAreaReservations.status = 6;
                vm.commonAreaReservations.egressId = result.id;
                console.log(vm.commonAreaReservations);
                CommonAreaReservations.update(vm.commonAreaReservations, onSaveSuccess, onSaveError);

            });
            function onSaveSuccess(result) {
                angular.forEach(vm.bancos, function (banco, key) {
                    if (banco.id == vm.egress.account) {
                        banco.saldo = banco.saldo - vm.egress.total;
                        Banco.update(banco, function () {
                            vm.egress = result;
                            Modal.hideLoadingBar()
                            $state.go('common-area-devolution-administration.pending-devolution')
                            Modal.toast("Se realizó la devolución de depósito correctamente");

                        }, onSaveError);
                    }
                });

            }
        }

        vm.createDevolution = function () {
            Modal.confirmDialog("¿Está seguro que realizar la devolución del depósito?", "Una vez registrada esta información no se podrá editar",
                function () {
                    createEgress();
                });

        }

    }
})();
