(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['CompanyConfiguration', 'CommonMethods', '$state', '$rootScope', 'Principal', '$timeout', '$scope', '$stateParams', 'entity', 'House', 'WSHouse', 'Balance', 'AdministrationConfiguration'];

    function HouseDialogController(CompanyConfiguration, CommonMethods, $state, $rootScope, Principal, $timeout, $scope, $stateParams, entity, House, WSHouse, Balance, AdministrationConfiguration) {
        var vm = this;
        $rootScope.active = "houses";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.house = entity;
        if (vm.house.due == null || vm.house.due == undefined) {
            vm.house.due = 0;
        }
        if (vm.house.squareMeters == null || vm.house.squareMeters == undefined) {
            vm.house.squareMeters = 0;
        }
        if (vm.house.isdesocupated != null) {
            vm.house.isdesocupated = vm.house.isdesocupated.toString()
        } else {
            vm.house.isdesocupated = "0";
        }
        vm.save = save;
        setTimeout(getConfiguration(), 800);

        function getConfiguration() {
            CompanyConfiguration.getByCompanyId({companyId: $rootScope.companyId}).$promise.then(onSuccessCompany, onError);
        }

        function onSuccessCompany(data) {
            angular.forEach(data, function (configuration, key) {
                vm.companyConfiguration = configuration;
            });
            loadQuantities();
            setTimeout(function () {
                $("#loadingIcon").fadeOut(300);
            }, 400)
            setTimeout(function () {
                $("#edit_house_form").fadeIn('slow');
            }, 900)
        }

        function onError() {
        }

        function generateLoginCode() {
            var text = "";
            var letters = "";
            var numbers = "";
            var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

            for (var i = 0; i < 4; i++)
                letters += possible.charAt(Math.floor(Math.random() * possible.length));

            numbers = Math.floor((Math.random() * 899) + 100);
            text = numbers + "" + letters;
            return text.toUpperCase();
        }


        function loadQuantities() {
            House.query({companyId: $rootScope.companyId}, onSuccess, onError);
        }

        function onSuccess(data) {
            vm.houseQuantity = data.length;

        }

        if (vm.house.id !== null) {
            vm.title = "Editar casa";
            vm.button = "Editar";

        } else {
            vm.title = "Registrar casa";
            vm.button = "Registrar";
        }


        function save() {

            if (vm.house.extension == undefined) {
                vm.extension = 'noTengoExtensionCODE';
            } else {
                vm.extension = vm.house.extension;
            }
            if (vm.house.due == null || vm.house.due == undefined) {
                vm.house.due = 0;
            }
            if (vm.house.squareMeters == null || vm.house.squareMeters == undefined) {
                vm.house.squareMeters = 0;
            }
            vm.isSaving = true;
            if (vm.house.id !== null) {
                CommonMethods.waitingMessage();
                House.validateUpdate({
                    houseId: vm.house.id,
                    houseNumber: vm.house.housenumber,
                    extension: vm.extension,
                    companyId: $rootScope.companyId
                }, onSuccessUp, onErrorUp)

            } else {
                if (vm.companyConfiguration.quantityhouses <= vm.houseQuantity) {
                    toastr['error']("Ha excedido la cantidad de casas permitidas para registrar, contacte el encargado de soporte.")
                    bootbox.hideAll();
                } else {
                    CommonMethods.waitingMessage();
                    vm.house.companyId = $rootScope.companyId;
                    vm.house.desocupationinitialtime = new Date();
                    vm.house.desocupationfinaltime = new Date();
                    vm.house.loginCode = generateLoginCode();
                    vm.house.codeStatus = 0;
                    House.validate({
                        houseNumber: vm.house.housenumber,
                        extension: vm.extension,
                        companyId: $rootScope.companyId
                    }, onSuccess, onError)
                }
            }

            function onSuccessUp(data) {
                bootbox.hideAll();
                if (vm.house.id !== data.id) {
                    toastr['error']("El número de casa o de extensión ingresado ya existe.")
                } else {
                    House.update(vm.house, onSaveSuccess, onSaveError);
                }
            }

            function onErrorUp() {
                House.update(vm.house, onSaveSuccess, onSaveError);
            }

            function onSuccess(data) {
                bootbox.hideAll();
                toastr['error']("El número de casa o de extensión ingresado ya existe.")

            }

            function onError() {
                House.save(vm.house, onSaveSuccess, onSaveError);

            }
        }

        function onSaveSuccess(result) {
            var balance = {houseId: parseInt(result.id), extraordinary: 0, commonAreas: 0, maintenance: 0};

            Balance.save(balance)

            WSHouse.sendActivity(result);
            $state.go('house');
            bootbox.hideAll();
            if (vm.house.id !== null) {
                toastr["success"]("Se editó la casa correctamente");
            } else {
                toastr["success"]("Se registró la casa correctamente");
            }

            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
