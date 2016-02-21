/* options structure:

var config = {
	title: "Escriba su color favorito.",
	message: "Por favor escriba su color favorito en minúscula",
	infoDescriptor: (see information.js to understand the format of this atribute),
	options: [{
		name: "Cancel",
		btnClass: "JM_cancelButton"
	},{
		name: "Ok",
		btnClass: "JM_aceptButton",
		callback: function(object){ //object will be an object defined by infoData atribute. 
			console.log(object); // use the object as you need
		} 
	}]
}

*/

app.directive('dirPopUps', function(){
  return {
    restrict: 'E',
    scope:{},
    templateUrl: 'lib/auto-display/pop-ups.html',
    controller: function($rootScope, $scope){
    	$scope.edit = true;
    	$rootScope.$on('pop-up', function(event, config){
    		$scope.object = {};
    		$scope.config = config;
    		$scope.showDialog = true;
    	})
    	$scope.selectOption = function(option){
    		 $scope.showDialog=false; 
    		 if(option.callback){
    		 	option.callback($scope.object)
    		 }
    	}
    }
  }
})