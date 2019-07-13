'use strict';

describe('Controller Tests', function() {

    describe('MacroAdminAccount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMacroAdminAccount, MockMacroCondominium, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMacroAdminAccount = jasmine.createSpy('MockMacroAdminAccount');
            MockMacroCondominium = jasmine.createSpy('MockMacroCondominium');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MacroAdminAccount': MockMacroAdminAccount,
                'MacroCondominium': MockMacroCondominium,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("MacroAdminAccountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:macroAdminAccountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
