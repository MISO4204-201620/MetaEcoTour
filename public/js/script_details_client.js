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

    $("#createEditClient").submit(function(event)
    {

        //Primero saber si los campos no están vacios...
        var cliente = {tipoUsuario:"CLIENT",usuario : {}},
            campos = ["nombre", "apellido","correo", "clave", "documento", "tipoDoc"],
            procesa = true;
        for(var i = 0; i < campos.length; i++)
        {
            cliente.usuario[campos[i]] = $("#" + campos[i]).val();

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
            cliente.usuario["id"] = idCliente;

            //http://localhost:9000/api/productos/
            //http://localhost:9000/api/usuarios/crear
            $.ajax(
                {
                    url 		: "/api/usuarios/crear",
                    type 		: "POST",
                    data 		: JSON.stringify(cliente),
                    dataType 	: "json",
                    contentType: "application/json; charset=utf-8"
                }).done(function(data)
            {
                if (registroLogin){
//                    window.location = "/login";
                    //swal({title: "Exitoso!", text: "Se han creado el usuario, por favor loguese con sus credenciales",   timer: 2000, type : "success" });
                    swal({
                        title: "Exitoso",
                        text: "Se ha creado una cuenta de usuario con tus datos, ¿Deseas iniciar sesión?",
                        type: "success",
                        showCancelButton: false,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Si, Iniciar Sesión",
                        closeOnConfirm: false
                    }, function ()
                    {
                        window.location = "/login";
                    });
                }else {
                    window.location = "/crudclients";
                }


            }).error(function(request, status, error)
            {
                sweetAlert("Error", "No ha sido posible guardar el cliente", "error");

            });
        }
        event.preventDefault();
    });

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