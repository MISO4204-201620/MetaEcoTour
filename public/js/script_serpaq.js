$(function()
{

    var REPORTE_VENTAS    = false,
        listadoCategorias = [],
        resultadoReporte  = [];
    $.getJSON("api/services/", function(data)
    {
        //console.log(data[3].childs);
        console.log(data[3].childs[2].present);
        REPORTE_VENTAS = data[3].childs[2].present;
        if(REPORTE_VENTAS)
        {
            $.getScript("js/generaPDF.js", function (data, textStatus, jqxhr)
            {
                console.log(jqxhr.status); // 200
            });
        }

        $.getJSON("api/categorias/", function(data)
        {
            listadoCategorias = data;
            productosProveedor();
        });
    });


    var buscaProducto = function(id)
    {
        var producto = {};
        for(var i = 0; i < resultadoReporte.length; i++)
        {
            if(Number(id) === Number(resultadoReporte[i].id))
            {
                producto = resultadoReporte[i];
                break;
            }
        }
        return producto;
    };

    var generaCompras = function(idProducto)
    {
        var urlConsulta = "/api/rptCompra/prv/"+user.data.id+"/" + idProducto;
            cont        = 1,
            producto    = buscaProducto(idProducto),
            datos       = [],
            estados     = {X : "Cancelado", C : "Cerrado", O : "Abierto", E : "Error"};
        //console.log(idproducto);
        $.getJSON(urlConsulta, function(data)
        {
            // sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
            $.each(data, function()
            {
                //console.log(this);
                datos.push([cont, this[10], this[5], (this[6] !== null ? this[6] : "--"), estados[this[7]], this[8] !== "" ? this[8] : "--"]);
                cont++;
            });
            //console.log("Ingresa a ese punto");
            //console.log(datos);
            PDF.generaPDF({
                header  : ["Producto: " + producto.nombre, "Tipo: " + producto.tipo, "Precio: " + format2(producto.precioActual, "$")],
                columns : ["#", "Usuario", "Fecha Creación", "Fecha Actualiza", "Estado Compra", "Forma de Pago"],
                data    : datos,
                nombre  : "Reporte Compras " + producto.nombre
            });
        });
    };


    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });

    var txtLoading = "<div align='center'><img src = '/images/loading.gif' border = '0'/></div>";

    //$('#summernote').summernote();
    if(tipoUser.esProveedor("menuOpc"))
    {
        $("#listado").html(txtLoading);
        var user = tipoUser.datosUser();
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        shopping.numCompras(user.data.id, "numCompras");
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

    var atributosServicio   = [],
        idProdunctoSel      = 0
        newAtributo         = {creando : false, nuevo : true, id : 0, nombre: "", valor : "", token: ""};
    var buscarAtributoSeleccionado = function(token)
    {
        var regresa = {existe : false, indice : 0};
        for(var i = 0; i < atributosServicio.length; i++)
        {
            if(atributosServicio[i].token === token)
            {
                regresa.existe = true;
                regresa.indice = i;
                break;
            }
        }
        return regresa;
    };

    var seleEditaAtributo = function(divId)
    {
        var opcSel  = divId.split("_"),
            attrSel = buscarAtributoSeleccionado(opcSel[1]);
        if(attrSel.existe)
        {
            if(opcSel[0].toLowerCase() === "editattr")
            {
                if(!newAtributo.creando)
                {
                    newAtributo.nuevo = false;
                    newAtributo.id = atributosServicio[attrSel.indice].id;
                    newAtributo.nombre = atributosServicio[attrSel.indice].nombre;
                    newAtributo.valor = atributosServicio[attrSel.indice].valor;
                    newAtributo.token = atributosServicio[attrSel.indice].token;
                    crearEditarAtributo();
                }
            }
            else
            {
                swal({
                    title: "¿Estás segur@?",
                    text: "¿Deseas Eliminar el atributo seleccionado?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Si, Eliminar",
                    closeOnConfirm: false
                }, function ()
                {
                    saveDleteNewAtributoServicio({idAtributo : atributosServicio[attrSel.indice].id, idProducto : idProdunctoSel}, "rmv", function(err, data)
                    {
                        if(!err)
                        {
                            swal({title: "Eliminado!", text: "Se ha eliminado el atributo.",   timer: 2000, type : "success" });
                            $("#trAtrr_" + atributosServicio[attrSel.indice].token).fadeOut("fast", function(){
                                $(this).remove();
                                atributosServicio.splice(attrSel.indice, 1);
                            });
                        }
                        else
                        {
                            sweetAlert("Error", "No ha sido posible realizar la acción", "error");
                        }
                    });
                });
            }
        }
    };

    var creaTrServicios = function(data, type, asocia)
    {
        var tr = "<td width=\"25%\">"+(type === 1 ? data.nombre : "<input type=\"text\" class=\"form-control\" placeholder=\"Nombre\" value = \""+(data.nombre)+"\" id = \"nomNewattr_"+(data.token)+"\">")+"</td>" +
                 "<td width=\"50%\">"+(type === 1 ? data.valor : "<input type=\"text\" class=\"form-control\" placeholder=\"Valor\" value = \""+(data.valor)+"\" id = \"valNewattr_"+(data.token)+"\">")+"</td>" +
                 "<td width=\"25%\">";
        if(type === 1)
        {
            tr += "<button type=\"button\" class=\"btn btn-default btn-sm\" id = \"editattr_"+(data.token)+"\">" +
                  "<span class=\"glyphicon glyphicon-edit\"></span></button>" +
                  "<button type=\"button\" class=\"btn btn-default btn-sm\" id = \"deleteattr_"+(data.token)+"\">" +
                  "<span class=\"glyphicon glyphicon-remove\"></span></button>";
        }
        else
        {
            tr += "<button type=\"button\" class=\"btn btn-default btn-sm\" id = \"savNewattr_"+(data.token)+"\">" +
                  "<span class=\"glyphicon glyphicon-saved\"></span></button>" +
                  "<button type=\"button\" class=\"btn btn-default btn-sm\" id = \"delNewattr_"+(data.token)+"\">" +
                  "<span class=\"glyphicon glyphicon-ban-circle\"></span></button>"
        }
        tr += "</td>";
        if(asocia.tipo === 1)
        {
            $("#" + asocia.elemento).append("<tr id = \"trAtrr_"+(data.token)+"\">" + tr + "</tr>");
        }
        else
        {
            $("#" + asocia.elemento).html(tr);
        }
        if(type === 1)
        {
            $("#editattr_" + data.token).click(function(e)
            {
                seleEditaAtributo(e.currentTarget.id);
            });
            $("#deleteattr_" + data.token).click(function(e)
            {
                seleEditaAtributo(e.currentTarget.id);
            });
        }
        else
        {
            $("#savNewattr_" + newAtributo.token).click(function()
            {
                //console.log("Aceptta: ", this.id);
                var nombre = $("#nomNewattr_" + newAtributo.token).val();
                var valor = $("#valNewattr_" + newAtributo.token).val();
                if(nombre.length !== 0 && valor.length !== 0)
                {
                    saveDleteNewAtributoServicio({idProducto : idProdunctoSel, nombre : nombre, tipo : "String", valor : valor}, "add", function(err, data)
                    {
                        if(!err)
                        {
                            console.log(data);
                            atributosServicio.push({
                                token   : newAtributo.token,
                                id      : data.idAtributo,
                                nombre  : data.nombre,
                                valor   :  data.valor
                            });
                            creaTrServicios({token : newAtributo.token, nombre : data.nombre, valor : data.valor}, 1, {tipo : 2, elemento : "trAtrr_" + newAtributo.token});
                            newAtributo = {creando : false, nuevo : true, id : 0, nombre: "", valor : "", token: ""};
                        }
                        else
                        {
                            sweetAlert("Error", "No ha sido posible realizar la acción", "error");
                        }
                    });
                }
                else
                {
                    swal({title: "Error!", text: "Por favor completa los campos",   timer: 2000, type : "error" });
                }
            });
            $("#delNewattr_" + newAtributo.token).click(function()
            {
                console.log("Cancela: ", this.id);
                if(!newAtributo.nuevo)
                {
                    creaTrServicios({token : newAtributo.token, nombre : newAtributo.nombre, valor : newAtributo.valor}, 1, {tipo : 2, elemento : "trAtrr_" + newAtributo.token});
                }
                else
                {
                    $("#trAtrr_" + newAtributo.token).remove();
                }
                newAtributo = {creando : false, nuevo : true, id : 0, nombre: "", valor : "", token: ""};
            });
        }
    };

    var listarAtrbutosServicio = function(data)
    {
        var token = "";
        var tabla = "<table class = \"table\" id = \"tablatrr\"><thead><tr>";
        tabla += "<th>Nombre</th><th>Valor</th><th>&nbsp;</th></tr></thead><tbody>";
        $("#atributos").append(tabla);
        //atributosServicio = data;
        for(var i = 0; i < data.length; i++)
        {
            token = guid();
            atributosServicio.push({
                token   : token,
                id      : data[i].idAtributo,
                nombre  : data[i].nombre,
                valor   :  data[i].valor
            });
            creaTrServicios({token : token, nombre : data[i].nombre, valor : data[i].valor}, 1, {tipo : 1, elemento : "tablatrr"});
        }
    };

    var saveDleteNewAtributoServicio = function(atributo, type, callback)
    {
        if(!newAtributo.nuevo && type === "add")
        {
            atributo.idAtributo = newAtributo.id;
        }
        $.ajax({
            url 		: "/api/producto/atr/" + type + "/",
            type 		: "POST",
            data 		: JSON.stringify(atributo),
            dataType 	: "json",
            contentType : "application/json; charset=utf-8"
        }).done(function(data)
        {
            callback(false, data);
        }).error(function(request, status, error)
        {
            callback(true);
        });
    };

    var crearEditarAtributo = function()
    {
        if(!newAtributo.creando)
        {
            var tr = "";
            newAtributo.creando = true; //Indica que está creando un atributo...
            if(atributosServicio.length === 0)
            {
                //No existe la tabla, por lo que se debe crear...
                var tabla = "<table class = \"table\" id = \"tablatrr\"><thead><tr>";
                tabla += "<th>Nombre</th><th>Valor</th><th>&nbsp;</th></tr></thead><tbody>";
                $("#atributos").append(tabla);
            }
            var tipoAsocia = {tipo : 0, elemento : ""};
            if(newAtributo.nuevo)
            {
                newAtributo.token = guid();
                tipoAsocia.tipo = 1;
                tipoAsocia.elemento = "tablatrr";
                //tr = "<tr id = \"trAtrr_"+(newAtributo.token)+"\">";
            }
            else
            {
                tipoAsocia.tipo = 2;
                tipoAsocia.elemento = "trAtrr_" + newAtributo.token;
            }
            creaTrServicios({token : newAtributo.token, nombre : newAtributo.nombre, valor : newAtributo.valor}, 2, tipoAsocia);
            //tr += creaTrServicios({token : newAtributo.token, nombre : newAtributo.nombre, valor : newAtributo.valor}, 2);
            /*
            if (newAtributo.nuevo)
            {
                tr += "</tr>";
                $("#tablatrr").append(tr);
            }
            else
            {
                $("#trAtrr_" + newAtributo.token).html(tr);
            }
            */
        }
    };

    //Para la acción de un nuevo atributo...
    $("#newAtrr").click(function()
    {
        crearEditarAtributo();
    });

    $("#cancelAtrr").click(function(){
        $('#myModal').modal('hide');
    });




    //Traer el listado de paquetes y servicios...
    var productosProveedor = function()
    {
        console.log(user.data.id);
        resultadoReporte = [];
        //http://localhost:9000/api/producto/proveedor/1
        $.getJSON("api/producto/proveedor/" + user.data.id, function(data)
        {
            resultadoReporte = data;
            console.log(resultadoReporte);
            /*
             <table class="table table-hover"> <thead> <tr> <th>#</th> <th>First Name</th> <th>Last Name</th> <th>Username</th> </tr> </thead> <tbody> <tr> <th scope="row">1</th> <td>Mark</td> <td>Otto</td> <td>mdo</td> </tr> <tr> <th scope="row">2</th> <td>Jacob</td> <td>Thornton</td> <td>fat</td> </tr> <tr> <th scope="row">3</th> <td>Larry</td> <td>the Bird</td> <td>twitter</td> </tr> </tbody> </table>
             */
            $("#listado").empty();
            var tabla = "<table class=\"table table-hover\" id = \"tabla\"><thead><tr>";
            tabla += "<th></th><th>Nombre</th><th>Tipo</th><th>Precio</th><th>Categoria</th><th>Puntuación</th></tr> </thead><tbody>";
            $("#listado").append(tabla);
            $.each(data, function()
            {
                var token = guid(),
                    urlDetalle  = "api/recursos/" + this.id + "/0",
                    ratings     = "",
                    tipoRecurso = {SER : "Servicio", PAQ : "Paquete"};
                for(var i = 1; i <= 5; i++)
                {
                    ratings += '<span class = "glyphicon ' + (i <= this.puntuacion ? 'glyphicon-star' : 'glyphicon-star-empty' ) + '"></span>';
                }
                var tr = "<tr id = \"tr_"+(token)+"\" data-id = \""+(this.id)+"\"><th scope=\"row\"><img src = \""+(this.imagen)+"\" border = \"0\" class=\"imagenTabla\"></th>" +
                         "<td><a href = \""+(urlDetalle)+"\" id = \"nomb_"+token+"\">" + this.nombre + "</a></td>" +
                         "<td style=\"color: #F44336;\"><b>" + tipoRecurso[this.tipo] + "</b></td>" +
                         "<td>" + format2(this.precioActual, "$") + "</td>" +
                         "<td>" + categoriaProducto(this.idCategoria) + "</td>" +
                         "<td>" + (ratings) + "</td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"attr_"+(token)+"\" title = \"Agregar Atributos\">" +
                         "<span class=\"glyphicon glyphicon-bookmark\"></span></button></td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"edit_"+(token)+"\" title = \"Editar Paquete/Servicio\">" +
                         "<span class=\"glyphicon glyphicon-edit\"></span></button></td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"del_"+(token)+"\" title = \"Eliminar Paquete/Servicio\">" +
                         "<span class=\"glyphicon glyphicon-remove\"></span></button></td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"preg_"+(token)+"\" title = \"Preguntas al Proveedor\">" +
                         "<span class=\"glyphicon glyphicon-comment\"></span></button></td>";
                if(REPORTE_VENTAS)
                {
                    tr += "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"rventa_"+(token)+"\" title = \"Reporte de Ventas\">" +
                          "<span class=\"glyphicon glyphicon-transfer\"></span></button></td>";
                }
                tr += "</tr>";
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
                            });
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

                $("#rventa_" + token).click(function()
                {
                    var token = this.id.split("_")[1],
                        id = $("#tr_" + token).attr("data-id");
                    generaCompras(id);
                });

                $("#attr_" + token).click(function()
                {
                    var token = this.id.split("_")[1];
                    atributosServicio = [];
                    newAtributo = {creando : false, nuevo : true, id : 0, nombre: "", valor : "", token: ""};
                    idProdunctoSel = $("#tr_" + token).attr("data-id");
                    $("#textTitle").html("<b>Atributos: " + $("#nomb_" + token).html() + "</b>");
                    $('#myModal').modal('show');
                    $("#atributos").html(txtLoading);
                    $.getJSON("/api/producto/atr/" + idProdunctoSel, function(data)
                    {
                        $("#atributos").empty();
                        if(data.length !== 0)
                        {
                            listarAtrbutosServicio(data);
                        }
                        else
                        {
                            $("#atributos").html("<div align = 'center'><b>No existen atributos relacionados</b></div>");
                        }
                    });
                });
            });
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

    function format2(n, currency)
    {
        return currency + " " + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
    }

});



