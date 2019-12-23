(function () {
    'use strict';

    angular
        .module('aditumApp')
        .provider('CommonMethods', CommonMethods);

    function CommonMethods() {
        this.$get = getService;

        function getService($localStorage) {
            return {
                validateName: validateName,
                waitingMessage: waitingMessage,
                validateLetters: validateLetters,
                validateNumbers: validateNumbers,
                validateRepeat: validateRepeat,
                capitalizeFirstLetter: capitalizeFirstLetter,
                encryptIdUrl: encryptIdUrl,
                decryptIdUrl: decryptIdUrl,
                encryptString: encryptString,
                decryptString: decryptString,
                validateSpecialCharacters: validateSpecialCharacters,
                getCarBrands: getCarBrands,
                validateSpecialCharactersAndVocals: validateSpecialCharactersAndVocals,
                formatCurrencyInputs: formatCurrencyInputs,
                deleteFromArray: deleteFromArray,
                deleteFromArrayWithId: deleteFromArrayWithId,
                getCurrentCompanyConfig: getCurrentCompanyConfig,
                getInitialConfig: getInitialConfig,
                setInitialConfigReady:setInitialConfigReady
            };

            function getCurrentCompanyConfig(compaId) {
                if ($localStorage.companiesConfig != undefined) {
                    var companiesConfig = decryptIdUrl($localStorage.companiesConfig);
                    if (companiesConfig == "admin") {
                        return "admin";
                    } else {
                        var companiesArray = companiesConfig.split("|");
                        for (var i = 0; i < companiesArray.length; i++) {
                            var companyId = companiesArray[i].split(";")[0];
                            if (companyId == compaId) {
                                return {
                                    companyId: companyId,
                                    hasContability: companiesArray[i].split(";")[1],
                                    minDate: new Date(companiesArray[i].split(";")[2]),
                                    hasWatches: companiesArray[i].split(";")[3] == "1",
                                    showEstadoResultados: companiesArray[i].split(";")[4] == "true",
                                    showEjecPresu: companiesArray[i].split(";")[5] == "true",
                                    bookCommonArea: companiesArray[i].split(";")[6] == "true",
                                    initialConfiguration: companiesArray[i].split(";")[7],
                                    hasRounds:companiesArray[i].split(";")[8] == "true",
                                    currency: companiesArray[i].split(";")[9],
                                }
                            }
                        }
                    }
                } else {
                    return "admin";
                }
            }

            function getInitialConfig(compaId) {
                if ($localStorage.initialConfig != undefined) {
                    var initialConfig = decryptIdUrl($localStorage.initialConfig);
                    if (initialConfig == "admin") {
                        return "admin";
                    } else {
                        var companiesArray = initialConfig.split("|");
                        for (var i = 0; i < companiesArray.length; i++) {
                            var companyId = companiesArray[i].split(";")[0];
                            if (companyId == compaId) {
                                return {
                                    companyId: companyId,
                                    initialConfiguration: companiesArray[i].split(";")[1],
                                    hasContability: companiesArray[i].split(";")[2]
                                }
                            }
                        }
                    }
                } else {
                    return "admin";
                }
            }

            function setInitialConfigReady(compaId) {
                var initialConfig = decryptIdUrl($localStorage.initialConfig);
                var companies = initialConfig.split("|");
                var showInitialConfigArray = "";
                for (var i = 0; i < companies.length; i++) {
                    var companyId = companies[i].split(";")[0];
                    var companyConfig = this.getInitialConfig(companyId)
                    if (compaId != companyId) {
                        showInitialConfigArray += companyConfig.companyId + ";" + companyConfig.initialConfiguration + "|";
                    } else {
                        showInitialConfigArray += companyConfig.companyId + ";" + "1" + "|";
                    }
                }
                $localStorage.initialConfig = this.encryptIdUrl(showInitialConfigArray);
            }

            function deleteFromArrayWithId(object, array) {
                var index = undefined;
                var founded = false;
                angular.forEach(array, function (item, i) {
                    if (parseInt(item.id) === parseInt(object.id)) {
                        index = i;
                        founded = true;
                    } else {
                        if (founded == false) {
                            index = -1;
                        }
                    }
                })
                if (index != -1) {
                    array.splice(index, 1);
                }
            }

            function deleteFromArray(item, array) {
                var index = array.indexOf(item);
                if (index > -1) {
                    array.splice(index, 1);
                }
            }

            function validateName(items, name) {
                var condition = true;
                angular.forEach(items, function (item, index) {
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
                jQuery('.numbers').keypress(function (tecla) {
                    if (tecla.charCode < 48 || tecla.charCode > 57) return false;
                });
            }

            function validateRepeat(items, itemToValidate, criteria) {
                var condition = false;
                angular.forEach(items, function (item, index) {
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

            function encryptIdUrl(id) {
                return CryptoJS.AES.encrypt(id.toString(), "Ankara06").toString();
            }

            function decryptIdUrl(encryptedId) {
                return CryptoJS.AES.decrypt(encryptedId.toString(), "Ankara06").toString(CryptoJS.enc.Utf8);

            }

            function encryptString(id) {
                return CryptoJS.AES.encrypt(id, "Ankara06").toString();
            }

            function decryptString(encryptedId) {
                return CryptoJS.AES.decrypt(encryptedId, "Ankara06").toString(CryptoJS.enc.Utf8);
            }

            function validateLetters() {
                $(".letters").keypress(function (key) {
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
                if (string != undefined) {
                    return string.charAt(0).toUpperCase() + string.slice(1);
                }
            }

            function getCarBrands() {
                var brands = {
                    data: [{

                        brand: "Audi"
                    }, {
                        brand: "Alfa Romeo"
                    }, {
                        brand: "BMW"
                    }, {
                        brand: "BYD"
                    }, {
                        brand: "Chevrolet"
                    }, {
                        brand: "Citroen"
                    }, {
                        brand: "Daewoo"
                    }, {
                        brand: "Daihatsu"
                    }, {
                        brand: "Dodge"
                    }, {
                        brand: "Fiat"
                    }, {
                        brand: "Ford"
                    }, {
                        brand: "Honda"
                    }, {
                        brand: "Hummer"
                    }, {
                        brand: "Hyundai"
                    }, {
                        brand: "Izuzu"
                    }, {
                        brand: "Jaguar"
                    }, {
                        brand: "JAC"
                    }, {
                        brand: "Jeep"
                    }, {
                        brand: "Kia"
                    }, {
                        brand: "Land Rover"
                    }, {
                        brand: "Lexus"
                    }, {
                        brand: "Maserati"
                    }, {
                        brand: "Mazda"
                    }, {
                        brand: "Mercedes Benz"
                    }, {
                        brand: "Mini"
                    }, {
                        brand: "Mitsubishi"
                    }, {
                        brand: "Nissan"
                    }, {
                        brand: "Peugeot"
                    }, {
                        brand: "Porshe"
                    }, {
                        brand: "Renault"
                    }, {
                        brand: "Rolls Royce"
                    }, {
                        brand: "Ssanyong"
                    }, {
                        brand: "Subaru"
                    }, {
                        brand: "Suzuki"
                    }, {
                        brand: "Toyota"
                    }, {
                        brand: "Volkswagen"
                    }, {
                        brand: "Volvo"

                    },]
                }

                return brands;
            }

            function validateSpecialCharacters() {
                jQuery('.specialCharacters').keypress(function (tecla) {
                    if ((tecla.charCode < 48) || (tecla.charCode > 90 && tecla.charCode < 97) || (tecla.charCode > 122 && tecla.charCode < 126) || (tecla.charCode > 57 && tecla.charCode < 65)) return false;
                });
            }

            function validateSpecialCharactersAndVocals() {
                jQuery('.specialCharactersAndVocals').keypress(function (tecla) {
                    if ((tecla.charCode < 48) || (tecla.charCode > 90 && tecla.charCode < 97) || (tecla.charCode > 122 && tecla.charCode < 126) || (tecla.charCode > 57 && tecla.charCode < 65) || tecla.charCode == 97 || tecla.charCode == 101 || tecla.charCode == 105 || tecla.charCode == 111 || tecla.charCode == 117) return false;
                });
            }

            function validateRepeat(items, itemToValidate, criteria) {
                var condition = false;
                angular.forEach(items, function (item, index) {
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

            function formatCurrencyInputs() {

                var $form = $("#form");
                var $input = $('input.currency')

                $input.on("keyup", function (event) {


                    // When user select text in the document, also abort.
                    var selection = window.getSelection().toString();
                    if (selection !== '') {
                        return;
                    }

                    // When the arrow keys are pressed, abort.
                    if ($.inArray(event.keyCode, [38, 40, 37, 39]) !== -1) {
                        return;
                    }


                    var $this = $(this);

                    // Get the value.
                    var input = $this.val();

                    var input = input.replace(/[\D\s\._\-]+/g, "");
                    input = input ? parseInt(input, 10) : 0;

                    $this.val(function () {
                        return (input === 0) ? "" : input.toLocaleString("en-US");
                    });
                });

                /**
                 * ==================================
                 * When Form Submitted
                 * ==================================
                 */
                $form.on("submit", function (event) {

                    var $this = $(this);
                    var arr = $this.serializeArray();

                    for (var i = 0; i < arr.length; i++) {
                        arr[i].value = arr[i].value.replace(/[($)\s\._\-]+/g, ''); // Sanitize the values.
                    }
                    ;


                    event.preventDefault();
                });

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
//             validatebrand:function(items, name) {
//                 var condition = true;
//                 angular.forEach(items, function(item, index) {
//                     if (item.name.toUpperCase() == name.toUpperCase()) {
//                         condition = false;
//                     }
//                 });
//                 return condition;
//             },
//

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
//                     brand:item.name,
//                     last_brand:item.last_name,
//                     second_last_brand:item.second_last_name,
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
