var checkLoggedin = function($q, $timeout, $http, $location, $rootScope, User){ 
	var deferred = $q.defer(); 
	$http.get('/loggedin').success(function(user){ 
		if (user === '0') {
			$timeout(function(){deferred.reject();}, 0);
			if($location.url() !== '/'){
				$location.url('/');
			} 
		}else { 
			$timeout(deferred.resolve, 0);
			User.setUser(user);
		} 
	});
}

var app = angular.module('app', ['ngRoute', 'autoDisplay'])

.config(function($routeProvider, $httpProvider) {
	moment.locale('es');
	$routeProvider
	.when('/', {
		controller:'LoginCtrl',
		templateUrl:'app/login/login.html',
		resolve: { 
			loggedin: checkLoggedin 
		}
	})
	.when('/admin', {
		controller:'AdminCtrl',
		templateUrl:'app/admin/admin.html',
		//resolve: {
		//	loggedin: checkLoggedin
		//}
	})
	.when('/proveedor', {
		controller:'TutoresCtrl',
		templateUrl:'app/tutores/tutores.html',
		//resolve: {
		//	loggedin: checkLoggedin
		//}
	})
	.when('/usuario', {
		controller:'EstudiantesCtrl',
		templateUrl:'app/estudiantes/estudiantes.html',
		//resolve: {
		//	loggedin: checkLoggedin
		//}
	})
	.otherwise({
		controller:'LoginCtrl',
		templateUrl:'app/login/login.html',
		resolve: { 
			loggedin: checkLoggedin 
		}
	});
})

