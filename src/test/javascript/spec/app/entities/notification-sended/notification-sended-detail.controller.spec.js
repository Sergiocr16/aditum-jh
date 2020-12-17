'use strict';

describe('Controller Tests', function() {

    describe('NotificationSended Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNotificationSended, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNotificationSended = jasmine.createSpy('MockNotificationSended');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'NotificationSended': MockNotificationSended,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("NotificationSendedDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:notificationSendedUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
