'use strict';

describe('Controller Tests', function() {

    describe('RevisionConfigTask Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRevisionConfigTask, MockRevisionTaskCategory, MockRevisionConfig;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRevisionConfigTask = jasmine.createSpy('MockRevisionConfigTask');
            MockRevisionTaskCategory = jasmine.createSpy('MockRevisionTaskCategory');
            MockRevisionConfig = jasmine.createSpy('MockRevisionConfig');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'RevisionConfigTask': MockRevisionConfigTask,
                'RevisionTaskCategory': MockRevisionTaskCategory,
                'RevisionConfig': MockRevisionConfig
            };
            createController = function() {
                $injector.get('$controller')("RevisionConfigTaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:revisionConfigTaskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
