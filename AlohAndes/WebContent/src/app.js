(function (ng) {
    var app = angular.module('app', [
        'ui.router'
        ]);

    app.config(function($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise('/home');

        $stateProvider.state('productos', {
            url: '/productos/{filtro:int}/{parametro:string}/list',
            params: {
                filtro: null,
                parametro: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/productos.html',
                    controller: 'productosCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });


        $stateProvider.state('filterProducto', {
            url: 'productos/',
            views: {
                'filterView':{
                    templateUrl: 'src/filtroP.html',
                    controller: 'productosCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });

        $stateProvider.state('rentabilidad', {
            url: '/rentabilidad/{filtro:int}/list',
            params: {
                filtro: null,
                initDate: null,
                endDate: null
            },
            views: {
                'mainView': {
                    templateUrl: 'src/rentabilidad.html',
                    controller: 'rentabilidadCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });


        $stateProvider.state('filterRentabilidad', {
            url: 'rentabilidad/',
            views: {
                'filterView':{
                    templateUrl: 'src/filtroR.html',
                    controller: 'rentabilidadCtrl',
                    controllerAs: 'ctrl'
                }
            }
        });


    });

    app.controller('productosCtrl', ['$scope', '$state', '$stateParams', '$http', function ($scope, $state, $stateParams, $http) {

        $scope.opciones = [
        {number: "1", name: "ID Restaurante"},
        {number: "2", name: "Categoria"},
        {number: "3", name: "Rango de Precios"},
        {number: "4", name: "ID Orden"}
        ];

        $scope.buscar = function () {
            console.log('filtro: ' + $scope.selectedOption.number + " -> " + $scope.selectedOption.name);
            console.log('parametro: ' + $scope.parametro);
            $scope.dat = {filtro: $scope.selectedOption.number, parametro: $scope.parametro};
            console.log('scopedat -> ' + $scope.dat);
            $state.go('productos', {filtro: $scope.selectedOption.number, parametro: $scope.parametro}, {reload : true});
        };

        console.log('filtro antes if: ' + $state.params.filtro);
        console.log('parametro antes if: ' + $state.params.parametro);
        console.log('params antes if: ' + $state.params);
        console.log('stateparams: ' + $stateParams);
        console.log('stateparams values: ' + $stateParams);
        if ($state.params.filtro !== undefined  && $state.params.parametro !== undefined) {
            if( $state.params.filtro !== null && $state.params.parametro !== null){
                console.log('entró');
                var ruta = "http://localhost:8081/RotondAndes/rest/admin/productos?filtro=" + $state.params.filtro + "&parametro=" + $state.params.parametro;
                $http.get(ruta).then(function (response) {
                    $scope.productos = response.data;
                });
            }else {
                console.log('paila interno');
            }
        } else {
            console.log('paila paila');
        }

    }]);



    app.controller('rentabilidadCtrl', ['$scope', '$state', '$stateParams', '$http', '$filter', function ($scope, $state, $stateParams, $http, $filter) {

        $scope.opciones = [
        {number: "1", name: "CATEGORIA"},
        {number: "2", name: "PRODUCTO"},
        {number: "3", name: "ORDEN"}
        ];

        $scope.buscar = function () {
            var iFecha = $filter('date')($scope.initFecha, "dd/MM/yyyy");            
            var eFecha = $filter('date')($scope.endFecha, "dd/MM/yyyy"); 
            console.log('iFecha: ' + iFecha);
            console.log('eFecha: ' + eFecha);
            $state.go('rentabilidad', {filtro: $scope.selectedOption.number, initDate: iFecha, endDate: eFecha}, {reload : true});
        };

        if ($state.params.filtro !== undefined  && $state.params.initDate !== undefined && $state.params.endDate !== undefined) {
            if( $state.params.filtro !== null && $state.params.initDate !== null && $state.params.endDate !== null){
                console.log('entró');
                var ruta = "http://localhost:8081/RotondAndes/rest/admin/rentabilidad?filtro=" + $state.params.filtro + "&initDate=" + $state.params.initDate + "&endDate=" + $state.params.endDate;
                $http.get(ruta).then(function (response) {
                    $scope.respuestas = response.data;
                });
            }else {
                console.log('paila interno');
            }
        } else {
            console.log('paila paila');
        }

    }]);
})(window.angular);
