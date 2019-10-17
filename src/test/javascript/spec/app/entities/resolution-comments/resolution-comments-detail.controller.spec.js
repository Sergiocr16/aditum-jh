'use strict';

describe('Controller Tests', function() {

    describe('ResolutionComments Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockResolutionComments, MockAdminInfo, MockResolution;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockResolutionComments = jasmine.createSpy('MockResolutionComments');
            MockAdminInfo = jasmine.createSpy('MockAdminInfo');
            MockResolution = jasmine.createSpy('MockResolution');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ResolutionComments': MockResolutionComments,
                'AdminInfo': MockAdminInfo,
                'Resolution': MockResolution
            };
            createController = function() {
                $injector.get('$controller')("ResolutionCommentsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:resolutionCommentsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
