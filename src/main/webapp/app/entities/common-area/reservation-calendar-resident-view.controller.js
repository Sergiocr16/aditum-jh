(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationCalendarResidentViewController', ReservationCalendarResidentViewController);

    ReservationCalendarResidentViewController.$inject = ['ParseLinks','$scope','CommonAreaSchedule','$compile','uiCalendarConfig','entity','CommonAreaReservations','AlertService','Resident','$state','$rootScope','Modal'];

    function ReservationCalendarResidentViewController(ParseLinks,$scope,CommonAreaSchedule,$compile,uiCalendarConfig,entity,CommonAreaReservations,AlertService,Resident,$state,$rootScope,Modal) {
        var vm = this;
        vm.commonArea = entity;
        $rootScope.mainTitle = vm.commonArea.name;
        $rootScope.active = "reservationCalendarResidentView";
        vm.reservations = [];
        vm.events = [];
        vm.eventSources = [[]];
        vm.diasDeLaSemana = ['domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado', ''];
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        vm.onDayClick = onDayClick;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        CommonAreaSchedule.findSchedulesByCommonArea({
            commonAreaId: vm.commonArea.id
        }, onSuccessSchedule, onErrorSchedule);
        vm.loadPage = function (page, initialDate, finalDate) {
            vm.page = page;
            CommonAreaReservations.getPendingAndAcceptedReservationsBetweenDatesAndArea({
                areaId: vm.commonArea.id,
                initial_time: vm.initialDate,
                final_time: vm.finalDate,
                page: vm.page,
                size: 20,
            }, successReservations);
        }
        function onSuccessSchedule(data, headers) {
            vm.schedule = [];
            if (data[0].lunes !== "-") {
                vm.lunes = true;
                vm.schedule.push(1)
            }
            if (data[0].martes !== "-") {
                vm.martes = true;
                vm.schedule.push(2)
            }
            if (data[0].miercoles !== "-") {
                vm.miercoles = true;
                vm.schedule.push(3)
            }
            if (data[0].jueves !== "-") {
                vm.jueves = true;
                vm.schedule.push(4)
            }
            if (data[0].viernes !== "-") {
                vm.viernes = true;
                vm.schedule.push(5)
            }
            if (data[0].sabado !== "-") {
                vm.sabado = true;
                vm.schedule.push(6)
            }
            if (data[0].domingo !== "-") {
                vm.domingo = true;
                vm.schedule.push(0)
            }
        }

        function onErrorSchedule(error) {
            AlertService.error(error.data.message);

        }

        vm.searchType = 1;
        vm.searchByType = function(type){
            switch(type){
                case 1:
                    vm.changeView('month')

                    vm.searchType = 1;
                    break;
                case 2:
                    vm.changeView('agendaWeek')
                    vm.searchType = 2;
                    break;
                case 3:
                    vm.changeView('agendaDay')
                    vm.searchType = 3;
                    break;
                case 4:
                    vm.changeView('listWeek')
                    vm.searchType = 4;
                    break;

            }

        };

        vm.eventSource = {
            url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",
            className: 'gcal-event',           // an option!
            currentTimezone: 'America/Chicago' // an option!
        };

        /* event source that contains custom events on the scope */
        vm.events = [
            {title: 'All Day Event',start: new Date(y, m, 1)},
            {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2), description: 'This is a cool eventdfdsafasdfasdf'},
            {id: 999,title: 'Repeating Event33',start: new Date(y, m+1, d - 3, 16, 0),allDay: false, description: 'This is a cool eventdfdsafasdfasdf'},
            {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
            {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
            {title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
        ];

        /* event source that calls a function on every view switch */
        vm.eventsF = function (start, end, timezone, callback) {
            var s = new Date(start).getTime() / 1000;
            var e = new Date(end).getTime() / 1000;
            var m = new Date(start).getMonth();
            var events = [{title: 'Feed Me ' + m,start: s + (50000),end: s + (100000),allDay: false, className: ['customFeed']}];
            callback(events);
        };

        vm.calEventsExt = {
            color: '#f00',
            textColor: 'yellow',
            events: [
                {type:'party',title: 'Lunch',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0), allDay: false},
                {type:'party',title: 'Lunch 2',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
                {type:'party',title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
            ]
        };


        /* alert on Drop */
        vm.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view){
            vm.alertMessage = ('Event Droped to make dayDelta ' + delta);
        };
        /* alert on Resize */
        vm.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view ){
            vm.alertMessage = ('Event Resized to make dayDelta ' + delta);
        };
        /* add and removes an event source of choice */
        vm.addRemoveEventSource = function(sources,source) {
            var canAdd = 0;
            angular.forEach(sources,function(value, key){
                if(sources[key] === source){
                    sources.splice(key,1);
                    canAdd = 1;
                }
            });
            if(canAdd === 0){
                sources.push(source);
            }
        };
        /* add custom event*/
        vm.addEvent = function() {
            vm.events.push({
                title: 'Open Sesame',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                className: ['openSesame']
            });
        };
        /* Change View */
        vm.changeView = function(viewMode) {
            vm.uiConfig.calendar.defaultView = viewMode;
        };

        /* Change View */
        vm.renderCalender = function(calendar) {
            if(uiCalendarConfig.calendars[calendar]){
                uiCalendarConfig.calendars[calendar].fullCalendar('render');
            }
        };
        vm.formatearNumero = function (nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        };
        /* Render Tooltip */
        vm.eventRender = function( event, element, view ) {
            element.attr({'tooltip': event.title,
                'tooltip-append-to-body': true});
            $compile(element)(vm);
        };
        vm.loadAll = function (initialDate, finalDate) {
            Modal.showLoadingBar();
            vm.eventSources[0] = [];
            vm.eventsAll = [];
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.initialDate = initialDate.format() + "T00:00:00-06:00"
            vm.finalDate = finalDate.format() + "T23:59:59-06:00"
            CommonAreaReservations.getPendingAndAcceptedReservationsBetweenDatesAndArea({
                areaId: vm.commonArea.id,
                initial_time: vm.initialDate,
                final_time: vm.finalDate,
                page: vm.page,
                size: 20,
            }, successReservations);
        }

        function successReservations(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            angular.forEach(data, function (value) {
                var color;
                var message
                if(value.status==1){
                    color = '#ef5350';
                    message = "Pendiente de aprobar"
                }else if(value.status==2){
                    color = '#42a5f5'
                    message = "No disponible"
                }
                vm.eventsAll.push({
                    id: value.id,
                    title:message,
                    commonAreaId: value.commonAreaId,
                    start: new Date(value.initalDate),
                    end: new Date(value.finalDate),
                    color: color,
                    status: value.status
                })
            });
            if (vm.page <= vm.links['last']) {
                vm.loadPage(vm.page + 1, vm.initialDate, vm.finalDate)
            }else{
                Modal.hideLoadingBar();
                vm.eventSources[0] = vm.eventsAll;
            }
        }

        /* config object */
        /* config object */
        vm.uiConfig = {
            calendar: {
                events: [],
                height: 1000,
                viewRender: function (view, element) {
                    vm.loadAll(view.start, view.end)
                },
                dayClick: vm.onDayClick,
                editable: false,
                header: {
                    left: '',
                    center: 'title',
                    right: ' prev,next'
                },
                eventClick: vm.alertOnEventClick,
                eventDrop: vm.alertOnDrop,
                eventResize: vm.alertOnResize,
                eventRender: vm.eventRender,
                defaultView: 'agendaWeek',
                default: 'bootstrap3'
            },
            calendar1: {
                events: [],
                height: 700,
                viewRender: function (view, element) {
                    vm.loadAll(view.start, view.end)
                },
                dayClick: vm.onDayClick,
                editable: false,
                header: {
                    left: 'title',
                    center: '',
                    right: ' prev,next'
                },
                eventClick: vm.alertOnEventClick,
                eventDrop: vm.alertOnDrop,
                eventResize: vm.alertOnResize,
                eventRender: vm.eventRender,
                defaultView: 'listWeek',
                default: 'bootstrap3'
            }
        };

        vm.confirmMessage = function (date) {
            var today = new Date();
            var datePlus1 = moment(date, "DD-MM-YYYY").add(1, 'days');
            var dateSelected = datePlus1.toDate();
            dateSelected.setHours(23);
            dateSelected.setMinutes(59);
            Modal.confirmDialog("¿Desea realizar una reservación el día " + date.format("DD-MM-YYYY") + "?", " ",
                function () {
                    if (today.getTime() > dateSelected.getTime()) {
                        Modal.toast("No puede realizar reservaciones en una fecha pasada")
                    } else {
                        if (isTheDayInSchedule(dateSelected)) {
                            $state.go('common-area-new-reservation-resident-view-date', {
                               id: vm.commonArea.id, date: dateSelected
                            })
                        } else {
                            Modal.toast("No se permite reservar el día " + vm.diasDeLaSemana[dateSelected.getDay()] + " en esta área común")
                        }
                    }
                });
        };

        function isTheDayInSchedule(day) {
            var isContained = false;

            angular.forEach(vm.schedule, function (item, key) {

                if (item == day.getDay()) {
                    isContained = true;
                }
            });
            if (isContained) {
                return true;
            } else {
                return false;
            }

        }

        function onDayClick(date , jsEvent , view){
            vm.confirmMessage(date);
        }

        /* event sources array*/
        // vm.eventSources = [vm.events];
        // vm.eventSources2 = [vm.calEventsExt, vm.eventsF, vm.events];

    }
})();
