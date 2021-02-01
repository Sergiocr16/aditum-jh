'use strict';

describe('Controller Tests', function() {

    describe('AccountingNote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAccountingNote, MockHouse, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAccountingNote = jasmine.createSpy('MockAccountingNote');
            MockHouse = jasmine.createSpy('MockHouse');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AccountingNote': MockAccountingNote,
                'House': MockHouse,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("AccountingNoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:accountingNoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
