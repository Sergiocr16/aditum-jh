(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$state', 'Principal', '$timeout', 'Auth', 'MultiCompany', 'House', '$localStorage', 'CommonMethods', 'Modal', 'CompanyConfiguration', 'AdministrationConfiguration', 'MacroCondominium'];

    function LoginController($rootScope, $state, Principal, $timeout, Auth, MultiCompany, House, $localStorage, CommonMethods, Modal, CompanyConfiguration, AdministrationConfiguration, MacroCondominium) {

        var vm = this;
        vm.isIdentityResolved = Principal.isIdentityResolved;
        vm.pdfUrl = 'content/manuals/manualusuario.pdf';
        // vm.loadNewFile = function (url) {
        //     pdfDelegate
        //         .$getByHandle('my-pdf-container')
        //         .load(url);
        // };

        vm.anno = moment(new Date()).format('YYYY')
        $rootScope.showLogin = true;
        $rootScope.showSelectCompany = false;
        vm.isChangingPassword = $state.includes('finishReset');
        vm.isResetPassword = $state.includes('requestReset');
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.authenticationError = false;
        vm.cancel = cancel;
        vm.credentials = {};
        vm.login = login;
        vm.showLoginHelp = showLoginHelp;
        vm.password = null;
//        vm.register = register;
        vm.rememberMe = true;
        vm.requestResetPassword = requestResetPassword;
        vm.username = null;

        $timeout(function () {
            angular.element('#username').focus();
        });

        function cancel() {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
        }

        function showLoginHelp() {
            Modal.dialog("Nombre de usuario", "Tu nombre de usuario está constituido por la primera letra de tu nombre, tu primer apellido y la primera letra de tu segundo apellido. Ejemplo: Nombre: Antonio Vega Castro. Usuario: avegac", "¡Entendido!")
        }

        function defineCompanyConfig(companyConfig, administrationConfiguration) {
            return companyConfig.companyId + ";" + companyConfig.hasContability + ";" + companyConfig.minDate + ";" + companyConfig.hasAccessDoor + ";" + administrationConfiguration.incomeStatement + ";" + administrationConfiguration.monthlyIncomeStatement + ";" + administrationConfiguration.bookCommonArea + ";" + administrationConfiguration.initialConfiguration + ";" + companyConfig.hasRounds + ";" + companyConfig.currency + ";" + companyConfig.quantityadmins + "|";
        }


        function loadCompanyConfig(companiesTotal, i, companiesConfigArray, showInitialConfigArray) {
            if (i == companiesTotal) {
                $localStorage.companiesConfig = CommonMethods.encryptIdUrl(companiesConfigArray);
                $localStorage.initialConfig = CommonMethods.encryptIdUrl(showInitialConfigArray);
            }
        }

        function login(event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function (data) {
                console.log(data)
                vm.authenticationError = false;
                Principal.identity().then(function (account) {
                    $rootScope.menu = true;
                    $rootScope.showLogin = false;
                    $rootScope.inicieSesion = true;
                    switch (account.authorities[0]) {
                        case "ROLE_ADMIN":
                            $state.go('company');
                            break;
                        case "ROLE_MANAGER":
                            MultiCompany.getCurrentUserCompany().then(function (user) {
                                $rootScope.companyUser = user;
                                $rootScope.showSelectCompany = false;
                                $localStorage.companyId = CommonMethods.encryptIdUrl(user.companies[0].id);
                                var companiesConfigArray = "";
                                var showInitialConfigArray = "";
                                for (var i = 0; i < user.companies.length; i++) {
                                    CompanyConfiguration.get({id: user.companies[i].id}, function (companyConfig) {
                                        $rootScope.currency = companyConfig.currency;
                                        AdministrationConfiguration.get({companyId: companyConfig.companyId}, function (result) {
                                            var administrationConfiguration = result;
                                            companiesConfigArray += defineCompanyConfig(companyConfig, administrationConfiguration);
                                            showInitialConfigArray += companyConfig.companyId + ";" + administrationConfiguration.initialConfiguration + ";" + companyConfig.hasContability + "|";
                                            if (user.companies.length == i) {
                                                vm.backgroundSelectCompany = true;
                                                loadCompanyConfig(user.companies.length, i, companiesConfigArray, showInitialConfigArray);
                                                $state.go('dashboard');
                                            }
                                        });
                                    })
                                }
                            })
                            break;
                        case "ROLE_MANAGER_MACRO":
                            // MultiCompany.getCurrentUserCompany().then(function (user) {
                            //     $rootScope.companyUser = user;
                            //     $rootScope.showSelectCompany = false;
                            //     var companiesConfigArray = "";
                            //     var showInitialConfigArray = "";
                            //     MacroCondominium.get({id: user.macroCondominiumId}, function (macroCondo) {
                            //         $rootScope.companyUser.companies = macroCondo.companies;
                            //         $localStorage.companyId = CommonMethods.encryptIdUrl(macroCondo.companies[0].id);
                            //         $localStorage.macroCompanyId = CommonMethods.encryptIdUrl(user.macroCondominiumId);
                            //         for (var i = 0; i < macroCondo.companies.length; i++) {
                            //             CompanyConfiguration.get({id: macroCondo.companies[i].id}, function (companyConfig) {
                            //                 $rootScope.currency = companyConfig.currency;
                            //                 AdministrationConfiguration.get({companyId: companyConfig.companyId}, function (result) {
                            //                     var administrationConfiguration = result;
                            //                     companiesConfigArray += defineCompanyConfig(companyConfig,administrationConfiguration);
                            //                     showInitialConfigArray += companyConfig.companyId + ";" + administrationConfiguration.initialConfiguration + "|";
                            //                     if (macroCondo.companies.length == i) {
                            //                         vm.backgroundSelectCompany = true;
                            //                         $localStorage.companiesConfig = CommonMethods.encryptIdUrl(companiesConfigArray);
                            //                         $localStorage.initialConfig = CommonMethods.encryptIdUrl(showInitialConfigArray);
                            $state.go('dashboard');
                            //                     }
                            //                 });
                            //             })
                            //         }
                            //     })
                            // })
                            break;
                        case "ROLE_OFFICER":
                            $state.go('access-door.access');
                            break;
                        case "ROLE_OFFICER_MACRO":
                            // MultiCompany.getCurrentUserCompany().then(function (data) {
                            //     $localStorage.companyId = CommonMethods.encryptIdUrl(data.macroCondominiumId);
                            $state.go('access-door-macro.access');
                        // });
                        // break;
                        case "ROLE_USER":
                            MultiCompany.getCurrentUserCompany().then(function (data) {
                                console.log(data)



                                $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
                                if(data.houses.length>0){
                                    $localStorage.houseId = CommonMethods.encryptIdUrl(data.houses[0].id);
                                    $rootScope.houseSelected = data.houses[0];
                                }else{
                                    $localStorage.houseId = data.houseId;
                                    $rootScope.houseSelected = data.houseClean[0];
                                }

                                $rootScope.currentUserImage = data.image_url;
                                $rootScope.companyUser = data;
                                $localStorage.userId = CommonMethods.encryptIdUrl(data.id);
                                $localStorage.userRole = CommonMethods.encryptIdUrl("ROLE_USER");
                                $localStorage.userIdNumber = CommonMethods.encryptIdUrl(data.identificationnumber);

                                var companiesConfigArray = "";
                                CompanyConfiguration.get({id: data.companyId}, function (companyConfig) {
                                    vm.backgroundSelectCompany = true;
                                    $rootScope.currency = companyConfig.currency;
                                    AdministrationConfiguration.get({companyId: data.companyId}, function (result) {
                                        var administrationConfiguration = result;
                                        companiesConfigArray += defineCompanyConfig(companyConfig, administrationConfiguration);
                                        $localStorage.companiesConfig = CommonMethods.encryptIdUrl(companiesConfigArray);
                                    });
                                    // setTimeout(function () {
                                    $state.go("announcement-user", {}, {reload: true});
                                    // $state.go('announcement-user');
                                    //         }, 300);
                                })
                            });
                            break;
                        case "ROLE_OWNER":
                            MultiCompany.getCurrentUserCompany().then(function (data) {
                                $rootScope.houseSelected = data.houses[0];
                                $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
                                $localStorage.houseId = CommonMethods.encryptIdUrl(data.houses[0].id);
                                $rootScope.currentUserImage = data.image_url;
                                $rootScope.companyUser = data;
                                $localStorage.userId = CommonMethods.encryptIdUrl(data.id);
                                $localStorage.userRole = CommonMethods.encryptIdUrl("ROLE_USER");
                                console.log(data.identificationnumber)
                                if(data.identificationnumber==undefined || data.identificationnumber==null  ){
                                    $localStorage.userIdNumber = CommonMethods.encryptIdUrl("");

                                }else{
                                    $localStorage.userIdNumber = CommonMethods.encryptIdUrl(data.identificationnumber);
                                }
                                $localStorage.houseSelected = data.houses[0];
                                var companiesConfigArray = "";
                                CompanyConfiguration.get({id: data.companyId}, function (companyConfig) {
                                    vm.backgroundSelectCompany = true;
                                    $rootScope.currency = companyConfig.currency;
                                    AdministrationConfiguration.get({companyId: data.companyId}, function (result) {
                                        var administrationConfiguration = result;
                                        companiesConfigArray += defineCompanyConfig(companyConfig, administrationConfiguration);
                                        $localStorage.companiesConfig = CommonMethods.encryptIdUrl(companiesConfigArray);
                                    });
                                    // setTimeout(function () {
                                    $state.go("announcement-user", {}, {reload: true});
                                    // $state.go('announcement-user');
                                    //         }, 300);
                                })
                            });
                            break;
                        case "ROLE_RH":
                            $rootScope.active = "company-rh";
                            $state.go('company-rh');
                            break;
                        case "ROLE_JD":
                            // MultiCompany.getCurrentUserCompany().then(function (data) {
                            //     $rootScope.companyUser = data;
                            //     $rootScope.showSelectCompany = false;
                            //     $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
                            //     $rootScope.companyId = data.companyId;
                            //     var companiesConfigArray = "";
                            //     CompanyConfiguration.get({id: data.companyId}, function (companyConfig) {
                            //         vm.backgroundSelectCompany = true;
                            //         $rootScope.currency = companyConfig.currency;
                            //         AdministrationConfiguration.get({companyId: data.companyId}, function (result) {
                            //             var administrationConfiguration = result;
                            //             companiesConfigArray += defineCompanyConfig(companyConfig,administrationConfiguration);
                            //             $localStorage.companiesConfig = CommonMethods.encryptIdUrl(companiesConfigArray);
                            //         });
                            //         setTimeout(function () {
                            //             vm.backgroundSelectCompany = true;
                            $state.go('dashboard');
                            //         }, 300);
                            //     })
                            // });
                            break;
                    }
                    if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                        $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                        $state.go('home');
                    }
                });
                $rootScope.$broadcast('authenticationSuccess');
                $('body').addClass("gray");
                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is successful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }
            }).catch(function (a) {
                vm.authenticationError = true;
                Modal.toast("Credenciales inválidos o cuenta deshabilitada.")
            });
        }

//        function register () {
////            $uibModalInstance.dismiss('cancel');
//            $state.go('register');
//        }

        function requestResetPassword() {
            $state.go('requestReset');
        }
    }
})();
