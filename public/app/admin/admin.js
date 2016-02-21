function AdminCtrl($scope, $rootScope, User, Categoria) {
    $scope.allow = User.allow;
    $scope.atributosCategoria = Categoria.atributos;
    Categoria.watch(function (categorias) {
        $scope.categorias = categorias;
    })
    $scope.categoriaNueva = {nombre:"hola"}


    $scope.showAddCategoria = function (reason, tutorAntiguoP) {
        $scope.showAddCategoriaDialog = true;
    }



    $scope.deleteCategoria = Categoria.deleteCategoria;
    $scope.selectCategoria = function(categoria){
        $scope.categoriaSelected = categoria;
        $scope.showEditCategoriaDialog = true;
    }
    $scope.opcionesEditCategoria = [{
        name: "Cancelar",
        btnClass: "JM_cancelButton",
        callback: function (categoria) {
            $scope.showEditCategoriaDialog = false;
        }
    }, {
        name: "Guardar",
        btnClass: "JM_aceptButton",
        callback: function (categoria) {
            $scope.showEditCategoriaDialog = false;
            Categoria.agregarCategoria(categoria, function(){

            });
        }
    }]


    $scope.opcionesAddCategoria = [{
        name: "Cancelar",
        btnClass: "JM_cancelButton",
        callback: function (categoria) {
            $scope.showAddCategoriaDialog = false;
        }
    }, {
        name: "Crear",
        btnClass: "JM_aceptButton",
        callback: function (categoria) {
            $scope.showAddCategoriaDialog = false;
            Categoria.agregarCategoria(categoria, function(){

            });
        }
    }]


    $scope.expand = "both";
}

angular.module('app').controller('AdminCtrl', AdminCtrl)