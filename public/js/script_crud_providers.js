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
        var url = "/api/usuarios/PROVIDER";

        $.getJSON(url, function(data)
        {
            //console.log(data);
            if(data.errorCode === undefined)
            {
                var tabla = "<table class=\"table table-hover\" id = \"tablaProveedores\"><thead><tr>";
                tabla += "<th>Nombre</th><th>Descripcion</th><th>Correo</th><th>Tipo Documento</th><th>Número Documento</th></tr> </thead><tbody>";
                $("#providers").append(tabla);
                var txt="";
                data.forEach(function(item)
                {
                    var token = guid();
                    var urlDetalle = "api/recursos/" + this.id + "/0";

                    var tr = "<tr id = \"tr_"+(token)+"\" data-id = \""+(item.id)+"\">" +
                        "<td>" + (item.nombre) + "</td>" +
                        "<td>" + (item.descripcion) + "</td>" +
                        "<td>" + (item.correo)+ "</td>" +
                        "<td>" + (item.tipoDoc) + "</td>" +
                        "<td>" + (item.documento) + "</td>" +
                        "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"edit_"+(token)+"\">" +
                        "<span class=\"glyphicon glyphicon-edit\"></span> Editar </button></td>" +
                        "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"del_"+(token)+"\">" +
                        "<span class=\"glyphicon glyphicon-remove\"></span> Eliminar </button></td>" +
                        "</tr>";
                    $("#tablaProveedores").append(tr);
                    $("#del_" + token).click(function(e){
                        var token = this.id.split("_")[1];
                        var id = $("#tr_" + token).attr("data-id");
                        swal({
                            title: "¿Estás segur@?",
                            text: "¿Deseas Eliminar este Proveedor?",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "Si, Eliminar",
                            closeOnConfirm: false
                        }, function ()
                        {
                            $.ajax({
                                url 		: "/api/usuarios/borrar/" + id,
                                type 		: "DELETE",
                                dataType 	: "json",
                                headers     : {"X-AUTH-TOKEN": window.authToken}
                            }).done(function(data)
                            {
                                swal({title: "Eliminado!", text: "Se ha eliminado el proveedor.",   timer: 2000, type : "success" });
                                $("#tr_" + token).fadeOut("fast", function(){
                                    $(this).remove();
                                })

                            }).error(function(request, status, error)
                            {
                                console.log(request, status, error);
                                swal({title: "Error!", text: "No ha sido posible relizar la acción.",   timer: 2000, type : "error" });
                            });
                        });
                    });
                    $("#edit_" + token).click(function() {
                        window.location = "/api/usuarios/userId/" + $("#tr_" + this.id.split("_")[1]).attr("data-id")+"/2";
                    });
             });

            }
            else
            {
                numPage--;
                $(".next").addClass("disabled");

            }
            //console.log(data);
        });

    };

    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }

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

            window.location = "/login";

        }).error(function(request, status, error)
        {

            window.location = "/login";
        });
    });
});
