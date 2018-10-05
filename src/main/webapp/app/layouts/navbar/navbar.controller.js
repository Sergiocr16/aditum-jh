(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['CommonMethods', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'MultiCompany', '$rootScope', '$scope', 'companyUser', 'Company', 'House', '$mdSidenav'];

    function NavbarController(CommonMethods, $state, Auth, Principal, ProfileService, LoginService, MultiCompany, $rootScope, $scope, companyUser, Company, House, $mdSidenav) {
        var vm = this;
        vm.colors = {primary: "#01579B", secondary: "#E1F5FE",normalColorFont:"##c3c3c3"};
        vm.colorsMenu = {
            mainButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            mainButtonActive: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:' + vm.colors.secondary + '!important',
            },
            mainButtonHover:{
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButtonActive: {
                color: 'color:black!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButtonHover:{
                color: 'color:black!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonActive: {
                color: 'color:#474747!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonHover:{
                color: 'color:#474747!important',
                backgroundColor: 'background-color:white!important',
            }
        };

        // MENU EXAMPLE STRUDCTURE
        // vm.menu = [
        //     {
        //         title: "Administración",
        //         icon: "assessment",
        //         authoritites: "ROLE_ADMIN",
        //         activeOn: "company,condons,admins,recursosHumanos,brands,destinies,dataprogress",
        //         collapsable: true,
        //         uisref: "",
        //         menuId: "administracionMenu",
        //         secondaryItems: [
        //             {
        //                 title: "Condominios",
        //                 icon: "home",
        //                 authoritites: "ROLE_ADMIN",
        //                 activeOn: "company",
        //                 collapsable: true,
        //                 uisref: "",
        //                 menuId: "condosMenu",
        //                 thirdItems: [
        //                     {
        //                         title: "Carros",
        //                         icon: "home",
        //                         authoritites: "ROLE_ADMIN",
        //                         activeOn: "company",
        //                         uisref: "admins"
        //                     },
        //                 ]
        //             }
        //         ]
        //     }
        // ];
        // END MENU EXAMPLE STRUCTURE
        vm.menu = [
            {
                title: "Administración",
                icon: "assessment",
                authoritites: "ROLE_ADMIN",
                activeOn: "condons,admins,admins,recursosHumanos,brands,destinies,dataprogress",
                collapsable: true,
                uisref: "",
                menuId: "administracionSuperAdminMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Condominios",
                        icon: "home",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "condons",
                        collapsable: false,
                        uisref: "company",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Administradores",
                        icon: "supervised_user_circle",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "admins",
                        collapsable: false,
                        uisref: "admin-info",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Recursos humanos",
                        icon: "supervised_user_circle",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "recursosHumanos",
                        collapsable: false,
                        uisref: "rh-account",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Marcas vehículos",
                        icon: "directions_car",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "brands",
                        collapsable: false,
                        uisref: "brand",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Destinos puerta acceso",
                        icon: "arrow_right_alt",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "destinies",
                        collapsable: false,
                        uisref: "destinies",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Progreso ingreso de datos",
                        icon: "show_chart",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "dataprogress",
                        collapsable: false,
                        uisref: "data-progress",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Aditum Jhipster",
                icon: "bar_chart",
                authoritites: "ROLE_ADMIN",
                activeOn: "user-management,tracker,metrics,health,configuration,audits,logs,docs",
                collapsable: true,
                uisref: "",
                menuId: "aditumJhipsterMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Gestión de usuarios",
                        icon: "supervised_user_circle",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "user-management",
                        collapsable: false,
                        uisref: "user-management",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Tracker",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "tracker",
                        collapsable: false,
                        uisref: "jhi-tracker",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Métricas",
                        icon: "pie_chart",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "metrics",
                        collapsable: false,
                        uisref: "jhi-metrics",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Salud",
                        icon: "sentiment_satisfied_alt",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "health",
                        collapsable: false,
                        uisref: "jhi-health",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Configuración",
                        icon: "build",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "configuration",
                        collapsable: false,
                        uisref: "jhi-configuration",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Auditorías",
                        icon: "people",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "audits",
                        collapsable: false,
                        uisref: "audits",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Logs",
                        icon: "pan_tool",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "logs",
                        collapsable: false,
                        uisref: "logs",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "API",
                        icon: "library_books",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "docs",
                        collapsable: false,
                        uisref: "docs",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Dashboard",
                icon: "dashboard",
                authoritites: "ROLE_MANAGER",
                activeOn: "dashboard",
                collapsable: false,
                uisref: "dashboard",
                menuId: "",
                hover:false,
                secondaryItems: []
            },
            {
                title: "Noticias",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "announcements,userNews",
                collapsable: true,
                uisref: "",
                menuId: "noticiasMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Ver noticias",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "userNews",
                        collapsable: false,
                        uisref: "announcement-user",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Administrar noticias",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "announcements",
                        collapsable: false,
                        uisref: "announcement",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Noticias",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "userNews",
                collapsable: false,
                uisref: "announcement-user",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },
        ];





        vm.defineStyleSecondButton = function (item) {
            if(item.hover){
                return vm.colorsMenu.secondButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.secondButtonActive
            } else {
                return vm.colorsMenu.secondButton
            }
        };
        vm.defineStyleThirdButton = function (item) {
            if(item.hover){
                return vm.colorsMenu.thirdButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.thirdButtonActive
            } else {
                return vm.colorsMenu.thirdButton
            }
        };
        vm.defineStyleMainButton = function (item) {
            if(item.hover){
                return vm.colorsMenu.mainButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.mainButtonActive
            } else {
                return vm.colorsMenu.mainButton
            }
        };
        vm.defineActive = function (item) {
            var items = item.activeOn.split(",");
            var count = 0;
            for (var i = 0; i < items.length; i++) {
                if ($rootScope.active == items[i]) {
                    count++;
                }
            }
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        };

        vm.go = function (uisref) {
            $rootScope.toggleLeft();
            $state.go(uisref)
        };


        $rootScope.isAuthenticated = Principal.isAuthenticated;
        vm.isAuthenticated = Principal.isAuthenticated;
        $rootScope.toggleLeft = buildToggler('left');

        function buildToggler(componentId) {
            return function () {
                $mdSidenav(componentId).toggle();
            };
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
        }

        if ($rootScope.inicieSesion == undefined) {
            $rootScope.inicieSesion = true;
        }
        $rootScope.$on('$stateChangeStart',
            function (event, toState, toParams, fromState, fromParams) {
                MultiCompany.getCurrentUserCompany().then(function (data) {
                    if (data != undefined) {
                        if (data.enable == 0 || data.enabled == 0) {
                            logout();
                        }
                    }
                })
            })
        vm.annoActual = moment(new Date()).format("YYYY");
        vm.editMyInfoAsManager = function () {
            $state.go('admin-info-edit')
        }
        vm.editMyInfoAsUser = function () {
            var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyUser.id)
            $state.go('residentByHouse.edit', {
                id: encryptedId
            })
        }
        angular.element(document).ready(function () {
            $('body').addClass("gray");
        });
        vm.viewWatch = function () {
            var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyId)
            $state.go('turno', {companyId: encryptedId})
        }

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;


        vm.getAcount = function () {
            Principal.identity().then(function (account) {
                vm.account = account;
                switch (account.authorities[0]) {
                    case "ROLE_ADMIN":
                        vm.contextLiving = "Dios de Aditum";
                        $rootScope.contextLiving = vm.contextLiving;
                        $rootScope.currentUserImage = null;
                        $rootScope.hideFilial = true;
                        break;
                    case "ROLE_MANAGER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            if (data.companies.length > 1) {
                                $rootScope.showSelectCompany = true;
                            }
                            if (data.companies.length > 1 && $rootScope.companyId == undefined) {
                                $state.go('dashboard.selectCompany');
                            } else {
                                if ($rootScope.companyId == undefined) {
                                    $rootScope.companyUser = data;
                                    $rootScope.companyUser.companyId = data.companies[0].id;
                                    $rootScope.companyId = data.companies[0].id;

                                }
                                Company.get({id: $rootScope.companyId}, function (condo) {
                                    vm.contextLiving = condo.name;
                                    $rootScope.companyName = condo.name;
                                    $rootScope.contextLiving = vm.contextLiving;
                                    $rootScope.currentUserImage = data.image_url;
                                    if (data.enabled == 0) {
                                        logout();
                                    }
                                })
                            }
                            $rootScope.hideFilial = true;
                        })
                        break;
                    case "ROLE_OFFICER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            $rootScope.companyId = data.companyId;
                            if (data != null) {
                                vm.contextLiving = $rootScope.companyUser.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                            }
                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_USER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            House.get({id: parseInt(data.houseId)}, function (house) {
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.hideFilial = false;
                                $rootScope.filialNumber = house.housenumber;
                                $rootScope.companyId = data.companyId;
                                $rootScope.currentUserImage = data.image_url;
                                $rootScope.companyUser = data;
                                Company.get({id: parseInt($rootScope.companyId)}, function (condo) {
                                    vm.contextLiving = condo.name;
                                    $rootScope.contextLiving = vm.contextLiving;
                                    if (condo.active == 0 || data.enabled == 0) {
                                        logout();
                                    }
                                })
                            })
                        })
                        break;
                    case "ROLE_RH":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            if (data != null) {
                                vm.contextLiving = " / " + data.enterprisename;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                            }
                            if (data.enable == 0) {
                                logout();
                            }
                        })
                        break;
                }
            })
        }

        setTimeout(function () {
            Principal.identity().then(function (account) {
                if (account !== null) {
                    $rootScope.companyUser = companyUser;
                    vm.getAcount();
                }
            })
        })
        var subLogin = $scope.$on('authenticationSuccess', vm.getAcount);


        function login() {
            collapseNavbar();
            LoginService.open();
        }


        vm.openManual = function () {
            window.open('/#/manual-residente', '_blank')
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

//        $scope.$on('$destroy', subChangeState);
        $scope.$on('$destroy', subLogin);
    }
})
();
