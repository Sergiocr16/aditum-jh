(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CreateProfileController', CreateProfileController);

    CreateProfileController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','Company','Brand'];

    function CreateProfileController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,Company,Brand) {
        var vm = this;

        vm.piregistered = 1;
        vm.required = 1;
        vm.required2 = 1;
        vm.vehicules = [];
        $('a[title]').tooltip();

        angular.element(document).ready(function () {
            $('body').removeClass("gray");
            $rootScope.showLogin = false;
            $rootScope.menu = false;
            $rootScope.isInManual = true;
        });
        loadHouse();

        angular.element(document).ready(function () {
            ColorPicker.init();
        });

        function loadHouse(){
            House.getByLoginCode({
                loginCode: $state.params.loginCode
            }).$promise.then(onSuccessHouse, onError);

        }

        function onSuccessHouse(data) {
            vm.house = data;
            loadCompany();

        }
        function onError() {

        }

        function loadCompany(){
            Company.get({
                id: vm.house.companyId
            }).$promise.then(onSuccessCompany);

        }

        function onSuccessCompany(data) {
            vm.company = data;
            Brand.query({}, onSuccessBrand);
        }
        function onSuccessBrand(brands){
            vm.brands = brands;
            vm.addVehiculeToList();
        }
        vm.deleteVehiculeFromList = function(index){

            vm.vehicules.splice(index,1)
        }
        vm.addVehiculeToList = function(){
            var vehicule = {licensePlate:null,brand:null,color:"#ffff",enabled:true,type:null,house_id:vm.house.id,company_id:vm.company.id}
            vm.vehicules.push(vehicule);

        }

        vm.createProfile = function () {
            vm.piregistered = 2;
        }

        vm.submitColor = function(vehicule,index) {
           vehicule.color = $('#index').css('background-color');
        }
        vm.changeState = function(view){
            switch(view){
                case 1:
                    $("#profile").fadeOut(0);
                    $("#profileli").removeClass("active");
                    $("#addCar").fadeOut(0);
                    $("#carli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");

                    $("#home").fadeIn(500);
                    $("#homeli").addClass( "active" );


                break;
                case 2:
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#addCar").fadeOut(0);
                    $("#carli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");

                    $("#profileli").addClass( "active" );
                    $("#profile").fadeIn(500);
                break;
                case 3:
                    $("#profile").fadeOut(0);
                    $("#profileli").removeClass("active");
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");

                    $("#carli").addClass( "active" );
                    $("#addCar").fadeIn(500);
                 break;
                 case 4:
                    $("#profile").fadeOut(0);
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#profileli").removeClass("active");
                    $("#addCar").fadeOut(0);
                    $("#carli").removeClass("active");



                    $("#residentli").addClass( "active" );
                    $("#resident").fadeIn(500);
                 break;

            }

        }
    }
})();
