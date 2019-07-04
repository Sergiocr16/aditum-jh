'use strict';

describe('Controller Tests', function() {

    describe('MacroOfficerAccount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMacroOfficerAccount, MockUser, MockMacroCondominium;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMacroOfficerAccount = jasmine.createSpy('MockMacroOfficerAccount');
            MockUser = jasmine.createSpy('MockUser');
            MockMacroCondominium = jasmine.createSpy('MockMacroCondominium');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MacroOfficerAccount': MockMacroOfficerAccount,
                'User': MockUser,
                'MacroCondominium': MockMacroCondominium
            };
            createController = function() {
                $injector.get('$controller')("MacroOfficerAccountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:macroOfficerAccountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
