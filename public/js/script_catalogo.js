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

    //Para la moneda...
    function format2(n, currency) {
        return currency + " " + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
    }

    //Para el filtro que se relizará...
    var filtro = {
                    name : "",
                    inicial : "",
                    final : "",
                    type : ""
    };

    //Para los rangos de precios...
    var precios = {
                    rangoMin : {valor : 0, label : "Min", filtro : "inicial"},
                    rangoMax : {valor : 0, label : "Max", filtro : "final"}
    };

    var preciosFiltro = function(idSel)
    {
        precios[idSel].valor = Number($("#" + idSel).val());
        $("#div_" + idSel).html(precios[idSel].label + " : " + format2(precios[idSel].valor, "$"));
        filtro[precios[idSel].filtro] = precios[idSel].valor;
    };

    $('input[type=range]').change(function(e)
    {
        preciosFiltro(e.currentTarget.id);
    });

    $("#eraseFilter").click(function(e)
    {
        //Reiniciar los rangos de precios...
        for(var i = 0, elemento = Object.keys(precios); i < elemento.length; i++)
        {
            $("#" + elemento[i]).val(0);
            preciosFiltro(elemento[i]);
        }
        $("#tipoSel").val("ALL");
        filtro.name = filtro.type = "";
        $("#textBusca").val("");
        elementos();
    });

    $("#executeFilter").click(function(e)
    {
        filtro.name = $("#textBusca").val();
        elementos();
        //console.log(filtro);
    });

    $("#tipoSel").change(function(e){
        filtro.type = $(this).val();
        console.log(filtro);
    });

    //Reemplazar por la url de Amazon...
    //var UrlImagen = "http://localhost:8887/maestria/base/img/portada/";
    var baseItem = function(data)
    {
        //debugger;
        //http://localhost:9000/api/recursos/50
        var urlDetalle = "api/recursos/" + data.id + "/0";
        //var imagen = UrlImagen + "/" + data.imagen;
        var imagen = data.imagen;
        var txt = '<div class="col-sm-4 col-lg-4 col-md-4">' +
                  '<div class="thumbnail">' +
                  '<img src = "'+(imagen)+'" alt="">' +
                  '<div class="caption">' +
                  '<h4 class="pull-right">$'+(data.precioActual)+'</h4>' +
                  '<h4><a href="'+(urlDetalle)+'">'+(data.nombre)+'</a>' +
                  '</h4>' +
                  '<p>'+(data.descripcion)+'</p>' +
                  '<button type="button" class="btn btn-primary text-center" id = "uno">' +
                  '<span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span> Añadir al carrito</button>' +
                  '</div>' +
                  '<div class="ratings">' +
                  '<p class="pull-right">'+(data.puntuacion)+'</p>' +
                  '<p>';
        //Para las estrellas...
        for(var i = 1; i <= 5; i++)
        {
            txt += '<span class = "glyphicon ' + (i <= data.puntuacion ? 'glyphicon-star' : 'glyphicon-star-empty' ) + '"></span>';
            //console.log(i);
        }
        txt += '</p></div></div></div>';
        return txt;
    };


    var paginacion = function(valor)
    {
        console.log(numPage, valor);
        if(numPage + valor >= 1)
        {
            $(".previous").removeClass("disabled");
            numPage += valor;
            elementos();
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

    //Traer las categorías...
    var categorias = (function()
    {
        $.getJSON("api/categorias/", function(data)
        {
            var total   = 0,
                txt     = "";
            console.log(data);

            for(var i = 0; i < data.length; i++)
            {
                total += data[i].cantidad;
                txt += "<li class=\"list-group-item\"><a href = \"#\">"+(data[i].nombre)+"</a> <span class=\"badge\">"+(data[i].cantidad)+"</span></li>";
            }
            txt = "<li class=\"list-group-item active\"><a href = \"#\">Todo</a> <span class=\"badge\">"+(total)+"</span></li>" + txt;
            $("#categoriesList").html(txt);
            //Para adicionar el valor de las categorias...
            //http://localhost:9000/api/producto/numpage/1/SER/1
            elementos();
        });
    })();

    var elementos = function()
    {
        $("#catalog").html("<div class = 'thumbnail' align='center'><img src = '/images/loading.gif' border = '0'/></div>");
        //Para validar la paginación...
        //var url = "/api/producto/numpage/" + numPage + "/" + $("#tipoSel").val();
        //var url = "/api/producto/numpage/" + numPage + "/SER";
        var inicial    = filtro.inicial,
            finaliza   = filtro.final;
        if(filtro.inicial > filtro.final)
        {
            finaliza = filtro.inicial;
            inicial = filtro.final;
        }
        //http://localhost:9000/api/productos/numpage/1/name/0name/inicial/-1/final/-1/type/ALL
        //debugger;
        var url = "/api/productos/numpage/"+ numPage;
        url += "/name/" + (filtro.name !== "" ? filtro.name : "0name");
        url += "/inicial/" + (inicial > 0 ? inicial : "-1");
        url += "/final/" + (finaliza > 0 ? finaliza : "-1");
        url += "/type/" + (filtro.type !== "" ? filtro.type : "ALL");
        console.log(url);
        $.getJSON(url, function(data)
        {
            if(data.length !== 0)
            {
                $(".next").show();
                $(".previous").show();
                if(data.errorCode === undefined)
                {
                    $("#catalog").html("");
                    data.forEach(function(item)
                    {
                        $("#catalog").append(baseItem(item));
                        $("#uno").click(function(e){
                            console.log("Botón");
                        });
                    });
                }
                else
                {
                    numPage--;
                    $(".next").addClass("disabled");

                }
            }
            else
            {
                $(".next").hide();
                $(".previous").hide();
                var txtError = "<div align='center'><img src = '/images/error.png' border = '0'/>";
                txtError += "<div>No se ha encontrado información con el filtro establecido</div></div>"
                $("#catalog").html(txtError);
                //error.png
            }
        });
        /*
        $("#catalog").html("");
        $.getJSON("/api/productos/" + $("#tipoSel").val(), function(data)
        {
            //console.log(data);
            data.forEach(function(item)
            {
                $("#catalog").append(baseItem(item));
            });
        });
        return elementos;
        */
    };

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
