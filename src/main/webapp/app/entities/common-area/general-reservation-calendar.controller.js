(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('GeneralReservationCalendarController', GeneralReservationCalendarController);

    GeneralReservationCalendarController.$inject = ['$compile','uiCalendarConfig','CommonAreaReservations','AlertService','Resident','$state','$rootScope'];

    function GeneralReservationCalendarController($compile,uiCalendarConfig,CommonAreaReservations,AlertService,Resident,$state,$rootScope) {
        var vm = this;
        $rootScope.active = "generaCalendar";
        vm.reservations = [];
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        vm.onDayClick = onDayClick;
        vm.alertOnEventClick = alertOnEventClick
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

        /* alert on eventClick */
        function alertOnEventClick( date, jsEvent, view){
            console.log (date.status + ' was clicked ');
            if(date.status==1){
                console.log('adfadf')
                $state.go('common-area-administration.general-reservation-calendar.reservationDetail', {
                    id: date.id
                });
            }else if(date.status==2){
                console.log('adf22adf')
                $state.go('common-area-administration.general-reservation-calendar.acceptedReservationsDetail', {
                    id: date.id
                });
            }

            // $state.go("({id:commonAreaReservations.id})"
            vm.alertMessage = (date.id + ' was clicked ');
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
            console.log(vm.uiConfig.calendar.defaultView);
        };

        /* Change View */
        vm.renderCalender = function(calendar) {
            console.log('adfad')
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

        /* config object */
        vm.uiConfig = {
            calendar:{
                events: function(start, end, timezone, callback) {
                    var events = [];
                        CommonAreaReservations.getPendingAndAcceptedReservations({
                            companyId: $rootScope.companyId
                        }, function(data) {
                            console.log(data)
                            angular.forEach(data,function(value){

                                var color;
                                if(value.status==1){
                                    color = '#ef5350'
                                }else if(value.status==2){
                                    color = '#42a5f5'
                                }
                                events.push({
                                    id:value.id,
                                    commonAreaId:value.commonAreaId,
                                    title: value.commonArea.name + " - " + value.resident.name + " " + value.resident.lastname + " - Filial " + value.house.housenumber  ,

                                    start:new Date(value.initalDate),

                                    end:new Date(value.finalDate),
                                    description: 'This is a cool eventdfdsafasdfasdf',
                                    color:color,
                                    status:value.status

                                })

                            });

                            callback(events);

                        });


                },
                height: 1000,
                dayClick: vm.onDayClick,
                editable: false,
                header:{
                    left: '',
                    center: 'title',
                    right: 'today prev,next'
                },

                eventClick: vm.alertOnEventClick,
                eventDrop: vm.alertOnDrop,
                eventResize: vm.alertOnResize,
                eventRender: vm.eventRender,
                defaultView: 'month',
                default: 'bootstrap3'
            }
        };

        vm.confirmMessage = function(date) {
            var dateSelected = new Date(date);
            bootbox.confirm({
                message: '<div class="text-center gray-font font-15"><h4 style="margin-bottom:10px; font-size: 17px;">¿Desea realizar una reservación el día <span class="" id="dateSelected"></span>?</h4></div>',
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {

                    if (result) {
                        createReservation()

                    } else {
                        vm.isSaving = false;

                    }
                }
            });
            document.getElementById("dateSelected").innerHTML = dateSelected.getDate() + "-" + dateSelected.getMonth()+"-"+dateSelected.getFullYear();



        };
        function onDayClick(date , jsEvent , view){
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
