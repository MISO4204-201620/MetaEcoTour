$(function()
{

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

    $("#createEditProvider").submit(function(event)
    {
        console.log(idProveedor);
        //Primero saber si los campos no están vacios...
        var proveedor = {tipoUsuario:"PROVIDER",usuario : {}},
            campos = ["nombre", "correo", "clave", "documento", "tipoDoc","descripcion","activo"],
            procesa = true;
        for(var i = 0; i < campos.length; i++)
        {
            proveedor.usuario[campos[i]] = $("#" + campos[i]).val();
            console.log(campos[i]);
            console.log($("#" + campos[i]).val());
            if($("#" + campos[i]).val().length === 0)
            {
                $("#" + campos[i]).focus();
                sweetAlert("Error", "Por favor completa el campo: " + campos[i], "error");
                procesa = false;
                break;
            }
        }

        if(procesa)
        {
            proveedor.usuario["id"] = idProveedor;
            console.log(JSON.stringify(proveedor));
            //http://localhost:9000/api/productos/
            //http://localhost:9000/api/usuarios/crear
            $.ajax(
                {
                    url 		: "/api/usuarios/crear",
                    type 		: "POST",
                    data 		: JSON.stringify(proveedor),
                    dataType 	: "json",
                    contentType: "application/json; charset=utf-8"
                }).done(function(data)
            {
                console.log(data);
                window.location = "/crudproviders";
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
                sweetAlert("Error", "No ha sido posible guardar el proveedor", "error");
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