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
            idEdita = Number(S_GET("id"));
            if(idEdita !== 0)
            {
                datosProducto();
            }
        });
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
        });
    };

    var datosProducto = function()
    {
        $.getJSON("api/producto/" + idEdita, function(data)
        {
            for(var i = 0; i < campos.length; i++)
            {
                $("#" + campos[i]).val(data[campos[i]]);
            }
            //console.log(data);
            //Traer los recursos que tengan asociados...
            datosRecursos();
        });
    };

    if(tipoUser.esProveedor())
    {
        var user = tipoUser.datosUser();
        console.log(user);
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        categorias();
    }
    else
    {
        window.location = "/login";
    }

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

    var actualizaRecursos = function()
    {
        var productos = {idProducto : idEdita, recursos : listadoRecursos().listado};
        //console.log(productos);
        var recursos = listadoRecursos();
        $.ajax({
                    url 		: "/api/recursos/",
                    type 		: "POST",
                    data 		: JSON.stringify(productos),
                    dataType 	: "json",
                    contentType: "application/json; charset=utf-8"
                }).done(function(data)
                {
                    //console.log(data);
                    window.location = "/paqser";
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
            console.log(productos);
            //Ahora recorrer la cantidad de recursos que tiene...
            if($("#newRecursos > .panel").size() !== 0)
            {
                var recursos = listadoRecursos();
                if(recursos.completo)
                {
                    productos.producto.id = idEdita !== 0 ? idEdita : "";
                    productos.producto.idProveedor = user.data.id;
                    productos.producto.recursos = recursos.listado;
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
        if(procesa)
        {
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
                if(idEdita === 0) {
                    window.location = "/paqser";
                }
                else
                {
                    //Guardar los recursos relacionados...
                    actualizaRecursos();
                }
                /*
                if(data.errorCode === undefined)
                {
                    callback(false);
                }
                else
                {
                    sweetAlert("Error", data.desCode, "error");
                    callback(true);
                }
                */
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



});



