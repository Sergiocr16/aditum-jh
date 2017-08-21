(function() {
	'use strict';

	angular
	.module('aditumApp')
	.controller('DashboardController', DashboardController);

	DashboardController.$inject = ['$scope','$rootScope', 'Principal', 'LoginService', '$state','Dashboard'];

	function DashboardController ($scope,$rootScope, Principal, LoginService, $state, Dashboard) {
		var vm = this;
		 $rootScope.active = "dashboard";
		vm.isInLogin = $state.includes('home');
		vm.account = null;
		vm.login = LoginService.open;

		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
			});
		}

		getAccount();
		vm.loadAll = function() {
			Dashboard.query({companyId : $rootScope.companyId},function(result) {
				vm.dashboard = result;
				showData();
				vm.visitorTitle = "Visitantes de la semana";
				vm.watch = formatWatch(vm.dashboard.currentWatch);
				if(vm.dashboard.currentWatch!==null){
				vm.officerPercentage = ((vm.dashboard.currentWatch.officers.length * 100)/vm.dashboard.officerQuantity)
				}else{
				 vm.officerPercentage = 0;
				}
				vm.housesPercentage = ((vm.dashboard.houseQuantity * 100)/vm.dashboard.totalHouses).toFixed(2)
			});
		}

	  setTimeout(function(){ vm.loadAll();},600)

		function formatResponsableOfficer(stringOfficer) {
			var variables = stringOfficer.split(';')
			var officer = {};
			officer.id = variables[0];
			officer.identificationnumber = variables[1];
			officer.name = variables[2];
			return officer;
		}
		function getformatResponsableOfficers(watch) {
			var formattedOfficers = [];
			var stringOfficers = watch.responsableofficer.slice(0, -2);
			var officers = stringOfficers.split('||');
			angular.forEach(officers, function(officer, key) {
				formattedOfficers.push(formatResponsableOfficer(officer))
			})
			return formattedOfficers;
		}
		function formatWatch(watch) {
		if(watch!=null){
			watch.initialtime = moment(watch.initialtime).format('h:mm a');
			if (watch.finaltime === null) {
				watch.finaltime = 'Aún en progreso'
			} else {
				watch.finaltime = moment(watch.finaltime).format('h:mm a');
			}
			watch.officers = getformatResponsableOfficers(watch);
			}
			return watch;
		}
		function showData(){
			angular.element(document).ready(function () {
				$('#all').fadeIn("300");
				initGraphs();
			});

		}

		function initGraphs(){

			var handleAnimatedPieChart = function(id,title,hab,desa,color) {
				var chart = AmCharts.makeChart( id, {
					"type": "pie",
					"balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
					"titleField": "category",
					"colorField":"color",
					"innerRadius": "20%",
					"baseColor": color,
					"titles": [{
						"text": title
					}],
					"valueField": "column-1",
					"labelRadius": 0,
					"allLabels": [],
					"balloon": {},
					"dataProvider": [
						{
							"category": "Habilitados",
							"column-1": hab
						},
						{
							"category": "Deshabilitados",
							"column-1": desa
						},
					]
				} );
			}
			handleAnimatedPieChart("resident-pie-chart","Residentes",vm.dashboard.enableResidentQuantity,vm.dashboard.disableResidentQuantity,'#FF8000');
			handleAnimatedPieChart("vehicle-pie-chart","Vehículos",vm.dashboard.enableVehicleuQantity,vm.dashboard.disableVehicleQuantity,'#008000');
			$('.easy-pie-chart .number.transactions').easyPieChart({
				animate: 1000,
				size: 75,
				lineWidth: 3,
				barColor: Metronic.getBrandColor('red')
			});

			$('.easy-pie-chart .number.visits').easyPieChart({
				animate: 1000,
				size: 75,
				lineWidth: 3,
				barColor: Metronic.getBrandColor('red'),
				data:10
			});

			$('.easy-pie-chart .number.bounce').easyPieChart({
				animate: 1000,
				size: 75,
				lineWidth: 3,
				barColor: Metronic.getBrandColor('yellow')
			});


			vm.showVisitorGraph = function() {
				if ($('#site_activities').length != 0) {
					//site activities
					var previousPoint2 = null;
					$('#site_activities_loading').hide();
					$('#site_activities_content').show();
					vm.dataVisitor = vm.dashboard.visitorsPerMonth;
					var plot_statistics = $.plot($("#site_activities"),
					[{
						data:  vm.dataVisitor,
						lines: {
							fill: 0.2,
							lineWidth: 0,
						},
						color: ['#BAD9F5']
					}, {
						data:  vm.dataVisitor,
						points: {
							show: true,
							fill: true,
							radius: 4,
							fillColor: "#9ACAE6",
							lineWidth: 2
						},
						color: '#9ACAE6',
						shadowSize: 1
					}, {
						data:  vm.dataVisitor,
						lines: {
							show: true,
							fill: false,
							lineWidth: 3
						},
						color: '#9ACAE6',
						shadowSize: 0
					}],

					{

						xaxis: {
							tickLength: 0,
							tickDecimals: 0,
							mode: "categories",
							min: 0,
							font: {
								lineHeight: 18,
								style: "normal",
								variant: "small-caps",
								color: "#6F7B8A"
							}
						},
						yaxis: {
							ticks: 5,
							tickDecimals: 0,
							tickColor: "#eee",
							font: {
								lineHeight: 14,
								style: "normal",
								variant: "small-caps",
								color: "#6F7B8A"
							}
						},
						grid: {
							hoverable: true,
							clickable: true,
							tickColor: "#eee",
							borderColor: "#eee",
							borderWidth: 1
						}
					});
					function showChartTooltip(x, y, xValue, yValue) {
						$('<div id="tooltip" class="chart-tooltip">' + yValue + '<\/div>').css({
							position: 'absolute',
							display: 'none',
							top: y - 40,
							left: x - 40,
							border: '0px solid #ccc',
							padding: '2px 6px',
							'background-color': '#fff'
						}).appendTo("body").fadeIn(200);
					}
					$("#site_activities").bind("plothover", function (event, pos, item) {
						$("#x").text(pos.x.toFixed(2));
						$("#y").text(pos.y.toFixed(2));
						if (item) {
							if (previousPoint2 != item.dataIndex) {
								previousPoint2 = item.dataIndex;
								$("#tooltip").remove();
								var x = item.datapoint[0].toFixed(2),
								y = item.datapoint[1].toFixed(2);
								showChartTooltip(item.pageX, item.pageY, item.datapoint[0], item.datapoint[1] + ' visitantes');
							}
						}
					});

					$('#site_activities').bind("mouseleave", function () {
						$("#tooltip").remove();
					});
				}
			}
			vm.showVisitorGraph();
		}


		vm.updateMonthData = function(){
			Dashboard.updateMonth({companyId : $rootScope.companyId},function(result) {
				vm.dashboard.visitorsPerMonth = result;
vm.visitorTitle = "Visitantes del mes";
				$('#site_activities').html('')
				vm.showVisitorGraph();
				initGraphs();
			});
		}
		vm.updateYearData = function(){
			Dashboard.updateYear({companyId : $rootScope.companyId},function(result) {
				vm.dashboard.visitorsPerMonth = result;
				vm.visitorTitle = "Visitantes del año";
				$('#site_activities').html('')
				vm.showVisitorGraph();
				initGraphs();
			});
		}

	}
})();
