(function() {
    'use strict';

    angular
        .module('aditumApp')
        .factory('PadronElectoral', PadronElectoral);
    PadronElectoral.$inject = ['firebase'];
    function PadronElectoral (firebase) {
      return {
        find:  function(identificationNumber,callback,error) {
               var pathName = "/padron-electoral/"+identificationNumber;
                firebase.database().ref(pathName).on('value', function(snapshot){
                    var data = snapshot.val()
                    if(data){
                       callback(data)
                    }else{
                      error()
                    }
                })
          }
      }
    }
})();
