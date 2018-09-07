(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['AdminInfo', 'House', '$state', 'CompanyConfiguration', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company', 'AdministrationConfiguration', 'EgressCategory', 'Banco'];

    function CompanyDialogController(AdminInfo, House, $state, CompanyConfiguration, $timeout, $scope, $stateParams, $uibModalInstance, entity, Company, AdministrationConfiguration, EgressCategory, Banco) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.save = save;
        var egressCategories = [{
            id: null,
            group: 'Gastos fijos',
            category: 'Administración',
            companyId: null
        }, {id: null, group: 'Gastos fijos', category: 'Agua potable', companyId: null}, {
            id: null,
            group: 'Gastos fijos',
            category: 'Energía eléctrica',
            companyId: null
        }, {id: null, group: 'Gastos fijos', category: 'Seguridad', companyId: null}, {
            id: null,
            group: 'Gastos fijos',
            category: 'Jardinería',
            companyId: null
        }, {id: null, group: 'Gastos fijos', category: 'Sueldos y salarios', companyId: null}, {
            id: null,
            group: 'Gastos fijos',
            category: 'Mantenimiento áreas comunes',
            companyId: null
        }
            , {id: null, group: 'Gastos variables', category: 'Eventos', companyId: null}, {
                id: null,
                group: 'Gastos variables',
                category: 'Gastos legales',
                companyId: null
            }, {id: null, group: 'Gastos variables', category: 'Impuestos y comisiones', companyId: null}, {
                id: null,
                group: 'Gastos variables',
                category: 'Papelería y copias',
                companyId: null
            }]
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        loadQuantities();

        function loadQuantities() {
            House.query({companyId: vm.company.id}, onSuccess, onError);
        }

        function onSuccess(data) {
            vm.houseQuantity = data.length;
            if (vm.company.id != undefined) {
                getAdmins();
            }
        }

        function getAdmins() {
            AdminInfo.getAdminsByCompanyId({
                companyId: vm.company.id
            }, onSuccess);

            function onSuccess(data) {
                vm.adminsQuantity = data.length;
            }
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        getConfiguration();

        function getConfiguration() {
            CompanyConfiguration.getByCompanyId({companyId: vm.company.id}).$promise.then(onSuccessCompany, onError);
        }

        function onSuccessCompany(data) {
            angular.forEach(data, function (configuration, key) {
                vm.companyConfiguration = configuration;
            });

        }

        function onError() {

        }

        function save() {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onUpdateSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveCompanySuccess, onSaveError);

            }
        }

        function onUpdateSuccess(result) {
            CompanyConfiguration.update(vm.companyConfiguration, onSaveSuccess, onSaveError);
        }

        function onSaveCompanySuccess(result) {
            if (vm.companyConfiguration.hasContability == 1) {
                angular.forEach(egressCategories, function (value, key) {
                    value.companyId = result.id;
                    EgressCategory.save(value);
                });
                var date = moment(new Date(), 'DD/MM/YYYY').toDate()
                var banco = {
                    id: null,
                    beneficiario: 'Caja chica',
                    cedula: null,
                    cuentaCorriente: 0,
                    cuentaCliente: 0,
                    moneda: null,
                    cuentaContable: null,
                    capitalInicial: 0,
                    mostrarFactura: null,
                    fechaCapitalInicial: date,
                    saldo: 0,
                    deleted: 1,
                    companyId: result.id
                }

                Banco.save(banco);
            }
            vm.companyConfiguration.companyId = result.id;
            CompanyConfiguration.save(vm.companyConfiguration, onSaveSuccess, onSaveError);
            var adminConfig = {
                squareMetersPrice: 0, companyId: result.id, folioSerie: "A", folioNumber: 1,
                usesFinePerDay: true,
                finePerDay: '0',
                daysToBeDefaulter: 15,
                percentageFinePerDay: 5
            };
            AdministrationConfiguration.save(adminConfig);
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:companyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
