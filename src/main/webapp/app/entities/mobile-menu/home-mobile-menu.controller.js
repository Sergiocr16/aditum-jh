(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('HomeMobileMenuController', HomeMobileMenuController);

        HomeMobileMenuController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope', 'CommonMethods', 'globalCompany'];

        function HomeMobileMenuController(timeout, $scope, $stateParams, $rootScope, CommonMethods, globalCompany) {
            var vm = this;
            $rootScope.mainTitle = "Inicio";
            $rootScope.active = "home-mobile-menu";
            vm.isReady = false;

            function getRandomInt(max) {
                return Math.floor(Math.random() * (max - 0)) + 0;
            }


            vm.showMenuResident = function () {
                if ($rootScope.companyUser != undefined) {
                    if ($rootScope.companyUser.type >= 3) {
                        return true;
                    } else {
                        if ($rootScope.companyUser.type == 1) {
                            if (globalCompany.getHouseId() == $rootScope.companyUser.houseId) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            };
            vm.banners = ["/content/images/banner-morning.png", "/content/images/banner-afternoon.png", "/content/images/banner-night.png"];
            vm.grettingsMorning = ["Buenos dias", "Que excelente mañana"];
            vm.grettingsEvenning = ["Buenas tardes", "Que excelente tarde"];
            vm.grettingsNight = ["Buenas noches", "Que excelente noche"];
            vm.phrases = ["¿Hacemos algo juntos?", "¿Qué te gustaría hacer hoy?", "Por cierto ¿Ya te lavaste las manos?", "Estamos para servirte"]

            vm.defineBanner = function () {
                var now = new Date().getHours();
                if (now >= 5 && now < 13) {
                    vm.currentBanner = vm.banners[1];
                    vm.getting = vm.grettingsMorning[getRandomInt(vm.grettingsMorning.length)];
                    if (now >= 12) {
                        vm.getting = vm.grettingsEvenning[getRandomInt(vm.grettingsEvenning.length)];
                    }
                }
                if (now >= 13 && now <= 18) {
                    vm.currentBanner = vm.banners[1];
                    vm.getting = vm.grettingsEvenning[getRandomInt(vm.grettingsEvenning.length)];
                }
                if (now > 18 && now < 5) {
                    vm.currentBanner = vm.banners[2];
                    vm.getting = vm.grettingsNight[getRandomInt(vm.grettingsNight.length)];
                }
                vm.phrase = vm.phrases[getRandomInt(vm.phrases.length)]
            }
            vm.defineBanner();
            vm.showingMenu = false;

            $scope.$watch(function () {
                    return $rootScope.loadedMenu;
                }, function () {
                    if (!vm.showingMenu) {
                        var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                        vm.bookCommonArea = companyConfig.bookCommonArea;
                        vm.hasControlAccess = companyConfig.hasControlAccess;
                        vm.menu = [
                            {
                                title: "Reservar amenidad",
                                icon: "event",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "common-area-reservation-resident-view",
                                show: vm.bookCommonArea && vm.showMenuResident(),
                            },
                            {
                                title: "Reportar invitado",
                                icon: "person_add_alt",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "visitant-invited-user.new",
                                show: vm.showMenuResident() && vm.hasControlAccess,
                            },
                            {
                                title: "Informar a oficial",
                                icon: "forward_to_inbox",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "noteNew",
                                show: vm.showMenuResident() && vm.hasControlAccess,
                            },
                            {
                                title: "Ver mi estado de cuenta",
                                icon: "account_balance_wallet",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "accountStatus-residentAccount",
                                show: true,
                            },
                        ]
                        vm.menu2 = [
                            {
                                title: "Reportar emergencia",
                                icon: "local_fire_department",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "emergency.new",
                                show: vm.showMenuResident() && vm.hasControlAccess,
                            },
                            {
                                title: "Enviar observación del condominio",
                                icon: "record_voice_over",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "complaint-user.new",
                                show: true,
                            },
                            {
                                title: "Enviar solicitud al administrador",
                                icon: "forum",
                                authoritites: "ROLE_USER,ROLE_OWNER",
                                uisref: "individual-release-user.new",
                                show: $rootScope.adminCompany.id == 1,
                            },
                        ]
                        if(vm.bookCommonArea!=undefined){
                            vm.isReady = true;
                        }
                    }
                }
            )
        }
    }

)();
