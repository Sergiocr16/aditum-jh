'use strict';

describe('Controller Tests', function() {

    describe('HistoricalPositive Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHistoricalPositive, MockCompany, MockHouse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHistoricalPositive = jasmine.createSpy('MockHistoricalPositive');
            MockCompany = jasmine.createSpy('MockCompany');
            MockHouse = jasmine.createSpy('MockHouse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HistoricalPositive': MockHistoricalPositive,
                'Company': MockCompany,
                'House': MockHouse
            };
            createController = function() {
                $injector.get('$controller')("HistoricalPositiveDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:historicalPositiveUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
