(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminsByCompanyController', AdminsByCompanyController);

    AdminsByCompanyController.$inject = ['$state','$uibModalInstance','$stateParams','Company','DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','$rootScope', 'CommonMethods'];

    function AdminsByCompanyController($state,$uibModalInstance,$stateParams,Company,DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants,Principal,$rootScope, CommonMethods) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        var admins = [];
        vm.clear = clear;
        var companyId = CommonMethods.decryptIdUrl($stateParams.companyId);

        function clear () {
            $uibModalInstance.dismiss('cancel');
            Principal.identity().then(function(account){
            if(account.authorities[0]=="ROLE_RH"){ $state.go('company-rh');}else{ $state.go('company');}
            })
        }
          vm.showCondominios = function(adminInfo) {
               AdminInfo.get({id:adminInfo.id},function(data){
                                   bootbox.dialog({
                                       message: '<div class="text-center gray-font font-20"> <h4 class="font-30">Condominios que administra <span class="font-30" id="key_id_house"></span></h4></div> <div class="text-center gray-font" id="condos"></div>',
                                       closeButton: false,
                                       buttons: {
                                           confirm: {
                                               label: 'Ocultar',
                                               className: 'btn-success'
                                           }
                                       },
                                   })
                                   var formattedCompanies = "";

                                   angular.forEach(data.companies,function(value,index){
                                    formattedCompanies += "<h5 class='text-center font-20'>"+value.name+"<h5>"
                                   })

                                   document.getElementById("key_id_house").innerHTML = "" + data.name;
                                    document.getElementById("condos").innerHTML = formattedCompanies;
               //                    document.getElementById("security_key").innerHTML = "" + securityKey;
               //                    document.getElementById("emergency_key").innerHTML = "" + emergencyKey;

               })

            }
        loadAll ();
        function loadAll () {
            AdminInfo.getAdminsByCompanyId({
                companyId:  companyId
            }, onSuccess, onError);
            function onSuccess(data) {
                admins = data;
               Company.query(onSuccessCompany, onError);

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
            function onSuccessCompany(data) {
                vm.companies = data;
                vm.adminInfos = formatAdminInfo();
                 setTimeout(function() {
                                             $("#loadingIcon").fadeOut(300);
                                                    }, 400)
                                                     setTimeout(function() {
                                                         $("#tableData").fadeIn('slow');
                                                     },700 )
            }

        }

        vm.viewDetail = function(adminId){
           var adminInfoId = CommonMethods.encryptIdUrl(adminId);
                    $uibModalInstance.close();
                    $state.go('admin-info-detail', {id: adminInfoId});

        }
        function formatAdminInfo() {
            for (var i = 0; i < admins.length; i++) {

                for (var e = 0; e < vm.companies.length; e++) {
                    if (admins[i].companyId == vm.companies[e].id) {
                        admins[i].companyId = vm.companies[e].name;
                        admins[i].name = admins[i].name + " " + admins[i].lastname + " " + admins[i].secondlastname ;
                    }
                }
            }

            return admins;
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
