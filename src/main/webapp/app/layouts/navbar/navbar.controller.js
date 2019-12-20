(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);
    NavbarController.$inject = ['WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'WSOfficer', '$timeout', 'CommonMethods', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'MultiCompany', '$rootScope', '$scope', 'companyUser', 'Company', 'MacroCondominium', 'House', '$mdSidenav', '$localStorage', 'globalCompany', 'WSDeleteEntity', 'WSEmergency'];

    function NavbarController(WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, WSOfficer, $timeout, CommonMethods, $state, Auth, Principal, ProfileService, LoginService, MultiCompany, $rootScope, $scope, companyUser, Company, MacroCondominium, House, $mdSidenav, $localStorage, globalCompany, WSDeleteEntity, WSEmergency) {
        var vm = this;
        vm.colors = {primary: "rgb(0,150,136)", secondary: "#E1F5FE", normalColorFont: "#37474f"};
        $rootScope.colors = vm.colors;
        vm.hasContability = false;
        $rootScope.currentUserImage = null;
        vm.menuResident = [];
        vm.menuFinanzas = {};
        vm.colorsMenu = {
            mainButton: {
                color: 'color:' + '#37474f',
                backgroundColor: 'background-color:white!important',
            },
            mainButtonActive: {
                color: 'color:' + '#37474f',
                backgroundColor: 'background-color:white!important',
            },
            mainButtonHover: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButtonActive: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:' + vm.colors.secondary + '!important',
            },
            secondButtonHover: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonActive: {
                color: 'color:' + '#f37960!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonHover: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            }
        };

        $scope.$on("$destroy", function () {
            vm.menu = [];
        })

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

        vm.showMenuFinanzas = function () {
            if ($rootScope.companyUser != undefined) {
                if ($rootScope.companyUser.type >= 3) {
                    return false;
                } else {
                    if ($rootScope.companyUser.type == 1) {
                        return true;
                        if (globalCompany.getHouseId() == $rootScope.companyUser.houseId) {
                            return true;
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            } else {
                return false;
            }
        };
        vm.chargeMenu = function (hasComta) {
            vm.menu = [
                {
                    title: "ADMINISTRACIÓN",
                    activeOn: "company,condons,admins,recursosHumanos,brands,destinies,dataprogress,macro-condominium",
                    authoritites: "ROLE_ADMIN",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        // {
                        //     title: "Condominios Macro",
                        //     icon: "account_balance",
                        //     authoritites: "ROLE_ADMIN",
                        //     activeOn: "macro-condominium",
                        //     collapsable: false,
                        //     uisref: "macro-condominium",
                        //     menuId: "",
                        //     hover: false,
                        //     thirdItems: [],
                        //     showXs: true,
                        //     showLg: true,
                        //
                        // },
                        {
                            title: "Condominios",
                            icon: "home",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "condons",
                            collapsable: false,
                            uisref: "company",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        },
                        {
                            title: "Administradores",
                            icon: "supervised_user_circle",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "admins",
                            collapsable: false,
                            uisref: "admin-info-by-company",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true

                        },
                        {
                            title: "Cuenta oficiales",
                            icon: "verified_user",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "officerAccount",
                            collapsable: false,
                            uisref: "officerAccounts",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true

                        },
                        //
                        //
                        //
                        // {
                        //     title: "Recursos humanos",
                        //     icon: "contacts",
                        //     authoritites: "ROLE_ADMIN",
                        //     activeOn: "recursosHumanos",
                        //     collapsable: false,
                        //     uisref: "rh-account",
                        //     menuId: "",
                        //     hover: false,
                        //     thirdItems: [],
                        //     showXs: true,
                        //     showLg: true
                        //
                        // },
                        {
                            title: "Bitácora de acciones",
                            icon: "chrome_reader_mode",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "bitacoraAcciones",
                            collapsable: false,
                            uisref: "bitacora-acciones",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Soporte",
                            icon: "live_help",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "soporte",
                            collapsable: false,
                            uisref: "soporte",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Marcas vehículos",
                            icon: "directions_car",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "brands",
                            collapsable: false,
                            uisref: "brand",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true

                        },
                        {
                            title: "Destinos puerta acceso",
                            icon: "store",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "destinies",
                            collapsable: false,
                            uisref: "destinies",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true

                        },
                        // {
                        //     title: "Ingreso de datos",
                        //     icon: "show_chart",
                        //     authoritites: "ROLE_ADMIN",
                        //     activeOn: "dataprogress",
                        //     collapsable: false,
                        //     uisref: "data-progress",
                        //     menuId: "",
                        //     hover: false,
                        //     showXs: true,
                        //     showLg: true
                        // },
                    ]
                },
                {
                    title: "ADITUM LEGAL",
                    activeOn: "",
                    authoritites: "ROLE_ADMIN",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Reglamentos",
                            icon: "gavel",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "regulation",
                            collapsable: false,
                            uisref: "regulation",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        },
                        {
                            title: "Categorías",
                            icon: "category",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "article-category",
                            collapsable: false,
                            uisref: "article-category",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        },
                        {
                            title: "Palabras clave",
                            icon: "vpn_key",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "key-words",
                            collapsable: false,
                            uisref: "key-words",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        },
                        {
                            title: "Búsqueda",
                            icon: "vpn_key",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "regulation-search",
                            collapsable: false,
                            uisref: "regulation-search-tabs.byCategories",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        }
                    ]
                },
                {
                    title: "ADITUM JHIPSTER",
                    activeOn: "company,condons,admins,recursosHumanos,brands,destinies,dataprogress",
                    authoritites: "ROLE_ADMIN",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Gestión de usuarios",
                            icon: "supervised_user_circle",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "user-management",
                            collapsable: false,
                            uisref: "user-management",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Tracker",
                            icon: "remove_red_eye",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "tracker",
                            collapsable: false,
                            uisref: "jhi-tracker",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Métricas",
                            icon: "pie_chart",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "metrics",
                            collapsable: false,
                            uisref: "jhi-metrics",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Salud",
                            icon: "sentiment_satisfied_alt",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "health",
                            collapsable: false,
                            uisref: "jhi-health",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Configuración",
                            icon: "build",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "configuration",
                            collapsable: false,
                            uisref: "jhi-configuration",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Auditorías",
                            icon: "people",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "audits",
                            collapsable: false,
                            uisref: "audits",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Logs",
                            icon: "pan_tool",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "logs",
                            collapsable: false,
                            uisref: "logs",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "API",
                            icon: "library_books",
                            authoritites: "ROLE_ADMIN",
                            activeOn: "docs",
                            collapsable: false,
                            uisref: "docs",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true
                        },
                    ]
                },

                {
                    title: "Condominio",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Dashboard",
                            icon: "dashboard",
                            authoritites: "ROLE_MANAGER_MACRO,ROLE_MANAGER",
                            activeOn: "dashboard",
                            collapsable: false,
                            uisref: "dashboard",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Propietarios",
                            icon: "group",
                            authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                            activeOn: "owner",
                            collapsable: false,
                            uisref: "owner",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Filiales",
                            icon: "home",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "houses",
                            collapsable: false,
                            uisref: "houses-tabs.house",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                    ]
                },

                {
                    title: "Comunicación",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Áreas comunes",
                            icon: "local_florist",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "reservationAdministration,reservations,generaCalendar,createReservation,devolutions",
                            collapsable: true,
                            uisref: "",
                            menuId: "areascomunesMenu",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Administrar",
                                    icon: "view_agenda",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "reservationAdministration",
                                    collapsable: false,
                                    uisref: "common-area-administration.common-area",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                },
                                {
                                    title: "Reservaciones",
                                    icon: "view_comfy",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "reservations",
                                    collapsable: false,
                                    uisref: "common-area-administration.common-area-reservations",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                },
                                {
                                    title: "Ver calendario",
                                    icon: "event_note",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "generaCalendar",
                                    collapsable: false,
                                    uisref: "common-area-administration.general-reservation-calendar",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                },
                                {
                                    title: "Reservar",
                                    icon: "event_available",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "createReservation",
                                    collapsable: false,
                                    uisref: "common-area-administration.newReservation",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                },
                                {
                                    title: "Devoluciones",
                                    icon: "payment",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "devolutions",
                                    collapsable: false,
                                    uisref: "common-area-devolution-administration.pending-devolution",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                },


                            ]


                        },
                        {
                            title: "Noticias",
                            icon: "picture_in_picture",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "announcements,userNews",
                            collapsable: true,
                            uisref: "",
                            menuId: "noticiasMenu",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Ver noticias",
                                    icon: "remove_red_eye",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "userNews",
                                    collapsable: false,
                                    uisref: "announcement-user",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true
                                },
                                {
                                    title: "Administrar noticias",
                                    icon: "view_agenda",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "announcements",
                                    collapsable: false,
                                    uisref: "announcements.announcement",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true
                                },


                            ]


                        },
                        {
                            title: "Gestionar quejas",
                            icon: "sentiment_very_dissatisfied",
                            authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                            activeOn: "complaint",
                            collapsable: false,
                            uisref: "complaint",
                            menuId: "",
                            hover: false,
                            secondaryItems: [],
                            showXs: true,
                            showLg: true,
                        },


                    ]
                },
                {
                    title: "ADITUM LEGAL",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Reglamentos",
                            icon: "gavel",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "regulation",
                            collapsable: false,
                            uisref: "regulation",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Búsqueda",
                            icon: "vpn_key",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "regulation-search",
                            collapsable: false,
                            uisref: "regulation-search-tabs.byCategories",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        },
                        {
                            title: "Contratos",
                            icon: "description",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "contract",
                            collapsable: false,
                            uisref: "contract",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,

                        }
                    ]
                },
                {
                    title: "Control de acceso",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER_MACRO,ROLE_MANAGER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        showCondoAdministrationContability(),
                        {
                            title: "Mis visitas",
                            icon: "person_pin",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "reportInvitation,residentsInvitedVisitors",
                            collapsable: true,
                            uisref: "",
                            menuId: "administracionVisitasMenu",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: [

                                {
                                    title: "Reportar visita",
                                    icon: "perm_identity",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "reportInvitation",
                                    collapsable: false,
                                    uisref: "visitant-invited-user.new",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                },
                                {
                                    title: "Visitantes invitados",
                                    icon: "account_circle",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "residentsInvitedVisitors",
                                    collapsable: false,
                                    uisref: "visitant-invited-user",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true,
                                }
                            ]
                        },

                        // {
                        //     title: "Seguridad",
                        //     icon: "security",
                        //     authoritites: "ROLE_MANAGER",
                        //     activeOn: "officers,watches",
                        //     collapsable: true,
                        //     uisref: "",
                        //     menuId: "seguridadMenu",
                        //     hover: false,
                        //     showXs: true,
                        //     showLg: true,
                        //     thirdItems: [
                        //         {
                        //             title: "Oficiales",
                        //             icon: "people_outline",
                        //             authoritites: "ROLE_MANAGER",
                        //             activeOn: "officers",
                        //             collapsable: false,
                        //             uisref: "officer",
                        //             menuId: "",
                        //             hover: false,
                        //             thirdItems: [],
                        //             showXs: true,
                        //             showLg: true
                        //         },
                        //         {
                        //             title: "Turnos",
                        //             icon: "access_time",
                        //             authoritites: "ROLE_MANAGER",
                        //             activeOn: "watches",
                        //             collapsable: false,
                        //             uisref: "turno",
                        //             menuId: "",
                        //             hover: false,
                        //             thirdItems: [],
                        //             showXs: vm.hasWatches,
                        //             showLg: vm.hasWatches
                        //         },
                        //     ]
                        // },

                    ]
                },
                {
                    title: "Finanzas",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: hasComta,
                    secondaryItems: [
                        {
                            title: "Contabilidad filiales",
                            icon: "dvr",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "houseAdministration",
                            collapsable: false,
                            uisref: "houseAdministration.accountStatus",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Ver egresos",
                            icon: "remove_red_eye",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "egress",
                            collapsable: false,
                            uisref: "egress-tabs.egress",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: false,
                        },
                        {
                            title: "Egresos",
                            icon: "call_received",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "egress,newEgress",
                            collapsable: true,
                            uisref: "",
                            menuId: "egresosMenu",
                            hover: false,
                            showXs: false,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Ver egresos",
                                    icon: "remove_red_eye",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "egress",
                                    collapsable: false,
                                    uisref: "egress-tabs.egress",
                                    menuId: "",
                                    hover: false,
                                    showLg: true

                                },
                                {
                                    title: "Capturar egresos",
                                    icon: "payment",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "newEgress",
                                    collapsable: false,
                                    uisref: "egress-tabs.new",
                                    menuId: "",
                                    hover: false,
                                    showXs: false,
                                    showLg: true
                                },
                            ]
                        },
                        {
                            title: "Ingresos",
                            icon: "call_made",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "generatePayment,capturarAdelanto",
                            collapsable: true,
                            uisref: "",
                            menuId: "ingresosMenu",
                            hover: false,
                            showXs: false,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Capturar ingreso",
                                    icon: "payment",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "generatePayment",
                                    collapsable: false,
                                    uisref: "generatePayment",
                                    menuId: "",
                                    hover: false,
                                    showXs: false,
                                    showLg: true
                                },
                                {
                                    title: "Capturar adelanto",
                                    icon: "redo",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "capturarAdelanto",
                                    collapsable: false,
                                    uisref: "advancePayment",
                                    menuId: "",
                                    hover: false,
                                    showXs: false,
                                    showLg: true
                                },
                                {
                                    title: "Otro ingreso",
                                    icon: "monetization_on",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "otroIngreso",
                                    collapsable: false,
                                    uisref: "otherPayment",
                                    menuId: "",
                                    hover: false,
                                    showXs: false,
                                    showLg: true
                                },
                            ]
                        },
                        {
                            title: "Generar cuotas",
                            icon: "turned_in",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "mensual,extraordinary,individual",
                            collapsable: true,
                            uisref: "",
                            menuId: "cuotasMenu",
                            hover: false,
                            showXs: false,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Mensuales",
                                    icon: "remove",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "mensual",
                                    collapsable: false,
                                    uisref: "mensualCharge",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true
                                },
                                {
                                    title: "Extraordinarias",
                                    icon: "remove",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "extraordinary",
                                    collapsable: false,
                                    uisref: "extraordinaryCharge",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true
                                },
                                {
                                    title: "Individual",
                                    icon: "remove",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "individual",
                                    collapsable: false,
                                    uisref: "individualCharge",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,
                                    showLg: true
                                },

                            ]
                        },
                        {
                            title: "Comprobantes de pago",
                            icon: "description",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "paymentProof",
                            collapsable: false,
                            uisref: "paymentProof.pending",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Presupuestos",
                            icon: "trending_up",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "presupuestos",
                            collapsable: false,
                            uisref: "presupuesto",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Bancos",
                            icon: "account_balance",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "bancos",
                            collapsable: false,
                            uisref: "banco",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Saldo de filiales",
                            icon: "library_books",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "balance",
                            collapsable: false,
                            uisref: "houses-balance",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        },
                        {
                            title: "Tabla de cobranza",
                            icon: "grid_on",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "collectionTable",
                            collapsable: false,
                            uisref: "collection-table",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        }
                    ]
                },
                showRounds(),
                showCondoAdministrationNoContability(),
                {
                    title: "Reportes",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: false,
                    hasContability: hasComta,
                    secondaryItems: [
                        {
                            title: "Estado de resultados",
                            icon: "equalizer",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "estadoResultados",
                            collapsable: false,
                            uisref: "resultStates.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Ejec. presupuestaria",
                            icon: "monetization_on",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "budgetExecution",
                            collapsable: false,
                            uisref: "budgetExecution.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Reporte de egresos",
                            icon: "keyboard_backspace",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "reporteGastos",
                            collapsable: false,
                            uisref: "egress-tabs.egress-report",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Reporte de ingresos",
                            icon: "trending_flat",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "reporteIngresos",
                            collapsable: false,
                            uisref: "payment-report",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Cuentas por pagar",
                            icon: "account_balance_wallet",
                            authoritites: "ROLE_MANAGER,ROLE_JD",
                            activeOn: "cuentasPorPagar",
                            collapsable: false,
                            uisref: "egress-to-pay",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Cuotas por cobrar",
                            icon: "list",
                            authoritites: "ROLE_MANAGER,ROLE_JD",
                            activeOn: "reporteCuotasPorPagar",
                            collapsable: false,
                            uisref: "chargesReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Configuración",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: false,
                    hasContability: hasComta,
                    secondaryItems: [
                        {
                            title: "General",
                            icon: "build",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "administrationConfiguration",
                            collapsable: false,
                            uisref: "administration-configuration-detail",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Junta directiva",
                            icon: "record_voice_over",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "jdAccount",
                            collapsable: false,
                            uisref: "junta-directiva-account.new",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Proveedores",
                            icon: "store_mall_directory",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "proovedores",
                            collapsable: false,
                            uisref: "proveedor",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Cuotas",
                            icon: "turned_in",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "configureCharges",
                            collapsable: false,
                            uisref: "configureCharges",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Cuentas de banco",
                            icon: "account_balance",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "bancoConfiguration",
                            collapsable: false,
                            uisref: "banco-configuration",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Activos fijos",
                            icon: "category",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "activosFijos",
                            collapsable: false,
                            uisref: "activos-fijos",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Categoría de egresos",
                            icon: "call_received",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "egressCategories",
                            collapsable: false,
                            uisref: "egress-category",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                    ]
                },
                {
                    title: "Condominio",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Dashboard",
                            icon: "dashboard",
                            authoritites: "ROLE_JD",
                            activeOn: "dashboard",
                            collapsable: false,
                            uisref: "dashboard",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Noticias",
                            icon: "picture_in_picture",
                            authoritites: "ROLE_JD",
                            activeOn: "userNews",
                            collapsable: false,
                            uisref: "announcement-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: []
                        },
                        {
                            title: "Visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_JD",
                            activeOn: "adminVisitors",
                            collapsable: false,
                            uisref: "visitant-admin",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: []
                        },
                        // {
                        //     title: "Seguridad",
                        //     icon: "security",
                        //     authoritites: "ROLE_JD",
                        //     activeOn: "officers,watches",
                        //     collapsable: true,
                        //     uisref: "",
                        //     menuId: "seguridadMenuJD",
                        //     hover: false,
                        //     showXs: true,
                        //     showLg: true,
                        //     thirdItems: [
                        //         {
                        //             title: "Oficiales",
                        //             icon: "people_outline",
                        //             authoritites: "ROLE_JD",
                        //             activeOn: "officers",
                        //             collapsable: false,
                        //             uisref: "officer",
                        //             menuId: "",
                        //             hover: false,
                        //             thirdItems: [],
                        //             showXs: true,
                        //             showLg: true
                        //         },
                        //         {
                        //             title: "Turnos",
                        //             icon: "timer",
                        //             authoritites: "ROLE_JD",
                        //             activeOn: "watches",
                        //             collapsable: false,
                        //             uisref: "turno",
                        //             menuId: "",
                        //             hover: false,
                        //             thirdItems: [],
                        //             showXs: vm.hasWatches,
                        //             showLg: vm.hasWatches
                        //         },
                        //     ]
                        // },
                        {
                            title: "Sugerencias",
                            icon: "feedback",
                            authoritites: "ROLE_JD",
                            activeOn: "complaint",
                            collapsable: false,
                            uisref: "complaint",
                            menuId: "",
                            hover: false,
                            secondaryItems: [],
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Finanzas",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: true,
                    hasContability: hasComta,
                    secondaryItems: [
                        {
                            title: "Presupuestos",
                            icon: "trending_up",
                            authoritites: "ROLE_JD",
                            activeOn: "presupuestos",
                            collapsable: false,
                            uisref: "presupuesto",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Bancos",
                            icon: "account_balance",
                            authoritites: "ROLE_JD",
                            activeOn: "bancos",
                            collapsable: false,
                            uisref: "banco",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Saldo de filiales",
                            icon: "library_books",
                            authoritites: "ROLE_JD",
                            activeOn: "balance",
                            collapsable: false,
                            uisref: "houses-balance",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        },
                        {
                            title: "Tabla de cobranza",
                            icon: "grid_on",
                            authoritites: "ROLE_JD",
                            activeOn: "collectionTable",
                            collapsable: false,
                            uisref: "collection-table",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        }
                    ]
                },
                {
                    title: "Reportes",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: false,
                    hasContability: hasComta,
                    secondaryItems: [
                        {
                            title: "Estado de resultados",
                            icon: "equalizer",
                            authoritites: "ROLE_JD",
                            activeOn: "estadoResultados",
                            collapsable: false,
                            uisref: "resultStates.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Ejec. presupuestaria",
                            icon: "monetization_on",
                            authoritites: "ROLE_JD",
                            activeOn: "budgetExecution",
                            collapsable: false,
                            uisref: "budgetExecution.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Reporte de egresos",
                            icon: "keyboard_backspace",
                            authoritites: "ROLE_JD",
                            activeOn: "reporteGastos",
                            collapsable: false,
                            uisref: "egress-tabs.egress-report",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Reporte de ingresos",
                            icon: "trending_flat",
                            authoritites: "ROLE_JD",
                            activeOn: "reporteIngresos",
                            collapsable: false,
                            uisref: "payment-report",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Cuentas por pagar",
                            icon: "account_balance_wallet",
                            authoritites: "ROLE_JD",
                            activeOn: "cuentasPorPagar",
                            collapsable: false,
                            uisref: "egress-to-pay",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Cuotas por cobrar",
                            icon: "list",
                            authoritites: "ROLE_JD",
                            activeOn: "reporteCuotasPorPagar",
                            collapsable: false,
                            uisref: "chargesReport",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Finanzas",
                    activeOn: "",
                    authoritites: "ROLE_OWNER",
                    showXs: true,
                    hasContability: hasComta && vm.showMenuFinanzas(),
                    secondaryItems: [
                        {
                            title: "Estado de cuenta",
                            icon: "account_balance_wallet",
                            authoritites: "ROLE_USER",
                            activeOn: "residentAccountStatus",
                            collapsable: false,
                            uisref: "accountStatus-residentAccount",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                        {
                            title: "Deudas",
                            icon: "assignment",
                            authoritites: "ROLE_USER",
                            activeOn: "chargesResidentAccount",
                            collapsable: false,
                            uisref: "chargePerHouse-residentAccount",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Pagos",
                            icon: "payment",
                            authoritites: "ROLE_USER",
                            activeOn: "paymentsResidentAccount",
                            collapsable: false,
                            uisref: "paymentsPerHouse-residentAccount",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Comprobantes de pago",
                            icon: "description",
                            authoritites: "ROLE_USER",
                            activeOn: "paymentProof",
                            collapsable: false,
                            uisref: "paymentProof.pending-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Condominio",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Noticias",
                            icon: "picture_in_picture",
                            authoritites: "ROLE_USER",
                            activeOn: "userNews",
                            collapsable: false,
                            uisref: "announcement-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                        {
                            title: "Quejas y sugerencias",
                            icon: "sentiment_very_dissatisfied",
                            authoritites: "ROLE_USER",
                            activeOn: "complaint-user",
                            collapsable: false,
                            uisref: "complaint-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Estado de resultados",
                            icon: "equalizer",
                            authoritites: "ROLE_USER",
                            activeOn: "estadoResultados",
                            collapsable: false,
                            uisref: "resultStates.mensualReport",
                            menuId: "",
                            hasContability: hasComta,
                            hover: false,
                            showXs: vm.showEstadoResultados,
                            showLg: vm.showEstadoResultados,
                        },
                        {
                            title: "Ejec. presupuestaria",
                            icon: "monetization_on",
                            authoritites: "ROLE_USER",
                            activeOn: "budgetExecution",
                            collapsable: false,
                            hasContability: hasComta,
                            uisref: "budgetExecution.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: vm.showEjecPresu,
                            showLg: vm.showEjecPresu,
                        },
                    ],
                },
                {
                    title: "ADMINISTRAR MI FILIAL",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: vm.showMenuResident(),
                    secondaryItems: [
                        {
                            title: "Usuarios",
                            icon: "group",
                            authoritites: "ROLE_USER",
                            activeOn: "residentsHouses",
                            collapsable: false,
                            uisref: "residentByHouse",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Véhiculos",
                            icon: "directions_car",
                            authoritites: "ROLE_USER",
                            activeOn: "vehiculesHouses",
                            collapsable: false,
                            uisref: "vehiculeByHouse",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Bitácora de visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_USER",
                            activeOn: "residentsVisitors",
                            collapsable: false,
                            uisref: "visitant",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Visitantes invitados",
                            icon: "account_circle",
                            authoritites: "ROLE_USER",
                            activeOn: "residentsInvitedVisitors",
                            collapsable: false,
                            uisref: "visitant-invited-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Clave de seguridad",
                            icon: "vpn_key",
                            authoritites: "ROLE_USER",
                            activeOn: "keysConfiguration",
                            collapsable: false,
                            uisref: "keysConfiguration",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        }
                    ]
                },
                {
                    title: "Reportar",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: vm.showMenuResident(),
                    secondaryItems: [
                        {
                            title: "Visitante",
                            icon: "perm_identity",
                            authoritites: "ROLE_USER",
                            activeOn: "reportInvitation",
                            collapsable: false,
                            uisref: "visitant-invited-user.new",
                            menuId: "",
                            hover: false,
                            showLg: true,
                            showXs: true
                        },
                        // {
                        //     title: "Reunión o fiesta",
                        //     icon: "group_add",
                        //     authoritites: "ROLE_USER",
                        //     activeOn: "reportInvitationList",
                        //     collapsable: false,
                        //     uisref: "visitant-invited-user.new-list",
                        //     menuId: "",
                        //     hover: false,
                        //     showLg: true,
                        //     showXs: true
                        // },
                        {
                            title: "Nota a oficial",
                            icon: "note",
                            authoritites: "ROLE_USER",
                            activeOn: "reportHomeService",
                            collapsable: false,
                            uisref: "noteNew",
                            menuId: "",
                            hover: false,
                            showLg: true,
                            showXs: true
                        },
                        {
                            title: "Reportar emergencia",
                            icon: "local_hospital",
                            authoritites: "ROLE_USER",
                            activeOn: "reportemergencyactive",
                            collapsable: false,
                            uisref: "emergency.new",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "ÁREAS COMUNES",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: vm.bookCommonArea && vm.showMenuResident(),
                    secondaryItems: [
                        {
                            title: "Ver todas",
                            icon: "view_agenda",
                            authoritites: "ROLE_USER",
                            activeOn: "common-area-resident-account,reservationCalendarResidentView",
                            collapsable: false,
                            uisref: "common-area-resident-account",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Reservar",
                            icon: "event_available",
                            authoritites: "ROLE_USER",
                            activeOn: "reservationDialogResidentView",
                            collapsable: false,
                            uisref: "common-area-reservation-resident-view",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Mis reservaciones",
                            icon: "view_comfy",
                            authoritites: "ROLE_USER",
                            activeOn: "allReservationsResidentsView",
                            collapsable: false,
                            uisref: "common-area-all-reservations-resident-view",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Áreas comunes",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Ver áreas",
                            icon: "view_agenda",
                            authoritites: "ROLE_JD",
                            activeOn: "reservationAdministration",
                            collapsable: false,
                            uisref: "common-area-administration.common-area",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Reservaciones",
                            icon: "view_comfy",
                            authoritites: "ROLE_JD",
                            activeOn: "reservations",
                            collapsable: false,
                            uisref: "common-area-administration.common-area-reservations",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Ver calendario",
                            icon: "event_note",
                            authoritites: "ROLE_JD",
                            activeOn: "generaCalendar",
                            collapsable: false,
                            uisref: "common-area-administration.general-reservation-calendar",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Devoluciones",
                            icon: "payment",
                            authoritites: "ROLE_JD",
                            activeOn: "devolutions",
                            collapsable: false,
                            uisref: "common-area-devolution-administration.pending-devolution",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                    ]
                },
                {
                    title: "Macro condominio",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER_MACRO",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Administradores",
                            icon: "supervised_user_circle",
                            authoritites: "ROLE_MANAGER_MACRO",
                            activeOn: "adminsByCompanyMacro",
                            collapsable: false,
                            uisref: "admin-info-by-company",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: []
                        },
                        {
                            title: "Bitácora visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_MANAGER_MACRO",
                            activeOn: "adminMacroVisitors",
                            collapsable: false,
                            uisref: "macro-visit",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: []
                        },

                    ]
                },
                {
                    title: "Soporte",
                    icon: "contact_support",
                    authoritites: "ROLE_RH,ROLE_MANAGER,ROLE_USER,ROLE_JD,ROLE_MANAGER_MACRO",
                    activeOn: "soporte",
                    collapsable: false,
                    uisref: "soporte",
                    menuId: "",
                    hover: false,
                    showXs: true,
                    showLg: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Soporte",
                            icon: "live_help",
                            authoritites: "ROLE_RH,ROLE_MANAGER,ROLE_USER,ROLE_JD,ROLE_MANAGER_MACRO",
                            activeOn: "soporte-user",
                            collapsable: false,
                            uisref: "soporte-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            secondItems: []
                        },
                    ]
                },
            ];
            showTheOneThatsActive()
        }


        vm.loadCompanyConfig = function () {
            vm.hasContability = false;
            Principal.identity().then(function (account) {
                vm.account = account;
                MultiCompany.getCurrentUserCompany().then(function (data) {
                    vm.chargeMenu(vm.hasContability);
                    var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                    vm.hasWatches = false;
                    vm.showEstadoResultados = companyConfig.showEstadoResultados;
                    vm.showEjecPresu = companyConfig.showEjecPresu;
                    vm.bookCommonArea = companyConfig.bookCommonArea;
                    vm.hasRounds = companyConfig.hasRounds;
                    $rootScope.currency = companyConfig.currency;
                    if (companyConfig == "admin") {
                        vm.hasContability = false;
                    } else {
                        if (companyConfig.hasContability == 1) {
                            vm.hasContability = true;
                        } else {
                            vm.hasContability = false;
                        }
                    }
                    $rootScope.companyUser = data;
                    vm.chargeMenu(vm.hasContability);
                });
            });
        };

        vm.showSecondItem = function (secondItem) {
            var secondItemsToHideIfHasNoContability = ["Devoluciones", "Ejec. presupuestaria", 'Estado de resultados'];
            var items = secondItemsToHideIfHasNoContability;
            var ocultar = 0;
            for (var i = 0; i < items.length; i++) {
                if (secondItem != null) {
                    if (secondItem.title == items[i]) {
                        ocultar++;
                    }
                }
            }
            if (ocultar > 0 && !vm.hasContability) {
                return false;
            } else {
                return true;
            }
        };
        vm.loadCompanyConfig();
        $scope.$watch(function () {
            return $localStorage.companiesConfig;
        }, function (newCodes, oldCodes) {
            vm.loadCompanyConfig(globalCompany.getId())
        });


        vm.defineStyleSecondButton = function (item) {
            if (item != null) {
                if (item.hover) {
                    return vm.colorsMenu.secondButtonHover;
                }
                if (this.defineActive(item) == true) {
                    return vm.colorsMenu.secondButtonActive
                } else {
                    return vm.colorsMenu.secondButton
                }
            } else {
                return "";
            }
        };
        vm.defineStyleThirdButton = function (item) {
            if (item.hover) {
                return vm.colorsMenu.thirdButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.thirdButtonActive
            } else {
                return vm.colorsMenu.thirdButton
            }
        };
        vm.defineStyleMainButton = function (item) {
            if (item.hover) {
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

        vm.collapseAllOther = function (item, fatherItem) {
            if (fatherItem != null) {
                $(".collapse:not(#" + item.menuId + ",#" + fatherItem.menuId + ")").collapse('hide');
            } else {
                $(".collapse:not(#" + item.menuId + ")").collapse('hide');

            }
        };


        function showTheOneThatsActive() {
            for (var i = 0; i < vm.menu.length; i++) {
                if (vm.menu[i]) {
                    if (vm.menu[i].secondaryItems != null) {
                        for (var j = 0; j < vm.menu[i].secondaryItems.length; j++) {
                            var secondaryItem = vm.menu[i].secondaryItems[j];
                            if (secondaryItem != null) {
                                if (secondaryItem.collapsable) {
                                    for (var k = 0; k < secondaryItem.thirdItems.length; k++) {
                                        if (secondaryItem.thirdItems[k].activeOn.includes($rootScope.active)) {
                                            $("#" + secondaryItem.menuId).collapse('show');
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        $rootScope.isAuthenticated = Principal.isAuthenticated;
        vm.isAuthenticated = Principal.isAuthenticated;
        $rootScope.toggleLeft = buildToggler('left');


        function buildToggler(componentId) {
            return function () {
                $mdSidenav(componentId).toggle();
            };
        }

        function showCondoAdministrationNoContability() {
            if (vm.hasContability == false) {
                return {
                    title: "Administración",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Usuarios",
                            icon: "group",
                            authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                            activeOn: "residents",
                            collapsable: false,
                            uisref: "resident",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },

                        {
                            title: "Vehículos",
                            icon: "directions_car",
                            authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                            activeOn: "vehicules",
                            collapsable: false,
                            uisref: "vehicule",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                            activeOn: "adminVisitors",
                            collapsable: false,
                            uisref: "visitant-admin",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Bitácora de acciones",
                            icon: "chrome_reader_mode",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "bitacoraAcciones",
                            collapsable: false,
                            uisref: "bitacora-acciones",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                    ]
                };
            } else {
                return {};
            }
        }

        function showRounds() {
            if (vm.hasRounds == true) {
                return {
                    title: "ADITUM RONDAS",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER,ROLE_JD",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Consultar rondas",
                            icon: "directions_walk",
                            authoritites: "ROLE_MANAGER,ROLE_JD",
                            activeOn: "rounds,round-detail",
                            collapsable: false,
                            uisref: "rounds",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                    ]
                };
            } else {
                return {};
            }
        }

        function showCondoAdministrationContability() {
            if (vm.hasContability === true) {
                return {
                    title: "Administración",
                    icon: "location_city",
                    authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                    activeOn: "residents,vehicules,adminVisitors",
                    collapsable: true,
                    uisref: "",
                    menuId: "administracionMenu",
                    hover: false,
                    showXs: true,
                    showLg: true,
                    thirdItems: [
                        {
                            title: "Usuarios",
                            icon: "group",
                            authoritites: "ROLE_MANAGER,ROLE_MANAGER_MACRO",
                            activeOn: "residents",
                            collapsable: false,
                            uisref: "resident",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },

                        {
                            title: "Vehículos",
                            icon: "directions_car",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "vehicules",
                            collapsable: false,
                            uisref: "vehicule",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "adminVisitors",
                            collapsable: false,
                            uisref: "visitant-admin",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                        {
                            title: "Bitácora de acciones",
                            icon: "chrome_reader_mode",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "bitacoraAcciones",
                            collapsable: false,
                            uisref: "bitacora-acciones",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true
                        },
                    ]
                };
            } else {
                return null;
            }
        }

        function unsubscribe() {
            WSDeleteEntity.unsubscribe(globalCompany.getId());
            WSEmergency.unsubscribe(globalCompany.getId());
            WSHouse.unsubscribe(globalCompany.getId());
            WSResident.unsubscribe(globalCompany.getId());
            WSVehicle.unsubscribe(globalCompany.getId());
            WSNote.unsubscribe(globalCompany.getId());
            WSVisitor.unsubscribe(globalCompany.getId());
            WSOfficer.unsubscribe(globalCompany.getId());
        }

        function logout() {
            collapseNavbar();
            Principal.identity().then(function (account) {
                switch (account.authorities[0]) {
                    case "ROLE_OFFICER":
                        $timeout.cancel($rootScope.timerAd);
                        unsubscribe();
                        break;
                    case "ROLE_OFFICER_MACRO":
                        $timeout.cancel($rootScope.timerAd);
                        unsubscribe();
                        break;
                }
            });
            Auth.logout();
            $localStorage.houseSelected = undefined;
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $localStorage.companyName = undefined;
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
            var encryptedId = CommonMethods.encryptIdUrl(globalCompany.getId())
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
                            if ($localStorage.companyId == undefined) {
                                $rootScope.companyUser = data;
                                $rootScope.companyUser.companyId = data.companies[0].id;
                                $localStorage.companyId = CommonMethods.encryptIdUrl(data.companies[0].id);
                            }
                            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                            $rootScope.currency = companyConfig.currency;
                            Company.get({id: globalCompany.getId()}, function (condo) {
                                vm.contextLiving = condo.name;
                                $rootScope.companyName = condo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = data.image_url;
                                if (data.enabled == 0) {
                                    logout();
                                }
                            });
                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_MANAGER_MACRO":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            if ($localStorage.macroCompanyId == undefined) {
                                $rootScope.companyUser = data;
                                $rootScope.companyUser.companyId = data.macroCondominiumId;
                                $localStorage.macroCompanyId = CommonMethods.encryptIdUrl(data.macroCondominiumId);
                            }
                            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                            $rootScope.currency = companyConfig.currency;
                            MacroCondominium.get({id: data.macroCondominiumId}, function (macroCondo) {
                                vm.contextLiving = macroCondo.name;
                                $rootScope.companyName = macroCondo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = data.imageUrl;
                                $rootScope.companyUser.companies = macroCondo.companies;
                                if (data.enabled == 0) {
                                    logout();
                                }
                            });
                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_OFFICER_MACRO":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            $localStorage.companyId = CommonMethods.encryptIdUrl(data.macroCondominiumId);
                            if (data != null) {
                                vm.contextLiving = $rootScope.companyUser.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                            }
                            MacroCondominium.get({id: parseInt(globalCompany.getId())}, function (condo) {
                                if (!condo.enabled || !data.enabled) {
                                    logout();
                                }
                            })
                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_OFFICER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
                            if (data != null) {
                                vm.contextLiving = $rootScope.companyUser.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                            }
                            Company.get({id: parseInt(globalCompany.getId())}, function (condo) {
                                vm.contextLiving = condo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                if (condo.active == 0 || data.enable == 0) {
                                    logout();
                                }
                            })
                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_USER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            if (data.houseId) {
                                House.get({id: parseInt(data.houseId)}, function (house) {
                                    $rootScope.filialNumber = house.housenumber;
                                    $rootScope.houseSelected = house;
                                    $localStorage.houseId = CommonMethods.encryptIdUrl(house.id);
                                });
                            } else {
                                if (data.houses.length > 1 && !$rootScope.houseSelected) {
                                    $rootScope.houseSelected = data.houses[0];
                                    $localStorage.houseId = CommonMethods.encryptIdUrl(data.houses[0].id);
                                }
                            }
                            $rootScope.contextLiving = vm.contextLiving;
                            $rootScope.hideFilial = false;
                            $rootScope.filialNumber = data.houseClean.housenumber;
                            $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
                            $rootScope.currentUserImage = data.image_url;
                            $rootScope.companyUser = data;
                            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                            $rootScope.currency = companyConfig.currency;
                            if (companyConfig == "admin") {
                                vm.hasContability = false;
                            } else {
                                if (companyConfig.hasContability == 1) {
                                    vm.hasContability = true;
                                } else {
                                    vm.hasContability = false;
                                }
                            }
                            Company.get({id: parseInt(globalCompany.getId())}, function (condo) {
                                vm.contextLiving = condo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                if (condo.active == 0 || data.enabled == 0) {
                                    logout();
                                }
                            })
                            // })
                        });
                        break;
                    case "ROLE_OWNER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            $rootScope.contextLiving = vm.contextLiving;
                            $rootScope.hideFilial = false;
                            $rootScope.filialNumber = data.house.housenumber;
                            $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
                            $rootScope.currentUserImage = data.image_url;
                            $rootScope.companyUser = data;
                            if(data.houses.length<=1){
                                House.get({id: parseInt(data.houseId)}, function (house) {
                                    $rootScope.houseSelected = house;
                                    $localStorage.houseId = CommonMethods.encryptIdUrl(data.houseId)
                                })
                            }
                            if (data.houses.length > 1 && !$rootScope.houseSelected) {
                                $rootScope.houseSelected = data.houses[0];
                                $localStorage.houseId = CommonMethods.encryptIdUrl(data.houses[0].id);
                            }
                            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
                            $rootScope.currency = companyConfig.currency;

                            if (companyConfig == "admin") {
                                vm.hasContability = false;
                            } else {
                                if (companyConfig.hasContability == 1) {
                                    vm.hasContability = true;
                                } else {
                                    vm.hasContability = false;
                                }
                            }
                            Company.get({id: parseInt(globalCompany.getId())}, function (condo) {
                                vm.contextLiving = condo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                if (condo.active == 0 || data.enabled == 0) {
                                    logout();
                                }
                            })
                            // })
                        });
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
                    case "ROLE_JD":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            if ($localStorage.companyId == undefined) {
                                $rootScope.companyUser = data;
                                $rootScope.companyUser.companyId = data.companies[0].id;
                                $localStorage.companyId = CommonMethods.encryptIdUrl(data.companies[0].id);
                            }
                            Company.get({id: globalCompany.getId()}, function (condo) {
                                vm.contextLiving = condo.name;
                                $rootScope.companyName = condo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                                $rootScope.companyUser.name = "Junta";
                                $rootScope.companyUser.lastname = "Directiva";
                                if (data.enabled == 0) {
                                    logout();
                                }
                            });

                            $rootScope.hideFilial = true;
                        });
                        break;
                }
            })
        }

        Principal.identity().then(function (account) {
            if (account !== null) {
                $rootScope.companyUser = companyUser;
                vm.getAcount();
            }
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


        vm.selectCompany = function (company) {
            $localStorage.companyId = CommonMethods.encryptIdUrl(company.id);
            $localStorage.houseSelected = undefined;
            $localStorage.infoHouseNumber = undefined;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.getAcount();
                    vm.loadCompanyConfig();
                    $state.go("dashboard", {}, {reload: true});
                })
            }, 300);
        };
        vm.selectHouse = function (house) {
            $localStorage.houseSelected = house;
            $localStorage.infoHouseNumber = house.housenumber;
            $rootScope.houseSelected = house;
            $localStorage.houseId = CommonMethods.encryptIdUrl(house.id);
            $rootScope.companyUser.houseId = house.id;
            setTimeout(function () {
                $scope.$apply(function () {
                    // vm.loadCompanyConfig();
                    $state.go("announcement-user", {}, {reload: true});
                })
            }, 300);
        };

        vm.defineSelectCompanyColor = function (company) {
            if (company.id == globalCompany.getId()) {
                return vm.colorsMenu.secondButtonActive;
            } else {
                return vm.colorsMenu.secondButton;
            }
        };
        vm.defineSelectHouseColor = function (house) {
            if (house.id == globalCompany.getHouseId()) {
                return vm.colorsMenu.secondButtonActive;
            } else {
                return vm.colorsMenu.secondButton;
            }
        };
//        $scope.$on('$destroy', subChangeState);
        $scope.$on('$destroy', subLogin);

        vm.findBootstrapEnvironment = function () {
            var envs = ['xs', 'sm', 'md', 'lg'];

            var $el = $('<div>');
            $el.appendTo($('body'));

            for (var i = envs.length - 1; i >= 0; i--) {
                var env = envs[i];

                $el.addClass('hidden-' + env);
                if ($el.is(':hidden')) {
                    $el.remove();
                    return env;
                }
            }
        }

        vm.isScreenSizeSmall = function () {
            var envs = ['xs', 'sm', 'md'];
            var e = 0;
            for (var i = 0; i < envs.length; i++) {
                if (envs[i] === vm.findBootstrapEnvironment()) {
                    e++;
                }
            }
            if (e > 0) {
                return true;
            }
            return false;
        }
    }
})
();
