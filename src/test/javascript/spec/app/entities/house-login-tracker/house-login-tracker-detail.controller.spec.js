'use strict';

describe('Controller Tests', function() {

    describe('HouseLoginTracker Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHouseLoginTracker, MockHouse, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHouseLoginTracker = jasmine.createSpy('MockHouseLoginTracker');
            MockHouse = jasmine.createSpy('MockHouse');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HouseLoginTracker': MockHouseLoginTracker,
                'House': MockHouse,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("HouseLoginTrackerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:houseLoginTrackerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
