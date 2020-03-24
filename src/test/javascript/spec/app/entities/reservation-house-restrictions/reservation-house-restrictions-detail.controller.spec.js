'use strict';

describe('Controller Tests', function() {

    describe('ReservationHouseRestrictions Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReservationHouseRestrictions, MockHouse, MockCommonArea;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReservationHouseRestrictions = jasmine.createSpy('MockReservationHouseRestrictions');
            MockHouse = jasmine.createSpy('MockHouse');
            MockCommonArea = jasmine.createSpy('MockCommonArea');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ReservationHouseRestrictions': MockReservationHouseRestrictions,
                'House': MockHouse,
                'CommonArea': MockCommonArea
            };
            createController = function() {
                $injector.get('$controller')("ReservationHouseRestrictionsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:reservationHouseRestrictionsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
