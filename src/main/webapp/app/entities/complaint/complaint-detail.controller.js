(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDetailController', ComplaintDetailController);

    ComplaintDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Complaint', 'House', 'Company', 'Resident'];

    function ComplaintDetailController($scope, $rootScope, $stateParams, previousState, entity, Complaint, House, Company, Resident) {
        var vm = this;

        vm.complaint = entity;
        vm.complaint.showingCreationDate = moment(vm.complaint.creationDate).format('ll hh:mm a');
        vm.previousState = previousState.name;
        console.log(vm.complaints)
        setTimeout(function () {
            $("#loadingIcon").fadeOut(300);
        }, 400);
        setTimeout(function () {
            $("#tableData").fadeIn('slow');
        }, 900);
        var unsubscribe = $rootScope.$on('aditumApp:complaintUpdate', function(event, result) {
            vm.complaint = result;
        });

        vm.setStatus = function(status){
            // bootbox.confirm({
            //     message: "¿Está seguro que desea cambiar el estado de la queja o sugerencia?",
            //     buttons: {
            //         confirm: {
            //             label: 'Aceptar',
            //             className: 'btn-success'
            //         },
            //         cancel: {
            //             label: 'Cancelar',
            //             className: 'btn-danger'
            //         }
            //     },
            //     callback: function (result) {
            //         if (result) {
                        vm.complaint.status = status;
                        if (vm.complaint.id !== null) {
                            Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
                        } else {
                            Complaint.save(vm.complaint, onSaveSuccess, onSaveError);
                        }
            //         }
            //     }
            // });
        };

        function onSaveSuccess (result) {
            toastr["success"]("Se modificó el estado correctamente.")
            vm.complaint.status = result.status;
            vm.isSaving = false;
        }

        function onSaveError () {
            toastr["error"]("Ah ocurrido un error actualizando el estado de la noticia")
        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
