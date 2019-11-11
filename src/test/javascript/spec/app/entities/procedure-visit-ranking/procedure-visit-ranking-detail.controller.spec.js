'use strict';

describe('Controller Tests', function() {

    describe('ProcedureVisitRanking Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProcedureVisitRanking, MockProcedureVisits;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProcedureVisitRanking = jasmine.createSpy('MockProcedureVisitRanking');
            MockProcedureVisits = jasmine.createSpy('MockProcedureVisits');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProcedureVisitRanking': MockProcedureVisitRanking,
                'ProcedureVisits': MockProcedureVisits
            };
            createController = function() {
                $injector.get('$controller')("ProcedureVisitRankingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:procedureVisitRankingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
