'use strict';

describe('Controller Tests', function() {

    describe('SubsidiaryType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSubsidiaryType, MockSubsidiaryCategory, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSubsidiaryType = jasmine.createSpy('MockSubsidiaryType');
            MockSubsidiaryCategory = jasmine.createSpy('MockSubsidiaryCategory');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SubsidiaryType': MockSubsidiaryType,
                'SubsidiaryCategory': MockSubsidiaryCategory,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("SubsidiaryTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:subsidiaryTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
