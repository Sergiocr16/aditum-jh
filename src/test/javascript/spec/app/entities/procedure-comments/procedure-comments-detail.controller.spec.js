'use strict';

describe('Controller Tests', function() {

    describe('ProcedureComments Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProcedureComments, MockProcedures, MockAdminInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProcedureComments = jasmine.createSpy('MockProcedureComments');
            MockProcedures = jasmine.createSpy('MockProcedures');
            MockAdminInfo = jasmine.createSpy('MockAdminInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProcedureComments': MockProcedureComments,
                'Procedures': MockProcedures,
                'AdminInfo': MockAdminInfo
            };
            createController = function() {
                $injector.get('$controller')("ProcedureCommentsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:procedureCommentsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
