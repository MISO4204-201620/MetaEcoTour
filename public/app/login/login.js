var app = angular.module('app');

app.controller('LoginCtrl', function( $scope, $http, $location, $route, User, EventHandler) {
	
	User.checkLoggedin();

	$scope.login = User.login;

	
});