(function() {
    'use strict';

    angular
        .module('aditumApp')
        .provider('CommonMethods', CommonMethods);
    function CommonMethods () {
     this.$get = getService;

        function getService () {
            return {
               validateName: validateName,
                waitingMessage: waitingMessage,
                validateLetters: validateLetters,
                validateNumbers: validateNumbers,
                validateRepeat: validateRepeat,
                capitalizeFirstLetter: capitalizeFirstLetter
            };
            function validateName (items, name) {
                var condition = true;
                   angular.forEach(items, function(item, index) {
                       if (item.name.toUpperCase() == name.toUpperCase()) {
                           condition = false;
                       }
                   });
                   return condition;
            }
           function waitingMessage(message) {
                 bootbox.dialog({
                         message: '<div class="text-center gray-font font-15"><img src="../../content/images/waiting-gif1.gif" style="width: 20px; height: 20px;"/>  Por favor espere...</div>',
                         closeButton: false,
                     })
             }
            function validateNumbers() {
                jQuery('.numbers').keypress(function(tecla) {
                    if (tecla.charCode < 48 || tecla.charCode > 57) return false;
                });
            }
             function validateRepeat(items, itemToValidate, criteria) {
                 var condition = false;
                 angular.forEach(items, function(item, index) {
                     switch (criteria) {
                         case 1:
                             if (item.identification_number == itemToValidate) {
                                 condition = true;
                             }
                             break;
                         case 2:
                             if (item.email.toUpperCase() == itemToValidate.toUpperCase()) {
                                 condition = true;
                             }
                             break;
                         case 3:
                             if (item.house_number == itemToValidate) {
                                 condition = true;
                             }
                             break;
                         case 4:
                             if (item.license_plate == itemToValidate) {
                                 condition = true;
                             }
                             break;
                         case 5:
                             if (item.email.toUpperCase() == itemToValidate.toUpperCase()) {
                                 condition = true;
                             }
                             break;
                     }


                 });
                 return condition;
             }
            function validateLetters() {
                 $(".letters").keypress(function(key) {
                     if ((key.charCode < 97 || key.charCode > 122) //letras mayusculas
                         &&
                         (key.charCode < 65 || key.charCode > 90) //letras minusculas
                         &&
                         (key.charCode != 45) //retroceso
                         &&
                         (key.charCode != 241) //ñ
                         &&
                         (key.charCode != 209) //Ñ
                         &&
                         (key.charCode != 32) //espacio
                         &&
                         (key.charCode != 225) //á
                         &&
                         (key.charCode != 233) //é
                         &&
                         (key.charCode != 237) //í
                         &&
                         (key.charCode != 243) //ó
                         &&
                         (key.charCode != 250) //ú
                         &&
                         (key.charCode != 193) //Á
                         &&
                         (key.charCode != 201) //É
                         &&
                         (key.charCode != 205) //Í
                         &&
                         (key.charCode != 211) //Ó
                         &&
                         (key.charCode != 218) //Ú

                     )
                         return false;
                 });
            }
             function capitalizeFirstLetter(string) {
                     return string.charAt(0).toUpperCase() + string.slice(1);
             }
             function validateRepeat(items, itemToValidate, criteria) {
                 var condition = false;
                 angular.forEach(items, function(item, index) {
                      console.log(criteria);
                     switch (criteria) {
                         case 1:
                             if (item.identificationnumber == itemToValidate) {
                                 condition = true;
                             }
                             break;
                         case 2:
                             if (item.email.toUpperCase() == itemToValidate.toUpperCase()) {
                                 condition = true;
                             }
                             break;
                         case 3:
                             if (item.housenumber == itemToValidate) {
                                 condition = true;
                             }
                             break;
                         case 4:
                             if (item.licenseplate == itemToValidate) {
                                 condition = true;
                             }
                             break;
                         case 5:
                             if (item.email.toUpperCase() == itemToValidate.toUpperCase()) {
                                 condition = true;
                             }
                             break;
                     }


                 });
                 return condition;
             }

      }

    }


//
//
//
//        .factory('commonMethods', function($rootScope, $state) {
//
//         return {
//
//             validateName: function(items, name) {
//                 var condition = true;
//                 angular.forEach(items, function(item, index) {
//                     if (item.name.toUpperCase() == name.toUpperCase()) {
//                         condition = false;
//                     }
//                 });
//                 return condition;
//             },
//             waitingMessage: function(message) {
//                 bootbox.dialog({
//                         message: '<div class="text-center gray-font font-15"><img src="../../assets/global/img/4.gif" style="width: 20px; height: 20px;"/>  Por favor espere...</div>',
//                         closeButton: false,
//                     })
//                     // var box = bootbox.dialog({
//                     //     message: '<div class="text-center gray-font font-15"><img src="../../assets/global/img/loading-circle.gif" style="width:40%; height 40%;" /></div>'
//                     //
//                     // })
//                     // box.find('.modal-content').css({
//                     //     'background': 'rgba(0, 0, 0, 0.0)',
//                     //     'border': '0px solid',
//                     //     'box-shadow': '0px 0px 0px #999'
//                     // });
//             },

//             validateSpecialCharacters: function() {
//                 jQuery('.specialCharacters').keypress(function(tecla) {
//                     if ((tecla.charCode < 48) || (tecla.charCode > 90 && tecla.charCode < 97) || (tecla.charCode > 122 && tecla.charCode < 126) || (tecla.charCode > 57 && tecla.charCode < 65)) return false;
//                 });
//             },
//
//             validateRepeat: function(items, itemToValidate, criteria) {
//                 var condition = false;
//                 angular.forEach(items, function(item, index) {
//                     switch (criteria) {
//                         case 1:
//                             if (item.identification_number == itemToValidate) {
//                                 condition = true;
//                             }
//                             break;
//                         case 2:
//                             if (item.email.toUpperCase() == itemToValidate.toUpperCase()) {
//                                 condition = true;
//                             }
//                             break;
//                         case 3:
//                             if (item.house_number == itemToValidate) {
//                                 condition = true;
//                             }
//                             break;
//                         case 4:
//                             if (item.license_plate == itemToValidate) {
//                                 condition = true;
//                             }
//                             break;
//                         case 5:
//                             if (item.email.toUpperCase() == itemToValidate.toUpperCase()) {
//                                 condition = true;
//                             }
//                             break;
//                     }
//
//
//                 });
//                 return condition;
//             },
//             validatePermisson: function(permission_level) {
//                 if ($rootScope.user.permission_level != permission_level) {
//                     $state.go('home');
//                 }
//
//             },
//             moveToLinked: function(item, itemsToLink, itemsLinked) {
//                 var index = itemsToLink.indexOf(item);
//                 itemsToLink.splice(index, 1);
//                 var item = {
//                     id: item.id,
//                     name: item.name,
//                     last_name: item.last_name,
//                     second_last_name: item.second_last_name,
//                     identification_number: item.identification_number
//                 }
//                 itemsLinked.push(item);
//
//             },
//             capitalizeFirstLetter: function(string) {
//                 return string.charAt(0).toUpperCase() + string.slice(1);
//             },
//             moveToLink: function(item, itemsToLink, itemsLinked) {
//                 var index = itemsLinked.indexOf(item);
//                 itemsLinked.splice(index, 1);
//                 itemsToLink.push(item);
//             }
//
//         };
//     })
})();
