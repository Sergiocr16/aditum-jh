'use strict';

describe('Controller Tests', function() {

    describe('CommonAreaReservations Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCommonAreaReservations, MockCommonArea;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCommonAreaReservations = jasmine.createSpy('MockCommonAreaReservations');
            MockCommonArea = jasmine.createSpy('MockCommonArea');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CommonAreaReservations': MockCommonAreaReservations,
                'CommonArea': MockCommonArea
            };
            createController = function() {
                $injector.get('$controller')("CommonAreaReservationsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:commonAreaReservationsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
