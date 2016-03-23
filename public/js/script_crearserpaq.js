$(function()
{
    //var listadoRecursos = [];
    //Se obtiene el token de validación del usuario...
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

    $("#logout").click(function(event)
    {
        console.log("LLega", window.authToken);
        $.ajax(
            {
                url 		: "/logout",
                type 		: "POST",
                dataType 	: "json",
                headers     : {"X-AUTH-TOKEN": window.authToken}
            }).done(function(data)
        {
            window.location = "/login";
        }).error(function(request, status, error)
        {
            window.location = "/login";
        });
    });

    //$('#summernote').summernote();


    var categorias = (function()
    {
        $.getJSON("api/categorias/", function(data)
        {
            var categorias = $("#idCategoria");
            $.each(data, function()
            {
                categorias.append($("<option />").val(this.id).text(this.nombre));
            });
        });
    })();

    //Para agregar un recurso...

    $("#tipoRecurso").change(function()
    {
        if($(this).val() !== "sel")
        {
            formRecurso($(this).val());
            $(this).val("sel");
        }
    });
    //$("#newRecursos > .panel").size()

    var formRecurso = function(tipo)
    {
        var token = guid();
        var form =  '<div class="panel panel-success" id = "rec_'+(token)+'" data-id = "'+(tipo)+'">' +
                    '<div class="panel-heading">Agergar Recurso '+(tipo)+'</div>' +
                    '<div class="panel-body">' +
                    '<div class="form-group">' +
                    '<div class="col-sm-12">' +
                    '<input type="text" class="form-control" id="nom_'+(token)+'" placeholder="Nombre">' +
                    '</div>' +
                    '</div>' +
                    '<div class="form-group">' +
                    '<div class="col-sm-12">';
        if(tipo === "html")
        {
            form += '<div id="text_'+(token)+'"><p>Escribe el texto que deseas</p></div>';
        }
        else
        {
            form += '<input type="text" class="form-control" id = "desc_'+(token)+'" placeholder="Contenido">';
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
            var ind = this.id.split("_")[1];
            console.log(ind);
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
                swal({title: "Eliminado!", text: "Se ha removido el recurso.",   timer: 2000, type : "success" });
                $("#rec_" + ind).fadeOut("fast", function(){
                    $(this).remove();
                })
            });
        });
        $("#nom_" + token).focus();
        //listadoRecursos
    };


    $("#newServPaq").submit(function(event)
    {
        //Primero saber si los campos no están vacios...
        var productos = {tipoProducto : $("#tipoProducto").val(), producto : {}},
            campos = ["idCategoria", "nombre", "descripcion", "precioActual", "imagen"],
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
            //http://localhost:9000/api/productos/
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
                window.location = "/catalog";
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
        var token    = "",
            recursos = [],
            completo = true;
        for(var i = 0; i < $("#newRecursos > .panel").size(); i++)
        {
            var elemento = {};
            token = $("#newRecursos > .panel")[i].id.split("_")[1];
            elemento.tipo = $("#rec_" + token).attr("data-id");
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

    var user = JSON.parse(localStorage.getItem("user")) || {};
    console.log(user);

    //Generar un token único...
    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }



});



