(function (ng) {
    var appModule = angular.module('mainApp');

    appModule.factory('httpInterceptor', ['$q', '$rootScope', function ($q, $rootScope) {

            //Se define el arreglo de alerts que va almacenar la alerta que se mostrará dependiendo del error
            $rootScope.alerts = [];
            //Se define el closeAlert en rootScope para que todas las vistas lo reutilicen cuando salga un alert
            $rootScope.closeAlert = function (index) {
                $rootScope.alerts.splice(index, 1);
            };

            var interceptor = {
                response: function (response) {
                    return response;
                },
                responseError: function (rejection) {

                    var status = rejection.status;

                    function showError(message, type) {

                        var types = ["info", "danger", "warning", "success"];
                        if (types.some(function (rc) {
                            return type === rc;
                        })) {
                            $rootScope.alerts.push({type: type, msg: message});
                        }
                    }

                    showError(rejection.data, "danger");
                    return $q.reject(rejection);
                }
            };
            return interceptor;
        }]);

    appModule.config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push('httpInterceptor');
        }]);
})(window.angular);