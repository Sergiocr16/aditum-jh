'use strict';

describe('Controller Tests', function() {

    describe('MacroVisit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMacroVisit, MockMacroCondominium, MockCompany, MockHouse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMacroVisit = jasmine.createSpy('MockMacroVisit');
            MockMacroCondominium = jasmine.createSpy('MockMacroCondominium');
            MockCompany = jasmine.createSpy('MockCompany');
            MockHouse = jasmine.createSpy('MockHouse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MacroVisit': MockMacroVisit,
                'MacroCondominium': MockMacroCondominium,
                'Company': MockCompany,
                'House': MockHouse
            };
            createController = function() {
                $injector.get('$controller')("MacroVisitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:macroVisitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
