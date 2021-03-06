(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ReservationCalendarController', ReservationCalendarController);

    ReservationCalendarController.$inject = ['globalCompany','CommonMethods','CommonAreaSchedule', '$scope', '$compile', 'uiCalendarConfig', 'entity', 'CommonAreaReservations', 'AlertService', 'Resident', '$state', '$rootScope', 'Modal'];

    function ReservationCalendarController(globalCompany,CommonMethods,CommonAreaSchedule, $scope, $compile, uiCalendarConfig, entity, CommonAreaReservations, AlertService, Resident, $state, $rootScope, Modal) {
        var vm = this;
        vm.commonArea = entity;
        $rootScope.mainTitle = vm.commonArea.name;
        $rootScope.active = "reservationAdministration";
        vm.diasDeLaSemana = ['domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado', ''];
        vm.reservations = [];
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        vm.onDayClick = onDayClick;
        vm.alertOnEventClick = alertOnEventClick
        vm.searchType = 1;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        var data = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        if (data.hasContability == 1) {
            vm.hasContability = true;
        } else {
            vm.hasContability = false;
        }

        CommonAreaSchedule.findSchedulesByCommonArea({
            commonAreaId: vm.commonArea.id
        }, onSuccessSchedule, onErrorSchedule);

        function onSuccessSchedule(data, headers) {
            vm.schedule = [];
            console.log(data)
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

        vm.searchByType = function (type) {
            switch (type) {
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
            {title: 'All Day Event', start: new Date(y, m, 1)},
            {
                title: 'Long Event',
                start: new Date(y, m, d - 5),
                end: new Date(y, m, d - 2),
                description: 'This is a cool eventdfdsafasdfasdf'
            },
            {
                id: 999,
                title: 'Repeating Event33',
                start: new Date(y, m + 1, d - 3, 16, 0),
                allDay: false,
                description: 'This is a cool eventdfdsafasdfasdf'
            },
            {id: 999, title: 'Repeating Event', start: new Date(y, m, d + 4, 16, 0), allDay: false},
            {
                title: 'Birthday Party',
                start: new Date(y, m, d + 1, 19, 0),
                end: new Date(y, m, d + 1, 22, 30),
                allDay: false
            },
            {title: 'Click for Google', start: new Date(y, m, 28), end: new Date(y, m, 29), url: 'http://google.com/'}
        ];

        /* event source that calls a function on every view switch */
        vm.eventsF = function (start, end, timezone, callback) {
            var s = new Date(start).getTime() / 1000;
            var e = new Date(end).getTime() / 1000;
            var m = new Date(start).getMonth();
            var events = [{
                title: 'Feed Me ' + m,
                start: s + (50000),
                end: s + (100000),
                allDay: false,
                className: ['customFeed']
            }];
            callback(events);
        };

        vm.calEventsExt = {
            color: '#f00',
            textColor: 'yellow',
            events: [
                {
                    type: 'party',
                    title: 'Lunch',
                    start: new Date(y, m, d, 12, 0),
                    end: new Date(y, m, d, 14, 0),
                    allDay: false
                },
                {
                    type: 'party',
                    title: 'Lunch 2',
                    start: new Date(y, m, d, 12, 0),
                    end: new Date(y, m, d, 14, 0),
                    allDay: false
                },
                {
                    type: 'party',
                    title: 'Click for Google',
                    start: new Date(y, m, 28),
                    end: new Date(y, m, 29),
                    url: 'http://google.com/'
                }
            ]
        };

        /* alert on eventClick */
        function alertOnEventClick(date, jsEvent, view) {
            if (date.status == 1) {
                $state.go('common-area-administration.reservationDetail', {
                    id: date.id,

                });
            } else if (date.status == 2) {
                $state.go('common-area-administration.acceptedReservationsDetail', {
                    id: date.id,

                });
            }

            // $state.go("({id:commonAreaReservations.id})"
            vm.alertMessage = (date.id + ' was clicked ');
        };
        /* alert on Drop */
        vm.alertOnDrop = function (event, delta, revertFunc, jsEvent, ui, view) {
            vm.alertMessage = ('Event Droped to make dayDelta ' + delta);
        };
        /* alert on Resize */
        vm.alertOnResize = function (event, delta, revertFunc, jsEvent, ui, view) {
            vm.alertMessage = ('Event Resized to make dayDelta ' + delta);
        };
        /* add and removes an event source of choice */
        vm.addRemoveEventSource = function (sources, source) {
            var canAdd = 0;
            angular.forEach(sources, function (value, key) {
                if (sources[key] === source) {
                    sources.splice(key, 1);
                    canAdd = 1;
                }
            });
            if (canAdd === 0) {
                sources.push(source);
            }
        };
        /* add custom event*/
        vm.addEvent = function () {
            vm.events.push({
                title: 'Open Sesame',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                className: ['openSesame']
            });
        };
        /* Change View */
        vm.changeView = function (viewMode) {
            vm.uiConfig.calendar.defaultView = viewMode;
            console.log(vm.uiConfig.calendar.defaultView);
        };

        /* Change View */
        vm.renderCalender = function (calendar) {
            console.log('adfad')
            if (uiCalendarConfig.calendars[calendar]) {
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
        vm.eventRender = function (event, element, view) {
            element.attr({
                'tooltip': event.title,
                'tooltip-append-to-body': true
            });
            $compile(element)(vm);
        };

        /* config object */
        vm.uiConfig = {
            calendar: {
                events: function (start, end, timezone, callback) {
                    var events = [];
                    CommonAreaReservations.getReservationsByCommonArea({
                        commonAreaId: vm.commonArea.id
                    }, function (data) {
                        console.log(data)
                        angular.forEach(data, function (value) {

                            var color;
                            if (value.status == 1) {
                                color = '#ef5350'
                            } else if (value.status == 2) {
                                color = '#42a5f5'
                            }
                            events.push({
                                id: value.id,

                                title: value.resident.name + " " + value.resident.lastname + " - Filial " + value.house.housenumber,

                                start: new Date(value.initalDate),

                                end: new Date(value.finalDate),
                                description: 'This is a cool ',
                                color: color,
                                status: value.status

                            })

                        });

                        callback(events);
                    });


                },
                height: 1000,
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
                defaultView: 'month',
                default: 'bootstrap3'
            },
            calendar1: {
                events: function (start, end, timezone, callback) {
                    var events = [];
                    CommonAreaReservations.getReservationsByCommonArea({
                        commonAreaId: vm.commonArea.id
                    }, function (data) {
                        console.log(data)
                        angular.forEach(data, function (value) {

                            var color;
                            if (value.status == 1) {
                                color = '#ef5350'
                            } else if (value.status == 2) {
                                color = '#42a5f5'
                            }
                            events.push({
                                id: value.id,

                                title: value.resident.name + " " + value.resident.lastname + " - Filial " + value.house.housenumber,

                                start: new Date(value.initalDate),

                                end: new Date(value.finalDate),
                                description: 'This is a cool eventdfdsafasdfasdf',
                                color: color,
                                status: value.status

                            })

                        });

                        callback(events);
                    });


                },
                height: 700,
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
            console.log(dateSelected);
            Modal.confirmDialog("¿Desea realizar una reservación el día " + date.format("DD-MM-YYYY") + "?", " ",
                function () {
                    if (today.getTime() > dateSelected.getTime()) {
                        Modal.toast("No puede realizar reservaciones en una fecha pasada")
                    } else {
                        if (isTheDayInSchedule(dateSelected)) {
                            $state.go('common-area-administration.newCommonAreaReservation', {
                                date: dateSelected, commonAreaId: vm.commonArea.id
                            })
                        } else {
                            Modal.toast("No se permite reservar el día " + vm.diasDeLaSemana[dateSelected.getDay()] + " en esta área común")
                        }

                    }


                });


        };


        vm.createReservationWithOutDate = function () {
            $state.go('common-area-administration.newCommonAreaReservation', {
                date: 0, commonAreaId: vm.commonArea.id
            })
        };

        function isTheDayInSchedule(day) {
            console.log(vm.schedule)
            console.log(day.getDay())
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

        function onDayClick(date, jsEvent, view) {
            vm.confirmMessage(date);

            console.log("clicked:" + date.calendar());
            console.log("view: " + view.name)
            console.log("event target: " + jsEvent.target);
        }

        /* event sources array*/
        // vm.eventSources = [vm.events];
        // vm.eventSources2 = [vm.calEventsExt, vm.eventsF, vm.events];

    }
})();
