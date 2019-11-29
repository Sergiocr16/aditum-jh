'use strict';

describe('Controller Tests', function() {

    describe('AccessDoor Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAccessDoor, MockCompany, MockWatch;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAccessDoor = jasmine.createSpy('MockAccessDoor');
            MockCompany = jasmine.createSpy('MockCompany');
            MockWatch = jasmine.createSpy('MockWatch');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AccessDoor': MockAccessDoor,
                'Company': MockCompany,
                'Watch': MockWatch
            };
            createController = function() {
                $injector.get('$controller')("AccessDoorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:accessDoorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
