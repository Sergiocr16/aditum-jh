(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDetailController', RegulationDetailController);

    RegulationDetailController.$inject = ['Modal','AlertService','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Regulation', 'Company'];

    function RegulationDetailController(Modal,AlertService,$scope, $rootScope, $stateParams, previousState, entity, Regulation, Company) {
        var vm = this;
        vm.isReady = false;
        vm.showReference=false;
        vm.waiting = false;
        vm.regulation = entity;
        console.log(entity)
        var completeRegulation = entity;
        vm.showCategoriesAndKeys = true;
        vm.previousState = previousState.name;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        $rootScope.active = "regulation";
        var unsubscribe = $rootScope.$on('aditumApp:regulationUpdate', function(event, result) {
            vm.regulation = result;
        });
        if(vm.regulation.companyId!=null){
            Company.get({id: parseInt(vm.regulation.companyId)}, function (company) {
                vm.regulation.company = company;
                vm.isReady = true;
                console.log( vm.regulation.chapters)
            });
        }else{
            vm.isReady = true;
        }

        vm.consult = function (item,type) {
            vm.waiting = true;
            vm.justCategory = false;
            vm.justKeyWord = false;
            vm.searchCategoriesDTO = {};
            vm.searchCategoriesDTO.regulationDTO = vm.regulation;
            vm.searchCategoriesDTO.categories = [];
            vm.searchCategoriesDTO.keyWords = [];
            if(type==1){
                vm.categorySelect = item;
                vm.searchCategoriesDTO.categories.push(item.id);
                vm.justCategory = true;
            }else{
                vm.keyWordSelect = item;
                vm.searchCategoriesDTO.keyWords.push(item.id);
                vm.justKeyWord = true;
            }


            Regulation.searchInfoByCategoriesAndKeyWords(vm.searchCategoriesDTO, function (data) {
                vm.regulation = data;
                vm.waiting = false;
                vm.noJustOne = false;

            }, onSaveError);
        }

        vm.consultReference = function (article,chapter,reference) {
            vm.regulationReference = {};
            vm.regulationReference.name = vm.regulation.name;
            vm.regulationReference.chapter = chapter;
            vm.regulationReference.article = article;
            vm.regulationReference.reference = reference;
            vm.showReference=true;
            console.log(article)
        }

        vm.consultAll = function () {
            vm.regulation = completeRegulation;
            vm.justCategory = false;
            vm.justKeyWord = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
        $scope.$on('$destroy', unsubscribe);
    }
})();
