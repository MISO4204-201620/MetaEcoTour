$(function()
{
    //var listadoRecursos = [];
    //Se obtiene el token de validación del usuario...
    /*
    document.cookie.split('; ').forEach(function(cookieString)
    {
        //console.log(cookieString);
        var cookie = cookieString.split("=");
        if ((cookie.length === 2) && (cookie[0] === "authToken"))
        {
            window.authToken = cookie[1];
            console.log(window.authToken);
        }
    });
    */
    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });

    //$('#summernote').summernote();
    var listadoCategorias = [];
    if(tipoUser.esProveedor())
    {
        var user = tipoUser.datosUser();
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        var categorias = (function()
        {
            $.getJSON("api/categorias/", function(data)
            {
                listadoCategorias = data;
                /*
                $.each(data, function()
                {
                    listadoCategorias.push({
                        id : this.id,
                        nombre : this.nombre
                    });
                });
                */
                //console.log(listadoCategorias);
                productosProveedor();
                //console.log("Termina");
            });
        })();
    }
    else
    {
        window.location = "/login";
    }

    var categoriaProducto = function(id)
    {
        var nomCategoria = "";
        for(var i = 0; i < listadoCategorias.length; i++)
        {
            if(id === listadoCategorias[i].id)
            {
                nomCategoria = listadoCategorias[i].nombre;
                break;
            }
        }
        return nomCategoria;
    };

    //Traer el listado de paquetes y servicios...
    var productosProveedor = function()
    {
        console.log(user.data.id);
        //http://localhost:9000/api/producto/proveedor/1
        $.getJSON("api/producto/proveedor/" + user.data.id, function(data)
        {
            console.log(data);
            /*
             <table class="table table-hover"> <thead> <tr> <th>#</th> <th>First Name</th> <th>Last Name</th> <th>Username</th> </tr> </thead> <tbody> <tr> <th scope="row">1</th> <td>Mark</td> <td>Otto</td> <td>mdo</td> </tr> <tr> <th scope="row">2</th> <td>Jacob</td> <td>Thornton</td> <td>fat</td> </tr> <tr> <th scope="row">3</th> <td>Larry</td> <td>the Bird</td> <td>twitter</td> </tr> </tbody> </table>
             */
            var tabla = "<table class=\"table table-hover\" id = \"tabla\"><thead><tr>";
            tabla += "<th></th><th>Nombre</th><th>Precio</th><th>Categoria</th><th>Puntuación</th></tr> </thead><tbody>";
            $("#listado").append(tabla);
            $.each(data, function()
            {
                var token = guid();
                var urlDetalle = "api/recursos/" + this.id + "/0";
                var ratings = "";
                for(var i = 1; i <= 5; i++)
                {
                    ratings += '<span class = "glyphicon ' + (i <= this.puntuacion ? 'glyphicon-star' : 'glyphicon-star-empty' ) + '"></span>';
                }
                var tr = "<tr id = \"tr_"+(token)+"\" data-id = \""+(this.id)+"\"><th scope=\"row\"><img src = \""+(this.imagen)+"\" border = \"0\" class=\"imagenTabla\"></th>" +
                         "<td><a href = \""+(urlDetalle)+"\">" + this.nombre + "</a></td>" +
                         "<td>" + (this.precioActual) + "</td>" +
                         "<td>" + categoriaProducto(this.idCategoria) + "</td>" +
                         "<td>" + (ratings) + "</td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"edit_"+(token)+"\">" +
                         "<span class=\"glyphicon glyphicon-edit\"></span> Editar </button></td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"del_"+(token)+"\">" +
                         "<span class=\"glyphicon glyphicon-remove\"></span> Eliminar </button></td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"preg_"+(token)+"\">" +
                         "<span class=\"glyphicon glyphicon-comment\"></span> Preguntas </button></td></tr>";
                $("#tabla").append(tr);
                $("#del_" + token).click(function(e){
                    var token = this.id.split("_")[1];
                    var id = $("#tr_" + token).attr("data-id");
                    swal({
                        title: "¿Estás segur@?",
                        text: "¿Deseas Eliminar este Paquete/servicio?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Si, Eliminar",
                        closeOnConfirm: false
                    }, function ()
                    {
                        $.ajax({
                            url 		: "/api/productos/" + id,
                            type 		: "DELETE",
                            dataType 	: "json",
                            headers     : {"X-AUTH-TOKEN": window.authToken}
                        }).done(function(data)
                        {
                            swal({title: "Eliminado!", text: "Se ha eliminado el paquete/servicio.",   timer: 2000, type : "success" });
                            $("#tr_" + token).fadeOut("fast", function(){
                                $(this).remove();
                            })
                            console.log(data);
                            //window.location = "/login";
                            //localStorage.setItem("user", "");
                        }).error(function(request, status, error)
                        {
                            console.log(request, status, error);
                            swal({title: "Error!", text: "No ha sido posible relizar la acción.",   timer: 2000, type : "error" });
                        });
                    });
                });
                $("#edit_" + token).click(function() {
                    window.location = "/cpaqser?id=" + $("#tr_" + this.id.split("_")[1]).attr("data-id");
                });
                $("#preg_" + token).click(function() {
                    window.location = "/preguntas?id=" + $("#tr_" + this.id.split("_")[1]).attr("data-id");
                });
            });
            //$("#listado").append("</tbody></table>");
            //"imagen", id, nombre, "precioActual", "idCategoria"
            /*
            $.each(data, function()
            {
                listadoCategorias.push({
                    id : this.id,
                    nombre : this.nombre
                });
            });
            //console.log(listadoCategorias);
            productosProveedor();
            */
            //console.log("Termina");
        });
    };

    /*
    var user = JSON.parse(localStorage.getItem("user")) || {};
    console.log(user);
    if(user.tipo !== undefined)
    {
        if(user.tipo.toLowerCase() === "proveedor")
        {
            console.log(user.tipo.toLowerCase());
        }
        else
        {
            window.location = "/login";
        }

    }

    //Generar un token único...
    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }
    */
    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }

});



