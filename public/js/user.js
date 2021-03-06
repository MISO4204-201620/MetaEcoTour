var tipoUser = (function()
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

    var tieneMensajeria = (function()
    {
        var mensajeria = JSON.parse(localStorage.getItem("mensajeria")) || {};
        if(mensajeria.present)
        {
            var activo = location.pathname.split("/")[1] === "mensajes" ? true : false;
            $("#menuOpc").append("<li class='"+(activo ? "active" : "")+"'><a href='/mensajes'>Mensajería</a></li>");
        }
    })();

    var datosUser = function()
    {
        var dataUser = {existe : false};
        if(window.authToken !== undefined && window.authToken != "")
        {
            dataUser.token = window.authToken;
            //Podría ser reemplazado por un servicio que traiga sin el usuario es válido, dado el Token..
            var user = JSON.parse(localStorage.getItem("user")) || {};
            if(user.tipo !== undefined)
            {
                dataUser.existe = true;
                dataUser.data = user;
            }
        }
        return dataUser;
    };

    var esProveedor = function(div)
    {
        var user    = datosUser(),
            regresa = false;
        if(user.existe && user.data.tipo !== null)
        {
            if(user.data.tipo.toLowerCase() === "proveedor")
            {
                regresa = true;
            }
        }
        //Para poner las opciones del proveedor...
        if(regresa)
        {
            var localResportes = JSON.parse(localStorage.getItem("reportes") || ""),
                tieneReportes  = false;
            if(localResportes.childs[0].present || localResportes.childs[1].present)
            {
                tieneReportes = true;
            }
            var txtHTML = "<li class=\"dropdown\">" +
                "<a href=\"#\" data-target=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Proveedor " +
                "<b class=\"caret\"></b></a>" +
                "<ul class=\"dropdown-menu\">" +
                "<li><a href=\"/paqser\"><span class=\"glyphicon glyphicon-th-list\"></span> Mis Servicios/Paquetes</a></li>" +
                "<li><a href=\"/cpaqser\"><span class=\"glyphicon glyphicon-plus\"></span> Nuevo Paquete y servicio</a></li>";
            if(tieneReportes)
            {
                txtHTML += "<li><a href=\"/preport\"><span class=\"glyphicon glyphicon-list-alt\"></span> Mis Reportes</a></li>";
            }
            txtHTML += "</ul></li>";
            $("#" + div).append(txtHTML);
        }
        return regresa;
    };

    var esAdministrador = function(div)
    {
        var user    = datosUser(),
            regresa = false;
        if(user.existe && user.data.tipo !== null)
        {
            if(user.data.tipo.toLowerCase() === "administrador")
            {
                regresa = true;
            }
        }
        //Para poner las opciones del proveedor...
        if(regresa)
        {
            var txtHTML = "<li class=\"dropdown\">" +
                "<a href=\"#\" data-target=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Administración " +
                "<b class=\"caret\"></b></a>" +
                "<ul class=\"dropdown-menu\">" +
                "<li><a href=\"/crudproviders\">Proveedores</a></li>" +
                "<li><a href=\"/crudclients\">Usuarios</a></li>" +
                "</ul>" +
                "</li>";
            $("#" + div).append(txtHTML);
        }
        return regresa;
    };

    var logout = function()
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
            localStorage.setItem("user", "");
        }).error(function(request, status, error)
        {
            window.location = "/login";
            localStorage.setItem("user", "");
        });
    };

    return {
        datosUser       : datosUser,
        logout          : logout,
        esProveedor     : esProveedor,
        esAdministrador : esAdministrador
    };
})();

if (typeof exports !== 'undefined')
{
    for (var i in tipoUser)
    {
        exports[i] = tipoUser[i];
    }
}