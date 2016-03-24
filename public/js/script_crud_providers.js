$(function()
{
    var numPage = 1;

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

    var paginacion = function(valor)
    {
        console.log(numPage, valor);
        if(numPage + valor >= 1)
        {
            $(".previous").removeClass("disabled");
            numPage += valor;
            proveedores();
        }
        else
        {
            $(".previous").addClass("disabled");
        }
    };

    $(".previous").click(function(e)
    {
        if($(".next").hasClass("disabled"))
        {
            $(".next").removeClass("disabled");
        }
        paginacion(-1);
    });

    $(".next").click(function(e){
        paginacion(1);
    });

    var proveedores = function()
    {
        //Para validar la paginación...
        //var url = "/api/producto/numpage/" + numPage + "/" + $("#tipoSel").val();
        var url = "/api/usuarios/" + numPage + "/PROVIDER";
        console.log(url);
        $.getJSON(url, function(data)
        {
            //console.log(data);
            if(data.errorCode === undefined)
            {
                var txt="";
                data.forEach(function(item)
                {
                    var urlDetalle = "/api/usuarios/userId/"+item.id+"/0";
                    txt += "<li class=\"list-group-item\">" +
                        '<div class="col-sm-4 col-lg-4 col-md-4">' +
                        '<div class="caption">' +
                        '<h3>'+(item.nombre) + '</h3>' +
                        '<p>'+(item.descripcion)+'</p>' +
                        '<h4><a href="'+(urlDetalle)+'">Editar Proveedor'+'</a></h4>' +
                        '<h4><a href="'+(urlDetalle)+'">Eliminar Proveedor'+'</a>'+
                        '</h4>' +
                        '</div></div>'
                        +"</span></li>";

                    $("#uno").click(function(e){
                        console.log("Botón");
                    });
                });
                $("#providersList").html(txt);
            }
            else
            {
                numPage--;
                $(".next").addClass("disabled");

            }
            //console.log(data);
        });

    };

    proveedores();

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
            //console.log(data);
            window.location = "/login";
            //window.location.href = "/catalog"
            //console.log(data.authToken);
            //window.authToken = data.authToken;
        }).error(function(request, status, error)
        {
            //console.log(request);
            //console.log(status);
            //console.log(error);
            //sweetAlert("Error", "No ha sido posible realizar la autenticación!", "error");
            //console.log("Error...");
            window.location = "/login";
        });
    });
});
