/*
self.atributos = [{
		displayHeader: 'Estado',
		columnAtrubute: 'Estado',
		show: true,
		editable: false,
		editor: 'text'
	}, {
		displayHeader: 'Fecha',
		columnAtrubute: 'Fecha',
		show: true,
		editable: false,
		editor: 'date'
	}, {
		displayHeader: 'Hora',
		columnAtrubute: 'Hora',
		show: true,
		editable: false,
		editor: 'number'
	}, {
		displayHeader: 'FechaSolicitud',
		columnAtrubute: 'FechaSolicitud',
		show: true,
		editable: false,
		editor: 'date'
	}];
*/
angular.module('autoDisplay')
	.directive('dirInformation', function() {
		return {
			restrict: 'E',
			scope: {
				objeto: '=',
				atributos: '=',
				editMode: '='
			},
			templateUrl: 'lib/auto-display/information.html',
			controller: function($scope) {
				$scope.getValueToDisplay= function(atributo){
					if(atributo.modifier){
						console.log("getValueToDisplay", atributo.modifier(atributo.columnAtrubute))
						return atributo.modifier($scope.objeto[atributo.columnAtrubute]);
					}else{
						return atributo.columnAtrubute;
					}
				}
			}
		}
	})
	.directive('dirJmDatepicker', function() {
		return {
			restrict: 'E',
			scope: {
				dateFieldName: "@",
				objeto: "=",
				editable: "="
			},
			templateUrl: 'lib/auto-display/datePicker.html',
			controller: function($scope) {

				$scope.today = function() {
					$scope.objeto[$scope.dateFieldName] = new Date();
				};


				$scope.clear = function() {
					$scope.objeto[$scope.dateFieldName] = null;
				};

				// Disable weekend selection
				$scope.disabled = function(date, mode) {
					return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
				};

				$scope.toggleMin = function() {
					$scope.minDate = $scope.minDate ? null : new Date();
				};
				$scope.toggleMin();
				$scope.maxDate = new Date(2020, 5, 22);

				$scope.open = function($event) {
					$scope.status.opened = true;
				};

				$scope.dateOptions = {
					formatYear: 'yy',
					startingDay: 1
				};

				$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
				$scope.format = $scope.formats[0];

				$scope.status = {
					opened: false
				};

				var tomorrow = new Date();
				tomorrow.setDate(tomorrow.getDate() + 1);
				var afterTomorrow = new Date();
				afterTomorrow.setDate(tomorrow.getDate() + 2);
				$scope.events =
					[{
						date: tomorrow,
						status: 'full'
					}, {
						date: afterTomorrow,
						status: 'partially'
					}];

				$scope.getDayClass = function(date, mode) {
					if (mode === 'day') {
						var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

						for (var i = 0; i < $scope.events.length; i++) {
							var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

							if (dayToCheck === currentDay) {
								return $scope.events[i].status;
							}
						}
					}

					return '';
				};

			}
		}
	})