(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DataProgressController', DataProgressController);

    DataProgressController.$inject = ['$rootScope', '$state', 'Principal', '$timeout', 'Auth', 'MultiCompany', 'House', 'Company'];

    function DataProgressController($rootScope, $state, Principal, $timeout, Auth, MultiCompany, House, Company) {

        var vm = this;
        $rootScope.active = "dataprogress";
        vm.isAuthenticated = Principal.isAuthenticated;

        function loadCompanies() {
            Company.query({}).$promise.then(onSuccessCompanies);
            function onSuccessCompanies(data, headers) {
                vm.companies = data;

                if (data != undefined) {
                    vm.company = data[0]
                    vm.loadHouses(data[0].id)
                }
            }
        }
        setTimeout(function(){
                 Principal.identity().then(function(account){
                 if(account.authorities[0]=="ROLE_ADMIN"){
                      loadCompanies();
                 }else if(  account.authorities[0]=="ROLE_MANAGER"){
                     vm.loadHouses($rootScope.companyId)
                 }
                 })
        },600)
        function initGraphs() {
            var handleAnimatedPieChart = function(id, title, noRedimido, redimido, enProgreso, Listo, color) {
                var chart = AmCharts.makeChart(id, {
                    "type": "pie",
                    "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
                    "baseColor": "",
                    "colors": [
                        "#F3565D",
                        "#4d90d6",
                        "#dfba49",
                        "#45B6AF"
                    ],
                    "outlineThickness": 0,
                    "titleField": "category",
                    "valueField": "column-1",
                    "allLabels": [],
                    "balloon": {},
                    "legend": {
                        "enabled": true,
                        "align": "center",
                        "markerType": "circle"
                    },
                    "titles": [],
                    "dataProvider": [{
                            "category": "Sin redimir",
                            "column-1": noRedimido
                        },
                        {
                            "category": "Redimido",
                            "column-1": redimido
                        },
                        {
                            "category": "En progreso",
                            "column-1": enProgreso
                        },
                        {
                            "category": "Listo",
                            "column-1": listo
                        }
                    ]
                });
            }
            var redimido = 0;
            var sinRedimir = 0;
            var enProgreso = 0;
            var listo = 0
            for (var i = 0; i < vm.houses.length; i++) {
                switch (vm.houses[i].codeStatus) {
                    case 0:
                        sinRedimir++;
                        break;
                    case 1:
                        redimido++;
                        break;
                    case 2:
                        enProgreso++;
                        break;
                    case 3:
                        listo++;
                        break;
                    default:
                }

            }

            handleAnimatedPieChart("codigos-pie-chart", "Códigos de ingreso", sinRedimir, redimido, enProgreso, listo, '#FF8000');
        }
        vm.loadHouses = function(companyId) {
            House.query({
                companyId: companyId
            }).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                angular.forEach(data, function(value, key) {
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                })
                console.log(data)
                vm.houses = data;
               initGraphs()
               setTimeout(function() {
                         $("#loadingIcon").fadeOut(300);
               }, 400)
                setTimeout(function() {
                    $("#tableData").fadeIn('slow');
                },900 )
            }

        }

        function saveTextAsFile(data, filename) {

            if (!data) {
                console.error('Console.save: No data')
                return;
            }

            if (!filename) filename = 'console.json'

            var blob = new Blob([data], {
                    type: 'text/plain'
                }),
                e = document.createEvent('MouseEvents'),
                a = document.createElement('a')
            // FOR IE:

            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                var e = document.createEvent('MouseEvents'),
                    a = document.createElement('a');

                a.download = filename;
                a.href = window.URL.createObjectURL(blob);
                a.dataset.downloadurl = ['text/plain', a.download, a.href].join(':');
                e.initEvent('click', true, false, window,
                    0, 0, 0, 0, 0, false, false, false, false, 0, null);
                a.dispatchEvent(e);
            }
        }
        vm.exportarCodigos = function() {
            var info = "";
            info += "Condominio " + vm.company.name + "  |  ";
            info += "A la Fecha: " + moment(new Date()).format("ll") + "\r\n\r\n";
            info += "Casa            Código de Ingreso\r\n";
            info += "---------------------------------\r\n";
            for (var i = 0; i < vm.houses.length; i++) {
                info += vm.houses[i].housenumber + "                        " + vm.houses[i].loginCode + "\r\n";
            }
            info += "---------------------------------\r\n\r\n";
            info += moment(new Date()).format("YYYY") + " © Lighthouse";
            saveTextAsFile(info, "Códigos de Ingreso, Condominio " + vm.company.name)
        }

        loadCompanies();
    }
})();
