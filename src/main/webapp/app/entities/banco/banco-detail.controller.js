(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BancoDetailController', BancoDetailController);

    BancoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Banco', 'Company','Egress','pagingParams','ParseLinks','Charge'];

    function BancoDetailController($scope, $rootScope, $stateParams, previousState, entity, Banco, Company,Egress,pagingParams,ParseLinks,Charge) {
        var vm = this;

        vm.banco = entity;
        vm.previousState = previousState.name;
        vm.movementsList = [];
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
        setTimeout(function(){getEgress();},700)

        function getEgress(){
             Egress.findBetweenDatesByCompany({
                    initial_time: moment(vm.dates.initial_time).format(),
                     final_time: moment(vm.dates.final_time).format(),
                     companyId: $rootScope.companyId,
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
            getIngress();
        }

        function getIngress(){
            Charge.query({}, onSuccessIngreses, onError);
        }
        function onSuccessIngreses(data, headers) {
           angular.forEach(data,function(value,key){
             value.movementType = 2;
              if(value.state==2){
                 value.paymentDate = value.date;
                 vm.movementsList.push(value);
                 console.log(value)
              }



           })


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
    }
})();
