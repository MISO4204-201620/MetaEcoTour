/* 

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

*/



app.directive('dirInfoOptions', function() {
    return {
        restrict: 'E',
        scope: {
            title: "@",
            message: "@",
            object: "=",
            infoDescriptor: "=",
            options: "=",
            editable: "@"
        },
        templateUrl: 'lib/auto-display/info-options.html',
        controller: function($scope) {

            
            $scope.selectOption = function(option) {
                if (option.callback) {
                    option.callback($scope.object)
                }
            }
        }
    }
})