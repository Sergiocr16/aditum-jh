'use strict';

describe('Controller Tests', function() {

    describe('ActivosFijos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockActivosFijos, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockActivosFijos = jasmine.createSpy('MockActivosFijos');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ActivosFijos': MockActivosFijos,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("ActivosFijosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:activosFijosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
