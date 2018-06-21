(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDetailController', BancoDetailController);

    BancoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Banco', 'Company','Egress','pagingParams','ParseLinks','Charge','Payment','BalanceByAccount','Transferencia'];

    function BancoDetailController($scope, $rootScope, $stateParams, previousState, entity, Banco, Company,Egress,pagingParams,ParseLinks,Charge,Payment,BalanceByAccount,Transferencia) {
        var vm = this;
      vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.banco = entity;
        vm.previousState = previousState.name;
        vm.movementsList = [];
        vm.movementsListConsulting = [];
        vm.saldoInicial = 0;
        vm.totalBalance = 0;
        var saldoActual = 0;
        vm.totalIngress = 0;
        vm.totalEgress = 0;
        vm.isConsulting = false;
        var unsubscribe = $rootScope.$on('aditumApp:bancoUpdate', function(event, result) {
            vm.banco = result;
        });

        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);

         vm.dates = {
                initial_time: firstDay,
                final_time: lastDay
         };
        setTimeout(function(){getInitialBalance();},900)

        vm.consult = function(){
            vm.isConsulting = true;
            var date =  vm.dates.initial_time, y = date.getFullYear(), m = date.getMonth();
            vm.firstDayConsulting = new Date(y, m, 1);
            vm.movementsList = [];
            vm.movementsListConsulting = [];
            vm.saldoInicial = 0;
            vm.totalBalance = 0;
            saldoActual = 0;
            vm.totalIngress = 0;
            vm.totalEgress = 0;
            vm.initialDate = vm.dates.initial_time;
            getInitialBalanceBetweenDatesConsulting(vm.firstDayConsulting);
            $("#tableData").fadeOut(0);
            setTimeout(function() {
                $("#loadingIcon").fadeIn(100);
            }, 200)

        }
      function getInitialBalance(){
            vm.initialDate = vm.dates.initial_time;
            getInitialBalanceBetweenDates(vm.dates.initial_time);
        }

        vm.stopConsulting = function(){
            vm.isConsulting = false;
            vm.movementsList = [];
            vm.movementsListConsulting = [];
            vm.saldoInicial = 0;
            vm.totalBalance = 0;
            saldoActual = 0;
            vm.totalIngress = 0;
            vm.totalEgress = 0;
            var date = new Date(), y = date.getFullYear(), m = date.getMonth();
            var firstDay = new Date(y, m, 1);
            var lastDay = new Date(y, m + 1, 0);

             vm.dates = {
                    initial_time: firstDay,
                    final_time: lastDay
             };
           getInitialBalance();
            $("#tableData").fadeOut(0);
            setTimeout(function() {
                $("#loadingIcon").fadeIn(100);
            }, 200)


        }

        function getInitialBalanceBetweenDates(date){
             BalanceByAccount.findBetweenDatesByAccount({
                    initial_time: moment(date).format(),
                     final_time: moment(date).format(),
                     accountId: vm.banco.id
             }).$promise.then(onSuccessBalance);
        }
        function onSuccessBalance(data, headers) {
        if(data.length>0){
           vm.saldoInicial = data[0].balance;
           saldoActual = data[0].balance;
        }else{
           vm.saldoInicial = parseInt(vm.banco.capitalInicial);
           saldoActual = parseInt(vm.banco.capitalInicial);
        }


          getEgress();
        }

        function getEgress(){
             Egress.findBetweenDatesByCompanyAndAccount({
                    initial_time: moment(vm.dates.initial_time).format(),
                     final_time: moment(vm.dates.final_time).format(),
                     companyId: $rootScope.companyId,
                     accountId: vm.banco.id,
                     page: pagingParams.page - 1,
                     size: vm.itemsPerPage,
             },onSuccessEgresses,onError);
        }

        function onSuccessEgresses(data, headers) {
        angular.forEach(data,function(value,key){
        value.movementType = 1;
            if(value.state==2){
              vm.movementsList.push(value)
            }


        })
                 getTransferenciasEntrantes();
        }
         function getTransferenciasEntrantes(){
             Transferencia.findBetweenDatesByIncomingTransfer({
                     initial_time: moment(vm.dates.initial_time).format(),
                     final_time: moment(vm.dates.final_time).format(),
                     accountId: vm.banco.id
             },onSuccessTransferenciasEntrantes,onError);
        }

        function onSuccessTransferenciasEntrantes(data, headers) {
            angular.forEach(data,function(value,key){

                  value.concept = value.concepto;
                  value.ammount = value.monto;
                  value.paymentDate = value.fecha;
                  value.movementType = 3;
                  vm.movementsList.push(value)
            })
                getTransferenciasSalientes();
        }
       function getTransferenciasSalientes(){
             Transferencia.findBetweenDatesByOutgoingTransfer({
                     initial_time: moment(vm.dates.initial_time).format(),
                     final_time: moment(vm.dates.final_time).format(),
                     accountId: vm.banco.id
             },onSuccessTransferenciasSalientes,onError);
        }

        function onSuccessTransferenciasSalientes(data, headers) {

            angular.forEach(data,function(value,key){
                  value.concept = value.concepto;
                  value.total = value.monto;
                  value.paymentDate = value.fecha;
                  value.movementType = 4;
                  vm.movementsList.push(value)
            })
                getIngress();
        }
        function getIngress(){
            Payment.findBetweenDatesByCompanyAndAccount({
                 initial_time: moment(vm.dates.initial_time).format(),
                 final_time: moment(vm.dates.final_time).format(),
                 companyId: $rootScope.companyId,
                 accountId: vm.banco.id,
                 page: pagingParams.page - 1,
                 size: vm.itemsPerPage,
            },onSuccessIngreses,onError);

        }
        function onSuccessIngreses(data, headers) {

           angular.forEach(data,function(value,key){
             value.movementType = 2;
                 value.paymentDate = value.date;
                 vm.movementsList.push(value);



           })
              calculateBalance();


        }
      function getInitialBalanceBetweenDatesConsulting(date){
                 BalanceByAccount.findBetweenDatesByAccount({
                        initial_time: moment(date).format(),
                         final_time: moment(date).format(),
                         accountId: vm.banco.id
                 }).$promise.then(onSuccessBalanceConsulting);
            }
            function onSuccessBalanceConsulting(data, headers) {
                 if(data.length>0){
                          saldoActual = data[0].balance;
                    }else{
                       saldoActual = parseInt(vm.banco.capitalInicial);
                    }
                  getEgressWhenConsulting();

            }
            function getEgressWhenConsulting(){
                 Egress.findBetweenDatesByCompanyAndAccount({
                        initial_time: moment(vm.firstDayConsulting).format(),
                         final_time: moment(vm.dates.initial_time).subtract('days', 1).format(),
                         companyId: $rootScope.companyId,
                         accountId: vm.banco.id,
                         page: pagingParams.page - 1,
                         size: vm.itemsPerPage,
                 },onSuccessEgressesConsulting,onError);
            }

            function onSuccessEgressesConsulting(data, headers) {
            angular.forEach(data,function(value,key){
            value.movementType = 1;
                if(value.state==2){
                  vm.movementsListConsulting.push(value)
                }


            })
                getTransferenciasEntrantesWhenConsulting();
            }

        function getTransferenciasEntrantesWhenConsulting(){
                 Transferencia.findBetweenDatesByIncomingTransfer({
                         initial_time: moment(vm.firstDayConsulting).format(),
                         final_time:  moment(vm.dates.initial_time).subtract('days', 1).format(),
                         accountId: vm.banco.id
                 },onSuccessTransferenciasEntrantesConsulting,onError);
            }

            function onSuccessTransferenciasEntrantesConsulting(data, headers) {
                angular.forEach(data,function(value,key){

                      value.concept = value.concepto;
                      value.ammount = value.monto;
                      value.paymentDate = value.fecha;
                      value.movementType = 3;
                      vm.movementsListConsulting.push(value)
                })
                    getTransferenciasSalientesWhenConsulting();
            }
           function getTransferenciasSalientesWhenConsulting(){
                 Transferencia.findBetweenDatesByOutgoingTransfer({
                         initial_time:  moment(vm.firstDayConsulting).format(),
                         final_time: moment(vm.dates.initial_time).subtract('days', 1).format(),
                         accountId: vm.banco.id
                 },onSuccessTransferenciasSalientesConsulting,onError);
            }

            function onSuccessTransferenciasSalientesConsulting(data, headers) {
                angular.forEach(data,function(value,key){
                      value.concept = value.concepto;
                      value.total = value.monto;
                      value.paymentDate = value.fecha;
                      value.movementType = 4;
                      vm.movementsListConsulting.push(value)
                })
                    getIngressWhenConsulting();
            }
        function getIngressWhenConsulting(){

            Payment.findBetweenDatesByCompanyAndAccount({
                 initial_time: moment(vm.firstDayConsulting).format(),
                 final_time: moment(vm.dates.initial_time).subtract('days', 1).format(),
                 companyId: $rootScope.companyId,
                 accountId: vm.banco.id,
                 page: pagingParams.page - 1,
                 size: vm.itemsPerPage,
            },onSuccessIngresesConsulting,onError);

        }
        function onSuccessIngresesConsulting(data, headers) {

           angular.forEach(data,function(value,key){
             value.movementType = 2;
                 value.paymentDate = value.date;
                 vm.movementsListConsulting.push(value);

           })

           calculateBalanceConsulting();


        }

         function calculateBalanceConsulting(){
                vm.movementsListConsulting.sort(function(a,b) { return new Date(a.paymentDate).getTime() - new Date(b.paymentDate).getTime() });
                angular.forEach(vm.movementsListConsulting,function(value,key){
                    if(value.movementType==2 || value.movementType==3){
                        var ammount = parseInt(value.ammount);
                        value.balance = saldoActual + ammount;
                        saldoActual = value.balance;
                    } else if(value.movementType==1 || value.movementType==4){
                        var ammount = parseInt(value.total);
                        value.balance = saldoActual - ammount;
                        saldoActual = value.balance;
                    }
                   })
                vm.saldoInicialConsulting = saldoActual;
                getEgress();

            }
         vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
             var rgx = /(\d+)(\d{3})/;
             while (rgx.test(x1)) {
                     x1 = x1.replace(rgx, '$1' + ',' + '$2');
             }
             return x1 + x2;
         }
        function calculateBalance(){
            vm.movementsList.sort(function(a,b) { return new Date(a.paymentDate).getTime() - new Date(b.paymentDate).getTime() });
            angular.forEach(vm.movementsList,function(value,key){
                if(value.movementType==2 || value.movementType==3){
                    var ammount = parseInt(value.ammount);
                    value.balance = saldoActual + ammount;
                    saldoActual = value.balance;
                    vm.totalIngress = vm.totalIngress + ammount;
                } else if(value.movementType==1 || value.movementType==4){
                    var ammount = parseInt(value.total);
                    value.balance = saldoActual - ammount;
                    saldoActual = value.balance;
                    vm.totalEgress = vm.totalEgress + ammount;
                }

               })
            vm.totalBalance = saldoActual;
            if(vm.totalBalance>0){
                vm.balanceColor = 'green';
            }else if(vm.totalBalance==0){
                vm.balanceColor = 'black';
            } else{
                vm.balanceColor = 'red';
            }
            setTimeout(function() {
                 $("#loadingIcon").fadeOut(300);
            }, 400)
             setTimeout(function() {
                 $("#tableData").fadeIn('slow');
            },900 )

        }

            vm.updatePicker = function() {
                    vm.picker1 = {
                        datepickerOptions: {
                           maxDate: vm.dates.final_time,
                            enableTime: false,
                            showWeeks: false,
                        }
                    };
                    vm.picker2 = {
                        datepickerOptions: {
                            minDate: vm.dates.initial_time,
                            enableTime: false,
                            showWeeks: false,
                        }
                    }
                }

        function onError(error) {
            AlertService.error(error.data.message);
        }
        $scope.$on('$destroy', unsubscribe);
        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
             vm.datePickerOpenStatus[date] = true;
        }
    }
})();
