var app = angular.module('app');

app.service('utils', function() {
    return {

        dateFromObjectId: function (objectId) {
            return new Date(parseInt(objectId.substring(0, 8), 16) * 1000);
        }

    }

})

app.directive('dirGlobal', function(){
  return {
    restrict: 'E',
    templateUrl: 'app/global/global.html',
    controller: function($scope, $http, $location, $sce, $rootScope, User, EventHandler){
        $scope.AppName = 'Ecoturismo a donde vallas';
        $scope.view = "CupiDudas"; /*CupiDudas, Usuarios*/

        //All functions in this module are exported to $rootScope.utils to be accessed from all modules
        $rootScope.utils = {
            isMobile: isMobile()
        };

        $scope.getUserName = User.getUserName;
        $scope.logout = User.logout;


        EventHandler.addErrorListener(function(error, status){
            var config = {
                title: "Mensaje de error",
                message: error.message,
                options: [ {
                    name: "Aceptar",
                    btnClass: "JM_aceptButton"
                }]
            }
            $rootScope.$broadcast('pop-up', config);
        })

        
        
        /* Used to detect whether the users browser is an mobile browser */
        function isMobile() {
            if (sessionStorage.desktop) /* desktop storage */
                return false;
            else if (localStorage.mobile) /* mobile storage */
                return true;

            /* alternative */
            var mobile = ['iphone','ipad','android','blackberry','nokia','opera mini','windows mobile','windows phone','iemobile']; 
            for (var i in mobile) 
                if (navigator.userAgent.toLowerCase().indexOf(mobile[i].toLowerCase()) > 0) 
                    return true;

                /* nothing found.. assume desktop */
                return false;
            }
        }
    }
})









