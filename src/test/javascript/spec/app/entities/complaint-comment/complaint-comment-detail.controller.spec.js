'use strict';

describe('Controller Tests', function() {

    describe('ComplaintComment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComplaintComment, MockResident, MockAdminInfo, MockComplaint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComplaintComment = jasmine.createSpy('MockComplaintComment');
            MockResident = jasmine.createSpy('MockResident');
            MockAdminInfo = jasmine.createSpy('MockAdminInfo');
            MockComplaint = jasmine.createSpy('MockComplaint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ComplaintComment': MockComplaintComment,
                'Resident': MockResident,
                'AdminInfo': MockAdminInfo,
                'Complaint': MockComplaint
            };
            createController = function() {
                $injector.get('$controller')("ComplaintCommentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:complaintCommentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
