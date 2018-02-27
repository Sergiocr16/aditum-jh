(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchController', WatchController);

    WatchController.$inject = ['Watch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope','CommonMethods','$stateParams','Company','$state','$scope'];

    function WatchController(Watch, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope,CommonMethods, $stateParams, Company, $state, $scope) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.isConsulting = false;
        vm.showCleanBtn = false;
        vm.showBackBtn = false;
        vm.currentTurn = true;
        moment.locale('es');
        vm.company = {};
        vm.company.name = "Condominio";
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        var companyId = CommonMethods.decryptIdUrl($stateParams.companyId)
        $rootScope.active = "watches";
        angular.element(document).ready(function() {
            $('.dating').keydown(function() {
                return false;
            });
        });

        function formatWatch(watch) {
            watch.initialtime = moment(vm.watch.initialtime).format('h:mm a');
            if (watch.finaltime === null) {
                watch.finaltime = 'Aún en progreso'
            } else {
                watch.finaltime = moment(vm.watch.finaltime).format('h:mm a');
            }
            watch.officers = getformatResponsableOfficers(vm.watch);
            return watch;
        }



        function setWatch(data) {
            vm.showTable = false;
            vm.currentTurn = false;
            vm.showBackBtn = true;
            vm.watch = data;
            vm.day = moment(vm.watch.initialtime).format('LL');
            vm.watch = formatWatch(vm.watch);
        }

        vm.filterWatches = function() {
            $("#data").fadeOut(0);
            setTimeout(function() {
                $("#loadingData").fadeIn(300);
            }, 200)
           if(vm.showFullDatePicker==true){
            Watch.findBetweenDates({
                initial_time: moment(vm.consulting_initial_time).format(),
                final_time: moment(vm.consulting_final_time).format(),
                companyId: parseInt(companyId),
            }, onSuccessBetweenDates, onErrorBetweenDates)
           }else{

           if(vm.daySelectedMinimo.fechaMinima.getTime()> vm.daySelectedMaximo.fechaMaxima.getTime()){
           toastr["error"]("La fecha máxima debe de ser mayor a la mínima")
           vm.getCurrentWatch()
           }else{
           Watch.findBetweenDates({
                initial_time: moment(vm.daySelectedMinimo.fechaMinima).format(),
                final_time: moment(vm.daySelectedMaximo.fechaMaxima).format(),
                companyId: parseInt(companyId),
            }, onSuccessBetweenDates, onErrorBetweenDates)
            }
            }
            function onSuccessBetweenDates(data, headers) {
           if(vm.showFullDatePicker==true){
                vm.showConsultingInitialTime = moment(vm.consulting_initial_time).format('ll');
                vm.showConsultingFinalTime = moment(vm.consulting_final_time).format('ll');
                }else{
                 vm.showConsultingInitialTime = moment(vm.daySelectedMinimo.fechaMinima).format('ll');
                                vm.showConsultingFinalTime = moment(vm.daySelectedMaximo.fechaMaxima).format('ll');
                }
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.watches = data;
                vm.page = pagingParams.page;
                $("#loadingData").fadeOut(0);
                setTimeout(function() {
                    $("#data").fadeIn(300);
                }, 200)
                vm.showCleanBtn = true;
                vm.showTable = true;
                vm.showBackBtn = false;
            }

            function onErrorBetweenDates(error) {
                AlertService.error(error.data.message);
            }
        }

        function createFiveDaysBehind(){
        vm.days = [];
        for(var i = 0;i<=4;i++){
        var diaAnterior = {}
         var today = new Date();
         today.setDate(today.getDate()-i);
         today.setHours(0);
         today.setMinutes(0);
         today.setSeconds(0);
         diaAnterior.fechaMinima = today;
         today.setHours(23);
         today.setMinutes(59);
         today.setSeconds(59);
         diaAnterior.fechaMaxima =  today;
         diaAnterior.fecha = moment(today).format("ll")
         diaAnterior.id = i;
         vm.days.push(diaAnterior)
        }
        }
        vm.loadAll = function(){
        Principal.identity().then(function(account){
        if(account.login == 'lh-admin' || account.authorities[0]!="ROLE_MANAGER"){
        vm.showFullDatePicker = true;
        }else{
        vm.showFullDatePicker = false;
        createFiveDaysBehind();
        }
         if(account.authorities[0]=="ROLE_RH"){
             Company.get({id:parseInt(companyId)},function(company){
             vm.company = company;
             vm.getCurrentWatch();
             },function(){
              if(account.authorities[0]=="ROLE_RH"){
              $state.go('company-rh')
              }else{
              $state.go('dashboard')
              }
             })
         }else{
            Company.get({id:parseInt(companyId)},function(company){
            vm.company = company;
            vm.getCurrentWatch();
            },function(){
             if(account.authorities[0]=="ROLE_RH"){
             $state.go('company-rh')
             }else{
             $state.go('dashboard')
             }
            })
         }
        })
        }

        vm.getCurrentWatch = function() {
            $("#data").fadeOut(0);
            setTimeout(function() {
                $("#loadingData").fadeIn(300);
                $("#backBTN").fadeIn(300);
            }, 200)
            Watch.getCurrent({
                companyId: parseInt(companyId)
            }, onSuccessCurrent, onErrorCurrent);

            function onSuccessCurrent(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.isData = true;

                setWatch(data);
                vm.currentTurn = true;
                vm.showBackBtn = false;
                vm.showCleanBtn = false;
                vm.consulting_initial_time = "";
                vm.consulting_final_time = "";
                  setTimeout(function() {
                       $("#loadingData").fadeOut(300);
                                                        }, 400)
                                                         setTimeout(function() {
                                                             $("#data").fadeIn('slow');

                                                         },900 )
            }

            function onErrorCurrent(error) {
              setTimeout(function() {
                                  $("#loadingData").fadeOut(300);
                                                                   }, 400)
                                                                    setTimeout(function() {
                                                                        $("#data").fadeIn('slow');

                                                                    },900 )
                vm.isData = false;
                AlertService.error(error.data.message);
            }
        }

        setTimeout(function(){vm.loadAll();},500)

        vm.getWatch = function(turnoId) {
            $("#data").fadeOut(0);
            setTimeout(function() {
                $("#loadingData").fadeIn(300);
            }, 200)

            Watch.get({
                id: parseInt(turnoId)
            }, onSuccess, onError)

            function onSuccess(data, headers) {
                $("#loadingData").fadeOut(0);
                setTimeout(function() {
                    $("#data").fadeIn(300);
                }, 200)
                setWatch(data);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.updatePicker = function() {
            vm.picker1 = {
                date: new Date(),
                datepickerOptions: {
                    maxDate: vm.consulting_final_time == undefined ? new Date() : vm.consulting_final_time,
                    enableTime: false,
                    showWeeks: false,

                }
            };
            vm.picker2 = {
                date: new Date(),
                datepickerOptions: {
                    minDate: vm.consulting_initial_time,
                    maxDate: new Date(),
                    enableTime: false,
                    showWeeks: false,
                }

            }
        }

        vm.updatePicker();

        function formatResponsableOfficer(stringOfficer) {
            var variables = stringOfficer.split(';')
            var officer = {};
            officer.id = variables[0];
            officer.identificationnumber = variables[1];
            officer.name = variables[2];
            return officer;
        }

        function getformatResponsableOfficers(watch) {
            var formattedOfficers = [];
            var stringOfficers = watch.responsableofficer.slice(0, -2);
            var officers = stringOfficers.split('||');
            angular.forEach(officers, function(officer, key) {
                formattedOfficers.push(formatResponsableOfficer(officer))
            })
            return formattedOfficers;
        }

        function loadAll() {
            Watch.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: companyId
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.isData = vm.queryCount > 0 ? true : false;
                vm.watches = data;
                vm.page = pagingParams.page;
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

        vm.datePickerOpenStatus.initialtime = false;
        vm.datePickerOpenStatus.finaltime = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

    }
})();


