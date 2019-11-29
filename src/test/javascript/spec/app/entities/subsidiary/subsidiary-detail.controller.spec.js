'use strict';

describe('Controller Tests', function() {

    describe('Subsidiary Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSubsidiary, MockSubsidiaryType, MockHouse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSubsidiary = jasmine.createSpy('MockSubsidiary');
            MockSubsidiaryType = jasmine.createSpy('MockSubsidiaryType');
            MockHouse = jasmine.createSpy('MockHouse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Subsidiary': MockSubsidiary,
                'SubsidiaryType': MockSubsidiaryType,
                'House': MockHouse
            };
            createController = function() {
                $injector.get('$controller')("SubsidiaryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:subsidiaryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
