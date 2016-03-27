$(function()
{
    var campos  = ["idCategoria", "nombre", "descripcion", "precioActual", "imagen"],
        idEdita = 0;
    function S_GET(id)
    {
        var a = new RegExp(id+"=([^&#=]*)");
        return decodeURIComponent(window.location.search !== "" ? a.exec(window.location.search)[1] : "0");
    }

    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });

    //Para traer los servicios, cuando se esé creando como PAQUETE...
    //VOLVER A PONER EL VAR...
    var listadoServiciosProveedor   = [],
        listadoServiciosPaquete     = [];
    var serviciosProveedor = function()
    {
        //listadoServiciosProveedor = [];
        $.getJSON("api/producto/proveedor/" + user.data.id, function(data)
        {
            //SE DEBE VALIDAR QUE SÓLO MUESTRE LOS SER (SERVICIOS)...
            console.log("El listado de servicios del proveedor");
            console.log(data);
            for(var i = 0; i < data.length; i++)
            {
                if(data[i].tipo === "SER")
                {
                    listadoServiciosProveedor.push({token : guid(), data : data[i]});
                }
            }
            idEdita = Number(S_GET("id"));
            if(idEdita !== 0)
            {
                datosProducto();
                //Para poner el tipo en el combo...
                for(var i = 0; i < data.length; i++)
                {
                    if(idEdita === data[i].id)
                    {
                        $("#tipoProducto").val(data[i].tipo);
                        $("#tipoProducto").prop('disabled', true);
                        break;
                    }
                }
            }
        })
    };

    //Cargar las categorias...
    var categorias = function()
    {
        $.getJSON("api/categorias/", function(data)
        {
            var categorias = $("#idCategoria");
            $.each(data, function()
            {
                categorias.append($("<option />").val(this.id).text(this.nombre));
            });
            serviciosProveedor();
        });
    };


    var datosServicios = function()
    {
        //Para traer los valores relacionados a los servicios relacionados al Paquete...
        console.log("LLEGA ACÁ");
        if($("#tipoProducto").val() === "PAQ")
        {
            //http://localhost:9000/api/itemServ/pqt/153
            $("#asociaServicios").show();
            $("#precioActual").prop('disabled', true);
            console.log("/api/itemServ/pqt/" + idEdita);
            $.getJSON("/api/itemServ/pqt/" + idEdita, function(data)
            {
                var dataServicio = data;
                //listadoServiciosPaquete = data;
                //Traer los valores para unificarlo en uno sólo...
                //http://localhost:9000/api/itemServ/items/153
                $.getJSON("/api/itemServ/items/" + idEdita, function(data)
                {
                    //itemServicios = data;
                    //Buscar el elemento y adicionarle los valores...
                    for(var c = 0; c < data.length; c++)
                    {
                        for(var i = 0; i < dataServicio.length; i++)
                        {
                            if(data[c].idServicio === dataServicio[i].id)
                            {
                                listadoServiciosPaquete.push({
                                    id              : dataServicio[i].id,
                                    token           : guid(),
                                    nuevo           : false,
                                    paquete         :  {id : data[c].id, cantidad : data[c].cantidad},
                                    nombre          : dataServicio[i].nombre,
                                    imagen          : dataServicio[i].imagen,
                                    precioActual    : dataServicio[i].precioActual,
                                });
                                break;
                            }
                        }
                    }
                    cargaServiciosProveedor();
                    cargaServiciosPaquete();
                });
            });
        }
    };


    var datosRecursos = function()
    {
        $.getJSON("/api/recursos/" + idEdita + "/1", function(data)
        {
            for(var i = 0; i < data.length; i++)
            {
                formRecurso(
                                data[i].tipo,
                                {
                                    id : Number(data[i].id),
                                    contenido : data[i].contenido,
                                    nombre : data[i].nombre
                                }
                );
            }
            //Si es un paquete, se deberá traer la información del mismo...
            datosServicios();
        });
    };

    var datosProducto = function()
    {
        $.getJSON("api/producto/" + idEdita, function(data)
        {
            //console.log("Los valores del producto");
            //console.log(data);
            for(var i = 0; i < campos.length; i++)
            {
                $("#" + campos[i]).val(data[campos[i]]);
            }
            //console.log(data);
            //Traer los recursos que tengan asociados...
            datosRecursos();
        });
    };

    if(tipoUser.esProveedor("menuOpc"))
    {
        var user = tipoUser.datosUser();
        //console.log(user);
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        shopping.numCompras(user.data.id, "numCompras");
        categorias();
    }
    else
    {
        window.location = "/login";
    }

    //Para saber si un servicio, ya ha sido cargado en el paquete...
    var buscaServicioEnPaquete = function(id)
    {
        var existe = false;
        for(var i = 0; i < listadoServiciosPaquete.length; i++)
        {
            if(listadoServiciosPaquete[i].id === id)
            {
                existe = true;
                break;
            }
        }
        return existe;
    };

    var cargaServiciosProveedor = function()
    {
        $("#serProveedor").empty();
        var tablaServ = "<table class=\"table table-hover\" id = \"tablaSer\"><thead><tr><th>&nbsp;</th><th>&nbsp;</th><th>Servicios</th><th>Precio</th></tr></thead><tbody>";
        $("#serProveedor").append(tablaServ);
        //console.log("DATA");
        //console.log(listadoServiciosProveedor);
        for(var i = 0; i < listadoServiciosProveedor.length; i++)
        {
            var item = listadoServiciosProveedor[i];
            //Se deberá comparar para que sólo se muestre los que no están seleccionados
            //Además sólo los que sean de tipo SER
            if(!buscaServicioEnPaquete(item.data.id))
            {
                //console.log(item.data);
                var tr = "<tr id = \"trs_"+(item.token)+"\" class = \"tableSer\">" +
                         "<td><input type=\"checkbox\" id = \"chSer_"+item.token+"\"></td>" +
                         "<td><img src = \""+(item.data.imagen)+"\" border = \"0\" class=\"imagenTabla\"></td>" +
                         "<td>"+(item.data.nombre)+"</td>" +
                         "<td>"+format2(item.data.precioActual, "$")+"</td>" +
                         "<tr>";

                $("#tablaSer").append(tr);
            }
            //$("#tablaSer > tbody > tr").size()
        }
    };

    //Para cargar los servicios que ya tenga creado el Paquete...
    var cargaServiciosPaquete = function()
    {
        $("#serPaquete").empty();
        var tablaServ = "<table class=\"table table-hover\" id = \"tablaPaq\"><thead><tr><th>&nbsp;</th><th>&nbsp;</th><th>Servicios</th><th>Precio</th></tr></thead><tbody>";
        $("#serPaquete").append(tablaServ);
        if(listadoServiciosPaquete.length !== 0)
        {
            for (var i = 0; i < listadoServiciosPaquete.length; i++)
            {
                var item = listadoServiciosPaquete[i];
                //Se deberá comparar para que sólo se muestre los que no están seleccionados
                //Además sólo los que sean de tipo SER
                var tr = "<tr id = \"trp_" + (item.token) + "\" class = \"tablePaq\">" +
                    "<td><input type = \"radio\" name = \"serpaq\" id = \"paqs_"+(item.token)+"\"></td>" +
                    "<td><img src = \"" + (item.imagen) + "\" border = \"0\" class=\"imagenTabla\"></td>" +
                    "<td>" + (item.nombre) + "</td>" +
                    "<td>" + format2(item.precioActual, "$") + "</td>" +
                    "<tr>";
                $("#tablaPaq").append(tr);
            }
        }
        else
        {
            $("#serPaquete").append("<div align = 'center'>No se ha encontrado información</div>");
        }
    };

    //La selección del tipo de producto que se creará...
    $("#tipoProducto").change(function()
    {
        if($(this).val() === "SER")
        {
            $("#asociaServicios").hide();
            $("#precioActual").prop('disabled', false);
        }
        else
        {
            $("#asociaServicios").show();
            $("#precioActual").prop('disabled', true);
            $("#precioActual").val("");
            cargaServiciosProveedor();
            cargaServiciosPaquete();
            //Se deberá cargar los servicios del proveedor...
        }
    });

    //Busca un servicio dado el Token...
    var buscarServicioSelecciona = function(token)
    {
        var regresa = {existe : false, indice : 0};
        for(var i = 0; i < listadoServiciosProveedor.length; i++)
        {
            if(listadoServiciosProveedor[i].token === token)
            {
                regresa.existe = true;
                regresa.indice = i;
                //regresa.data = listadoServiciosProveedor[i].data;
                break;
            }
        }
        return regresa;
    };

    //Buscar los datos del servicio que está relacioando a un paquete...
    var buscarServicioPaquete = function(token)
    {
        var regresa = {existe : false, indice : 0};
        for(var i = 0; i < listadoServiciosPaquete.length; i++)
        {
            if(listadoServiciosPaquete[i].token === token)
            {
                regresa.existe = true;
                regresa.indice = i;
                break;
            }
        }
        return regresa;
    };

    //Calcula el precio del paquete en relación a los servicios seleccionados...
    var calculaPrecioPaquete = function()
    {
        var precio = 0;
        for(var i = 0; i < listadoServiciosPaquete.length; i++)
        {
            precio += listadoServiciosPaquete[i].precioActual;
        }
        return precio;
    };

    $(".selBtn").click(function(e)
    {
        var tipoAccion = e.currentTarget.id;
        if(tipoAccion.toLowerCase() === "btnasocia")
        {
            var $elemento       = $(".tableSer"),
                numSelecciona   = 0,
                token           = "",
                itemsSelecciona = [];
            //debugger;
            if($elemento.size() !== 0)
            {
                for(var i = 0; i < $elemento.size(); i++)
                {
                    token = $elemento[i].id.split("_")[1];
                    if($('#chSer_' + token).is(":checked"))
                    {
                        var item = buscarServicioSelecciona(token);
                        if(item.existe)
                        {
                            itemsSelecciona.push(listadoServiciosProveedor[item.indice].data);
                            numSelecciona++;
                        }
                    }
                }
            }
            //Si ha seleccionado servicios para asociar al paquete...
            if(numSelecciona !== 0)
            {
                //console.log("Selecciona");
                //console.log(itemsSelecciona);
                swal({
                    title: "¿Estás segur@?",
                    text: "¿Deseas agregar los " + numSelecciona + " servicios seleccionados al presente Paquete?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Si, Asociar",
                    closeOnConfirm: false
                }, function ()
                {
                    swal({title: "Exitoso!", text: "Se han agregado los servicios al paquete",   timer: 2000, type : "success" });
                    for(var i = 0; i < itemsSelecciona.length; i++)
                    {
                        var item = itemsSelecciona[i];
                        listadoServiciosPaquete.push({
                            id              : item.id,
                            token           : guid(),
                            nuevo           : true,
                            nombre          : item.nombre,
                            imagen          : item.imagen,
                            precioActual    : item.precioActual,
                        });
                        cargaServiciosProveedor();
                        cargaServiciosPaquete();
                        $("#precioActual").val(calculaPrecioPaquete());
                    }
                });
            }
            else
            {
                swal({title: "Error!", text: "Por favor selecciona los servicios que deseas asociar al paquete.",   timer: 3000, type : "error" });
            }
        }
        else
        {
            //console.log("Elimina un servicio de Un paquete");
            //Se deberá buscar los datos del ítem seleccionado...
            var $elemento   = $(".tablePaq"),
                token       = "",
                itemSel     = {existe : false};
            if($elemento.size() !== 0)
            {
                //console.log($elemento.size());
                //debugger;
                for(var i = 0; i < $elemento.size(); i++)
                {
                    token = $elemento[i].id.split("_")[1];
                    if($("#paqs_" + token).prop("checked"))
                    {
                        var selecciona = buscarServicioPaquete(token);
                        itemSel.existe = selecciona.existe;
                        if(itemSel.existe)
                        {
                            itemSel.indice = selecciona.indice;
                        }
                        break;
                    }
                }
            }
            if(itemSel.existe)
            {
                swal({
                    title: "¿Estás segur@?",
                    text: "¿Deseas Eliminar el servicio de este Paquete?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Si, Eliminar",
                    closeOnConfirm: false
                }, function ()
                {
                    if(!listadoServiciosPaquete[itemSel.indice].nuevo)
                    {
                        //El elemento no es nuevo...
                        var idServicio = listadoServiciosPaquete[itemSel.indice].paquete.id;
                        $.ajax({
                            url 		: "/api/itemServ/delete/" + idServicio,
                            type 		: "DELETE",
                            dataType 	: "json",
                            headers     : {"X-AUTH-TOKEN": window.authToken}
                        }).done(function(data)
                        {
                            swal({title: "Eliminado!", text: "Se ha eliminado el servicio del Paquete.",   timer: 2000, type : "success" });
                            listadoServiciosPaquete.splice(itemSel.indice, 1);
                            cargaServiciosProveedor();
                            cargaServiciosPaquete();
                            $("#precioActual").val(calculaPrecioPaquete());
                            console.log(data);
                        }).error(function(request, status, error)
                        {
                            console.log(request, status, error);
                            swal({title: "Error!", text: "No ha sido posible relizar la acción.",   timer: 2000, type : "error" });
                        });
                    }
                    else
                    {
                        swal({title: "Eliminado!", text: "Se ha eliminado el servicio del Paquete.",   timer: 2000, type : "success" });
                        listadoServiciosPaquete.splice(itemSel.indice, 1);
                        cargaServiciosProveedor();
                        cargaServiciosPaquete();
                        $("#precioActual").val(calculaPrecioPaquete());
                    }
                });
            }
        }
    });

    //Para listar/normalizar la información que se guardará relacionada a los servicios de un paquete...
    var itemServicios = function()
    {
        var regresa = {
                            completo    : (listadoServiciosPaquete.length === 0 ? false : true),
                            listado     : []
        };

        if(regresa.completo)
        {
            //Sólo traer los nuevos...
            for(var i = 0; i < listadoServiciosPaquete.length; i++)
            {
                if(listadoServiciosPaquete[i].nuevo)
                {
                    regresa.listado.push({
                        idServicio  : listadoServiciosPaquete[i].id,
                        cantidad    : 1
                    });
                }
            }
        }
        return regresa;
    };

    //Para agregar un recurso...
    $("#tipoRecurso").change(function()
    {
        if($(this).val() !== "sel")
        {
            formRecurso($(this).val(), {});
            $(this).val("sel");
        }
    });
    //$("#newRecursos > .panel").size()

    var formRecurso = function(tipo, data)
    {
        var token   = guid(),
            id      = data.id !== undefined ? data.id : 0,
            dataID  = tipo + "#" + id;
        var form =  '<div class="panel panel-success" id = "rec_'+(token)+'" data-id = "'+(dataID)+'">' +
                    '<div class="panel-heading">Agergar Recurso '+(tipo)+'</div>' +
                    '<div class="panel-body">' +
                    '<div class="form-group">' +
                    '<div class="col-sm-12">' +
                    '<input type="text" class="form-control" id="nom_'+(token)+'" placeholder="Nombre" value="'+(id !== 0 ? data.nombre : "")+'">' +
                    '</div>' +
                    '</div>' +
                    '<div class="form-group">' +
                    '<div class="col-sm-12">';
        if(tipo === "html")
        {
            form += '<div id="text_'+(token)+'">'+(id !== 0 ? data.contenido : "")+'</div>';
        }
        else
        {
            form += '<input type="text" class="form-control" id = "desc_'+(token)+'" placeholder="Contenido" value="'+(id !== 0 ? data.contenido : "")+'">';
        }
        form += '</div></div>' +
                '<button type="button" class="btn btn-danger" id = "del_'+(token)+'">Remover Recurso</button>' +
                '</div></div>';
        $("#newRecursos").append(form);
        if(tipo === "html")
        {
            $('#text_' + token).summernote();
        }
        $("#del_" + token).click(function(e)
        {
            //console.log("Remover el elemento seleccionado");
            var ind = this.id.split("_")[1],
                id  = $("#rec_" + ind).attr("data-id").split("#")[1];
            swal({
                title: "¿Estás segur@?",
                text: "¿Deseas remover el recurso seleccionado?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Si, remover",
                closeOnConfirm: false
            }, function ()
            {
                if(Number(id) !== 0)
                {
                    $.ajax({
                        url 		: "/api/recursos/" + id,
                        type 		: "DELETE",
                        dataType 	: "json",
                        headers     : {"X-AUTH-TOKEN": window.authToken}
                    }).done(function(data)
                    {
                        console.log(data);
                        swal({title: "Eliminado!", text: "Se ha eliminado el paquete/servicio.",   timer: 2000, type : "success" });
                        $("#rec_" + ind).fadeOut("fast", function(){
                            $(this).remove();
                        });
                    }).error(function(request, status, error)
                    {
                        console.log(request, status, error);
                        swal({title: "Error!", text: "No ha sido posible relizar la acción.",   timer: 2000, type : "error" });
                    });
                }
                else
                {
                    swal({title: "Eliminado!", text: "Se ha removido el recurso.",   timer: 2000, type : "success" });
                    $("#rec_" + ind).fadeOut("fast", function(){
                        $(this).remove();
                    });
                }
            });
        });
        if(id === 0) {
            $("#nom_" + token).focus();
        }
        //listadoRecursos
    };

    var actualizaServicios = function()
    {
        console.log("Actualizar los servicios");
        var itemServ = itemServicios();
        if(itemServ.listado.length !== 0)
        {
            var servicios = {idProducto : idEdita, itemServicios : itemServ.listado};
            //http://localhost:9000/api/itemServ/crear/
            $.ajax({
                url 		: "/api/itemServ/crear/",
                type 		: "POST",
                data 		: JSON.stringify(servicios),
                dataType 	: "json",
                contentType: "application/json; charset=utf-8"
            }).done(function(data)
            {
                console.log(data);
                window.location = "/paqser";
            }).error(function(request, status, error)
            {
                sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
                //callback(true);
            });
        }
        else
        {
            console.log("No hay nuevos ítems");
            window.location = "/paqser";
        }
    };

    var actualizaRecursos = function()
    {
        var productos = {idProducto : idEdita, recursos : listadoRecursos().listado};
        //console.log(productos);
        //var recursos = listadoRecursos();
        $.ajax({
            url 		: "/api/recursos/",
            type 		: "POST",
            data 		: JSON.stringify(productos),
            dataType 	: "json",
            contentType: "application/json; charset=utf-8"
        }).done(function(data)
        {
            //console.log(data);
            if($("#tipoProducto").val() === "PAQ")
            {
                actualizaServicios();
            }
            else
            {
                window.location = "/paqser";
            }
        }).error(function(request, status, error)
        {
            sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
            //callback(true);
        });
    };

    $("#newServPaq").submit(function(event)
    {
        //Primero saber si los campos no están vacios...
        var productos = {tipoProducto : $("#tipoProducto").val(), producto : {}},
            procesa = true;
        for(var i = 0; i < campos.length; i++)
        {
            productos.producto[campos[i]] = $("#" + campos[i]).val();
            if(i >= 1 && $("#" + campos[i]).val().length === 0)
            {
                $("#" + campos[i]).focus();
                sweetAlert("Error", "Por favor completa el campo: " + campos[i], "error");
                procesa = false;
                break;
            }
        }
        if(procesa)
        {
            //Ahora recorrer la cantidad de recursos que tiene...
            if($("#newRecursos > .panel").size() !== 0)
            {
                var recursos = listadoRecursos();
                if(recursos.completo)
                {
                    productos.producto.id = idEdita !== 0 ? idEdita : "";
                    productos.producto.idProveedor = user.data.id;
                    productos.producto.recursos = recursos.listado;
                    if($("#tipoProducto").val() === "PAQ")
                    {
                        var itemServ = itemServicios();
                        if(itemServ.completo)
                        {
                            productos.producto.itemServicios = itemServ.listado;
                        }
                        else
                        {
                            procesa = false;
                            sweetAlert("Error", "No has asociado servicios al paquete", "error");
                        }
                    }
                }
                else
                {
                    procesa = false;
                }
            }
            else
            {
                sweetAlert("Error", "No has asociado recursos al producto", "error");
                procesa = false;
            }
        }
        //debugger;
        if(procesa)
        {
            console.log(productos);
            $.ajax(
            {
                url 		: "/api/productos/",
                type 		: "POST",
                data 		: JSON.stringify(productos),
                dataType 	: "json",
                contentType: "application/json; charset=utf-8"
            }).done(function(data)
            {
                console.log(data);
                if(idEdita === 0)
                {
                    window.location = "/paqser";
                }
                else
                {
                    //Guardar los recursos relacionados...
                    actualizaRecursos();
                }
            }).error(function(request, status, error)
            {
                sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
                //callback(true);
            });
        }
        event.preventDefault();
    });

    //Para listar los recursos asociados al producto...
    var listadoRecursos = function()
    {
        var recursos = [],
            completo = true;
        for(var i = 0; i < $("#newRecursos > .panel").size(); i++)
        {
            var elemento    = {},
                token       = $("#newRecursos > .panel")[i].id.split("_")[1],
                datos       = $("#rec_" + token).attr("data-id").split("#");
            elemento.id = Number(datos[1]) === 0 ? "" : Number(datos[1]);
            elemento.tipo = datos[0];
            elemento.nombre = $("#nom_" + token).val();
            elemento.contenido = elemento.tipo !== "html" ? $("#desc_" + token).val() : $('#text_' + token).summernote('code');
            if(elemento.nombre.length === 0 || elemento.contenido.length === 0)
            {
                completo = false;
                sweetAlert("Error", "Por favor completa todos los campos del recurso", "error");
                break;
            }
            recursos.push(elemento);
        }
        return {
                    listado : recursos,
                    completo : completo
               };
    };

    //Generar un token único...
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



