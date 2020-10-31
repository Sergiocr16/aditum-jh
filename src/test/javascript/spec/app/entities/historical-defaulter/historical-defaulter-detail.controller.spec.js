'use strict';

describe('Controller Tests', function() {

    describe('HistoricalDefaulter Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHistoricalDefaulter, MockCompany, MockHouse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHistoricalDefaulter = jasmine.createSpy('MockHistoricalDefaulter');
            MockCompany = jasmine.createSpy('MockCompany');
            MockHouse = jasmine.createSpy('MockHouse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HistoricalDefaulter': MockHistoricalDefaulter,
                'Company': MockCompany,
                'House': MockHouse
            };
            createController = function() {
                $injector.get('$controller')("HistoricalDefaulterDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:historicalDefaulterUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
