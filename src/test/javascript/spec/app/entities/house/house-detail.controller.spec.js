'use strict';

describe('Controller Tests', function() {

    describe('House Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHouse, MockVehicule, MockVisitant, MockNote, MockResident, MockEmergency, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHouse = jasmine.createSpy('MockHouse');
            MockVehicule = jasmine.createSpy('MockVehicule');
            MockVisitant = jasmine.createSpy('MockVisitant');
            MockNote = jasmine.createSpy('MockNote');
            MockResident = jasmine.createSpy('MockResident');
            MockEmergency = jasmine.createSpy('MockEmergency');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'House': MockHouse,
                'Vehicule': MockVehicule,
                'Visitant': MockVisitant,
                'Note': MockNote,
                'Resident': MockResident,
                'Emergency': MockEmergency,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("HouseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:houseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
