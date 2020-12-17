'use strict';

describe('Controller Tests', function() {

    describe('HistoricalDefaulterCharge Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHistoricalDefaulterCharge, MockHistoricalDefaulter;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHistoricalDefaulterCharge = jasmine.createSpy('MockHistoricalDefaulterCharge');
            MockHistoricalDefaulter = jasmine.createSpy('MockHistoricalDefaulter');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HistoricalDefaulterCharge': MockHistoricalDefaulterCharge,
                'HistoricalDefaulter': MockHistoricalDefaulter
            };
            createController = function() {
                $injector.get('$controller')("HistoricalDefaulterChargeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:historicalDefaulterChargeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
