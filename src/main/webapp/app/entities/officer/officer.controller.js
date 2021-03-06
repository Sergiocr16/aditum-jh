(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerController', OfficerController);

    OfficerController.$inject = ['User', '$state', 'CommonMethods', 'DataUtils', 'Officer', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', 'globalCompany', 'companyUser', 'Modal', 'WSDeleteEntity', 'WSOfficer'];

    function OfficerController(User, $state, CommonMethods, DataUtils, Officer, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, globalCompany, companyUser, Modal, WSDeleteEntity, WSOfficer) {

        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        $rootScope.mainTitle = "Oficiales de seguridad";
        vm.isReady = false;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.radiostatus = true;
        vm.isReady = false;

        loadAll();

        $rootScope.active = "officers";
        $rootScope.mainTitle = "Oficiales";
        vm.editOfficer = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('officer.edit', {
                id: encryptedId
            })
        }
        vm.switchEnabledResidents = function () {
            enabledOptions = true;
            vm.radiostatus = true;

            vm.isReady = false;
            loadAll();
        }
        vm.switchDisabledResidents = function () {
            enabledOptions = false;
            vm.radiostatus = false;
            vm.isReady = false;

            loadAll();
        }

        function loadAll() {
            if (companyUser != undefined) {
                vm.canEditOfficers = companyUser.administraOficiales;
            }

            if (enabledOptions) {
                changesTitles();
                Officer.officersEnabled({
                    companyId: globalCompany.getId(),
                }).$promise.then(onSuccess, onError);
            } else {
                changesTitles();
                Officer.officersDisabled({
                    companyId: globalCompany.getId(),
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {
                vm.officers = data;
                vm.isReady = true;


            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

            vm.detailOfficer = function (id) {
                var encryptedId = CommonMethods.encryptIdUrl(id)
                $state.go('officer.details', {
                    id: encryptedId
                })
            }

            function changesTitles() {
                if (vm.canEditOfficers == 1) {

                    if (enabledOptions) {
                        vm.title = "Oficiales habilitados";
                        vm.buttonTitle = "Ver oficiales deshabilitados";
                        vm.actionButtonTitle = "Deshabilitar";

                        vm.iconDisabled = "fa fa-user-times";
                        vm.color = "red-font";

                    } else {
                        vm.title = "Oficiales deshabilitados";
                        vm.buttonTitle = "Ver oficiales habilitados";
                        vm.actionButtonTitle = "Habilitar";
                        vm.iconDisabled = "fa fa-undo";
                        vm.titleDisabledButton = "Habilitar oficial";
                        vm.color = "green";
                    }
                } else {
                    vm.title = "Oficiales asignados al condominio";
                }
            }
        }


        vm.deleteOfficer = function (officer) {
            Modal.confirmDialog("¿Está seguro que desea eliminar al oficial " + officer.name + " " + officer.lastname + "?", "", function () {
                vm.login = officer.userLogin;
                Officer.delete({
                    id: officer.id
                }, onSuccessDelete);
                WSDeleteEntity.sendActivity({type: 'officer', id: officer.id})
            });


            function onSuccessDelete(result) {
                Modal.toast("Se ha eliminado el oficial correctamente.");
                loadAll();
            }

        };

        vm.disableEnabledOfficer = function (officerInfo) {
            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al oficial " + officerInfo.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al oficial " + officerInfo.name + "?";
            }
            Modal.confirmDialog(correctMessage, "", function () {
                Modal.showLoadingBar();
                Officer.get({id: officerInfo.id}).$promise.then(onSuccessGetOfficer);

            });

        };

        function onSuccessGetOfficer(result) {
            enabledDisabledOfficer(result);
        }

        function enabledDisabledOfficer(officer) {
            if (enabledOptions) {
                officer.enable = false;
                Officer.update(officer, onSuccessDisabledOfficer);
            } else {
                officer.enable = true;
                Officer.update(officer, onSuccessEnabledOfficer);

            }
        }

        function onSuccessDisabledOfficer(data, headers) {
            WSOfficer.sendActivity(data);
            Modal.toast("Se ha deshabilitado el oficial correctamente.");
            Modal.hideLoadingBar();
            loadAll();

        }

        function onSuccessEnabledOfficer(data, headers) {
            WSOfficer.sendActivity(data);

            Modal.toast("Se ha habilitado el oficial correctamente.");
            Modal.hideLoadingBar();
            loadAll();

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
