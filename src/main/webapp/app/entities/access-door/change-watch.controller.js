(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('ChangeWatchController', ChangeWatchController);

        ChangeWatchController.$inject = ['Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'globalCompany', 'Modal', 'Watch'];

        function ChangeWatchController(Auth, $state, $scope, $rootScope, CommonMethods, globalCompany, Modal, Watch) {
            var vm = this;
            $rootScope.mainTitle = "Cambio de turno";
            vm.officersSelected = [];
            $rootScope.$watchGroup(['officers'], function () {
                vm.officerToSelect = $rootScope.officers;
            });

            vm.selectOfficer = function (officer) {
                officer.selected = true;
            };
            vm.unSelectOfficer = function (officer) {
                officer.selected = false;
            };
            vm.selectedOfficers = function () {
                var officersSelected = []
                for (var i = 0; i < $rootScope.officers.length; i++) {
                    if ($rootScope.officers[i].selected == true) {
                        officersSelected.push($rootScope.officers[i])
                    }
                }
                return officersSelected;
            };

            vm.insertWatch = function () {
                Modal.confirmDialog("¿Está seguro que desea registrar este turno?", "", function () {
                    Modal.showLoadingBar();
                    var watch = {
                        responsableofficer: serializeOfficers(vm.selectedOfficers()),
                        companyId: globalCompany.getId()
                    }
                    Watch.save(watch, onSaveSuccess, onSaveError);
                })

            };

            function onSaveSuccess(result) {
                Modal.hideLoadingBar();
                toastr["success"]("Se registró el turno correctamente");
                initialState();
            }

            function onSaveError() {
                Modal.hideLoadingBar();
                initialState();
                toastr["info"]("Se registrará el turno una vez haya vuelto la conexión.", "No hay conexión a internet");
            }

            function serializeOfficers(officers) {
                var responsableofficers = "";
                angular.forEach(officers, function (officer, key) {
                    responsableofficers += officer.id + ";";
                })
                return responsableofficers;
            }

            function initialState() {
                for (var i = 0; i < $rootScope.officers.length; i++) {
                    $rootScope.officers[i].selected = false;
                }
                vm.officersSelected = [];
            }

            $rootScope.$on('$stateChangeStart',
                function (event, toState, toParams, fromState, fromParams) {
                    initialState();
                })

        }
    }
)();
