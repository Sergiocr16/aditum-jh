(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDialogController', ComplaintDialogController);

    ComplaintDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'Complaint', 'House', 'Company', 'Resident', '$rootScope','$state', 'globalCompany','Modal'];

    function ComplaintDialogController ($timeout, $scope, $stateParams, Complaint, House, Company, Resident, $rootScope, $state, globalCompany,Modal) {
        var vm = this;
        $rootScope.mainTitle = "Registrar queja o sugerencia";
        vm.isReady = false;
        vm.complaint = {complaintType:"Vigilancia"};
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.loadResidentsByHouse = loadResidentsByHouse;
        vm.houses = House.query();
        vm.companies = Company.query();
        vm.residents = Resident.query();

            loadAll()


        function clear () {
         history.back();
        }

       function loadResidentsByHouse(houseId){
           vm.residents= [];
           Resident.findResidentesEnabledByHouseId({houseId:houseId},
               function(data){
                   for (var i = 0; i < data.length; i++) {
                       data[i].fullName =  data[i].name + " "+  data[i].lastname + " " + data[i].secondlastname;
                           vm.residents.push(data[i]);
                   }
                   console.log(vm.residents)
               },function(){
                 Modal.toast("Ah ocurrido un error cargando los residentes de la filial.")
               })
       }

        function loadAll () {
            House.query({
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
                vm.houses = data;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function save () {
            Modal.confirmDialog("¿Está seguro que desea registrar la queja o sugerencia?","",
                function(){
                    vm.isSaving = true;
                    vm.complaint.creationDate = moment(new Date).format();
                    vm.complaint.companyId = globalCompany.getId();
                    vm.complaint.status = 1;
                    vm.complaint.deleted = 0;
                    Modal.showLoadingBar();
                    if (vm.complaint.id !== null) {
                        Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
                    } else {
                        Complaint.save(vm.complaint, onSaveSuccess, onSaveError);
                    }
                });


        }

        function onSaveSuccess (result) {
            Modal.hideLoadingBar();
            Modal.toast("Se registró la queja o sugerencia exitosamente.")
            $state.go('complaint');
            vm.isSaving = false;
        }

        function onSaveError () {
            Modal.hideLoadingBar();
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.resolutionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
