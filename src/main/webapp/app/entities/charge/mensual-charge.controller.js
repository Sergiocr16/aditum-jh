(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MensualChargeController', MensualChargeController);

    MensualChargeController.$inject = ['$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge','CommonMethods'];

    function MensualChargeController($state, House, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.radiostatus = true;
        vm.cuotaFija = true;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.verificando = false;
        moment.locale("es");
         vm.validate = function(cuota){
         var s = cuota.ammount;
             var caracteres = ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","ñ","o","p","q","r","s","t","u","v","w","x","y","z",",",".","?","/","-","+","@","#","$","%","^","&","*","(",")","-","_","=","|"]

              var invalido = 0;
              angular.forEach(caracteres,function(val,index){
              if (s!=undefined){
               for(var i=0;i<s.length;i++){
               if(s.charAt(i).toUpperCase()==val.toUpperCase()){

               invalido++;
               }
               }
               }
              })
              if(invalido==0){
              cuota.valida = true;
              }else{
               cuota.valida = false
              }
             }
        setTimeout(function() {
            loadAll();
        }, 1500)
        vm.verificarCargos = function() {
        var invalid = 0;
        angular.forEach(vm.houses, function(house, key) {
            angular.forEach(house.cuotas, function(cuota, key) {
                if (cuota.valida == false) {
                invalid++;
                }
            })
        })
        if(invalid==0){
         vm.verificando = true;
        }else{
           toastr["error"]("Porfavor verifica las cuotas ingresadas")
        }
        }
        vm.cancelar = function() {
            vm.verificando = false;
            console.log(vm.verificando)
        }
        vm.addNewDue = function() {
            vm.globalConceptNumber++;
            vm.globalConcept.push({
                date: "",
                concept: "",
                id: vm.globalConceptNumber,
                datePickerOpenStatus: false
            })
            angular.forEach(vm.houses, function(value, key) {
                if (value.isdesocupated == 1) {
                    value.cuotas.push({
                        ammount: 0,
                        globalConcept: vm.globalConceptNumber
                    })
                } else {
                    if (value.due == undefined && vm.cuotaFija == true) {
                        value.cuotas.push({
                            ammount: 0,
                            globalConcept: vm.globalConceptNumber,
                            valida: true
                        })

                    } else {
                        if (vm.cuotaFija == false) {
                            value.cuotas.push({
                                ammount: value.squareMeters * vm.adminConfig.squareMetersPrice,
                                globalConcept: vm.globalConceptNumber
                            })
                        } else {
                            value.cuotas.push({
                                ammount: value.due,
                                globalConcept: vm.globalConceptNumber
                            })
                        }


                    }
                }
            })
        }

        vm.definirCuotaFija = function() {
            vm.cuotaFija = true;
            angular.forEach(vm.houses, function(house, key) {
                angular.forEach(house.cuotas, function(cuota, key) {
                    if (house.isdesocupated == 1) {
                        cuota.ammount = 0;

                    } else {
                        cuota.ammount = house.due;

                    }
                })
            })
        }
        vm.definirCuotaMetroCuadrado = function() {
            vm.cuotaFija = false;
            angular.forEach(vm.houses, function(house, key) {
                angular.forEach(house.cuotas, function(cuota, key) {
                    if (house.isdesocupated == 1) {
                        cuota.ammount = 0;

                    } else {
                        cuota.ammount = house.squareMeters * vm.adminConfig.squareMetersPrice;


                    }
                })
            })
        }

        function buildCharge(cuota,house){
          cuota.houseId =parseInt(house.id);
          cuota.type = 1;
          cuota.date = vm.globalConcept[cuota.globalConcept].date;
          cuota.concept = vm.globalConcept[cuota.globalConcept].concept;
          cuota.state = 1;
          cuota.deleted = 0;
          return cuota;
        }

        vm.createDues = function() {
            var houseNumber = 0;

            function createCharge(houseNumber, cuotaNumber) {
                var cuota = vm.houses[houseNumber].cuotas[cuotaNumber];
                if (cuota.ammount != 0) {
                console.log(buildCharge(cuota,vm.houses[houseNumber]))
                       Charge.save(buildCharge(cuota,vm.houses[houseNumber]),function(result){

                       })
                }
                if (vm.houses[houseNumber].cuotas.length - 1 > cuotaNumber) {
                    createCharge(houseNumber, cuotaNumber + 1)
                } else {
                    if (vm.houses.length - 1 > houseNumber) {
                        var cuotaNumber = 0;
                        chargesPerHouse(houseNumber + 1)
                    }
                }
            }

            function chargesPerHouse(houseNumber) {
                var cuotaNumber = 0;
                var house = vm.houses[houseNumber]
                console.log(house.housenumber)
                createCharge(houseNumber, cuotaNumber)
            }
            chargesPerHouse(houseNumber)

        }

        vm.autoConcept = function(globalConcept) {
            String.prototype.capitalize = function() {
                return this.replace(/(?:^|\s)\S/g, function(a) {
                    return a.toUpperCase();
                });
            };

            globalConcept.concept = "Mantenimiento " + moment(globalConcept.date).format("MMMM").capitalize() + " " + moment(globalConcept.date).format("YYYY");

        }
        vm.deleteDue = function(id) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar esta columna?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        $scope.$apply(function() {
                            angular.forEach(vm.globalConcept, function(value, key) {
                                if (value.id == id) {
                                    vm.globalConcept.splice(key, 1);
                                }
                            })

                            angular.forEach(vm.houses, function(value, key) {
                                angular.forEach(value.cuotas, function(cuota, key) {
                                    if (cuota.globalConcept == id) {
                                        value.cuotas.splice(key, 1);
                                    }
                                })
                            })
                        }, 10)

                    }
                }
            });

        }

        function loadAll() {
            House.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: $rootScope.companyId
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
                    companyId: $rootScope.companyId
                }).$promise.then(function(result) {
                    vm.adminConfig = result;

                })
                vm.globalConceptNumber = 0;
                vm.globalConcept = [{
                    date: "",
                    concept: "",
                    id: vm.globalConceptNumber,
                    datePickerOpenStatus: false
                }];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                angular.forEach(data, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                    value.cuotas = [];
                    if (value.isdesocupated == 1) {
                        value.cuotas.push({
                            ammount: 0,
                            globalConcept: vm.globalConceptNumber
                        })
                    } else {
                        if (value.due == undefined) {
                            value.cuotas.push({
                                ammount: 0,
                                globalConcept: vm.globalConceptNumber
                            })

                        } else {
                            value.cuotas.push({
                                ammount: value.due,
                                globalConcept: vm.globalConceptNumber
                            })

                        }
                    }

                })
                vm.houses = data;

                vm.page = pagingParams.page;
                setTimeout(function() {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#tableData").fadeIn('slow');
                }, 700)
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
            vm.globalConcept[index].datePickerOpenStatus = true;
        }
    }
})();