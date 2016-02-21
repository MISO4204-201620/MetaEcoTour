angular.module('autoDisplay')
	.directive('dirJmList', function() {
		return {
			restrict: 'E',
			templateUrl: 'lib/auto-display/list.html',
			scope: {
				lista: '=',
				expand: '=',
				setExpand: '=',
				selectedItem: '=',
				setSelectedItem: '=',
				showEditOption: "=",
				showAddOption: "=",
				addItem: "=",
				deleteItem: '=',
				titulo: "@",
				columns: "="
			},
			controller: function($scope) {
				$scope.editList = false;
				$scope.orederPredicate = '';
				$scope.reverseOrder = false;
				elegirColumnas();
				angular.element(window).resize(function() {
					$scope.$apply(function() {
						elegirColumnas();
					});
				})
				$scope.filterObject = {}

				for (var i = 0; i < $scope.columns.length; i++) {
					$scope.filterObject[$scope.columns[i].displayHeader] = null;
				};
				this.crearFiltro = function(nombreFiltro, valor) {
					$scope.filterObject[nombreFiltro] = valor;
				}
				this.eliminarFiltro = function(nombreFiltro) {
					$scope.filterObject[nombreFiltro] = null;
				}
				this.orderBy = function(predicate) {
					if ($scope.orederPredicate === predicate) {
						$scope.reverseOrder = !$scope.reverseOrder;
					} else {
						$scope.orederPredicate = predicate;
						$scope.reverseOrder = false;
					}
				}
				$scope.displayText = function(item, column) {
					var atributeValue = item[column.columnAtrubute];
					if (atributeValue) {
						if (column.modifier) {
							return column.modifier(atributeValue);
						} else {
							return atributeValue;
						}
					}
					return null;
				}
				$scope.mostrarSegunFiltros = function(item) {
					var mostrar = true;
					for (var i = 0; mostrar && i < $scope.columns.length; i++) {
						var column = $scope.columns[i];
						var filter = $scope.filterObject[column.displayHeader];
						if (filter) {
							var textToDisplay = $scope.displayText(item, column)
							if (textToDisplay) {
								mostrar = (textToDisplay + '').search(new RegExp(filter, "i")) >= 0;
							} else {
								mostrar = false;
							}
							console.log("mostrarSegunFiltros", column.columnAtrubute, filter, textToDisplay, mostrar)
						}
					};
					return mostrar;
				}
				$scope.showColumn = function(index, column, expand) {
					if (expand === 'both' && index !== 0) {
						return false;
					}
					return column.show && column.responsiveShow;
				}

				function elegirColumnas() {
					var minPixels = 250;
					var totalWidth = angular.element(window).width();

					var numColumnsToDisplay = totalWidth / minPixels;
					var numColumns = $scope.columns.length;
					for (var i = 0; i < numColumns; i++) {
						$scope.columns[i].responsiveShow = (i < numColumnsToDisplay);
					};
				}
			}
		};
	})
	.directive('dirTableHeaderColumn', function() {
		return {
			restrict: 'A',
			scope: {
				header: '@headerName',
				atribute: '@columnAtribute'
			},
			templateUrl: 'lib/auto-display/tableHeaderColumn.html',
			require: '^dirJmList',
			link: function(scope, elem, attrs, jmListCtrl) {
				scope.$watch('filter', function(newValue, oldValue) {
					jmListCtrl.crearFiltro(scope.header, newValue);
				});
				scope.eliminarFiltro = function() {
					scope.previousFilter = scope.filter;
					scope.filter = null;
					jmListCtrl.eliminarFiltro(scope.header);
				}
				scope.restaurarFiltro = function() {
					scope.filter = scope.previousFilter;

				}
				scope.orderBy = function(predicate) {
					jmListCtrl.orderBy(predicate)
				}
			}
		};
	});