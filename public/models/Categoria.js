angular.module('app').service('Categoria', citaService);

function citaService($location, $http, EventHandler) {
	var self = this;
	var categorias = null;
	var listeners = [];




	self.atributos = [{
		displayHeader: 'Nombre',
		columnAtrubute: 'nombre',
		show: true,
		editable: true,
		editor: 'text'
	}];

    self.watch = function(callback) {
        listeners.push(callback);
        if (categorias) {
            callback(categorias);
        } else {
            refresh();
        }
    }

	function refresh() {
		$http.get('api/categorias/')
			.success(function(data, status, headers, config) {
                categorias = data;
				for (var i = listeners.length - 1; i >= 0; i--) {
                    listeners[i](categorias);
				};
			}).error(EventHandler.error);
	}
    self.agregarCategoria = function (categoriaNueva, callback) {
        console.log("Nueva categoría: ", categoriaNueva)
        $http.post('api/categorias/', categoriaNueva)
            .success(function(data, status, headers, config) {
                refresh();
                if(callback) callback(data);
            })
            .error(EventHandler.error);
    }
    self.deleteCategoria = function (categoria, callback){
        $http.delete('api/categorias/'+ categoria.id)
            .success(function(data, status, headers, config) {
                refresh();
                if(callback) callback(data);
            })
            .error(EventHandler.error);
    }




}