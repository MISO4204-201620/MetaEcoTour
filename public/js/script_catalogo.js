$(function()
{
    var numPage = 1,
        producServicio = [],
        filtro = {name : "", inicial : "", final : "", type : "", provider : "", categoria : "0"},
        precios = {
                    rangoMin : {valor : 0, label : "Min", filtro : "inicial"},
                    rangoMax : {valor : 0, label : "Max", filtro : "final"}
                  };
    //categoria/1
    //Para la moneda...
    function format2(n, currency)
    {
        return currency + " " + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
    }
    //Para los datos del usuario...
    var user = tipoUser.datosUser();
    if(user.existe)
    {
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        //Saber la cantidad de compras que tiene...
        //numCompras
        shopping.numCompras(user.data.id, "numCompras");
        tipoUser.esProveedor("menuOpc");
        tipoUser.esAdministrador("menuOpc");
    }
    else
    {
        $("#usuario").html("Iniciar Sesión").attr("href", "/login").removeAttr("data-toggle");
        $("#opcMenu").remove();
        $("#divCarrito").remove();
    }

    //Para el filtro que se relizará...
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
        filtro.provider = filtro.name = filtro.type = "";
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

    $("#tipoProvider").change(function(e){
        filtro.provider = $(this).val();
        console.log(filtro);
    });

    var cargarProveedores = (function()
    {
        $.getJSON("/api/usuarios/PROVIDER", function(data)
        {
            var options = $("#tipoProvider");
            options.append($("<option />").val("0").text("Proveedores de Servicio"));
            $.each(data, function() {
                options.append($("<option />").val(this.id).text(this.nombre));
            });
        });
    })();

    //Reemplazar por la url de Amazon...
    //var UrlImagen = "http://localhost:8887/maestria/base/img/portada/";
    var baseItem = function(data)
    {
        //debugger;
        //http://localhost:9000/api/recursos/50
        var token = guid();
        var urlDetalle = "/api/recursos/" + data.id + "/0";
        var imagen = data.imagen;
        producServicio.push({token: token, id : data.id, price : data.precioActual, name : data.nombre, img : imagen});
        //var imagen = UrlImagen + "/" + data.imagen;
        var txt = '<div class="col-sm-4 col-lg-4 col-md-4">' +
                  '<div class="thumbnail">' +
                  '<img src = "'+(imagen)+'" alt="">' +
                  '<div class="caption">' +
                  '<h4 class="pull-right">'+format2(data.precioActual, "$")+'</h4>' +
                  '<h4><a href="'+(urlDetalle)+'">'+(data.nombre)+'</a>' +
                  '</h4>' +
                  '<p>'+(data.descripcion)+'</p>' +
                  '<button type="button" class="btn btn-primary text-center shoping" id = "sh_'+(token)+'">' +
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
        $("#catalog").html("<div class = 'thumbnail' align='center'><img src = '/images/loading.gif' border = '0'/></div>");
        $.getJSON("api/categorias/", function(data)
        {
            var total   = 0,
                txt     = "";
            //console.log(data);
            for(var i = 0; i < data.length; i++)
            {
                total += data[i].cantidad;
                txt = "<li class=\"list-group-item\"><a href = \"javascript:;\" id = \"cate_"+(guid())+"\" data-id = \""+(data[i].id)+"\" class=\"cateMarket\">"+(data[i].nombre)+"</a> <span class=\"badge\">"+(data[i].cantidad)+"</span></li>";
                $("#categoriesList").append(txt);
            }
            $("#categoriesList").prepend("<li class=\"list-group-item\"><a href = \"javascript:;\" id = \"cate_"+(guid())+"\" data-id = \"0\" class=\"seleCategoria cateMarket\">Todo</a> <span class=\"badge\">"+(total)+"</span></li>");
            $(".cateMarket").click(function(e)
            {
                var token = e.currentTarget.id;
                var idCategoria = $("#" + token).attr("data-id");
                for(var i = 0; i < $(".cateMarket").size(); i++)
                {
                    if(token === $(".cateMarket")[i].id)
                    {
                        $("#" +  $(".cateMarket")[i].id).addClass("seleCategoria");
                    }
                    else
                    {
                        $("#" +  $(".cateMarket")[i].id).removeClass("seleCategoria");
                    }
                    //console.log($(".cateMarket")[i].id);
                }
                //console.log(idCategoria);
                filtro.categoria = idCategoria;
                elementos();
            });
            elementos();
        });
    })();

    var elementos = function()
    {
        producServicio = [];
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
        //http://localhost:9000/api/productos/numpage/1/name/0name/inicial/-1/final/-1/type/0type/provider/101
        //debugger;
        //categoria/1
        var url = "/api/productos/numpage/"+ numPage;
        url += "/name/" + (filtro.name !== "" ? filtro.name : "0name");
        url += "/inicial/" + (inicial > 0 ? inicial : "-1");
        url += "/final/" + (finaliza > 0 ? finaliza : "-1");
        url += "/type/" + (filtro.type !== "" ? filtro.type : "ALL");
        url += "/provider/" + (filtro.provider !== "" ? filtro.provider : "0");
        url += "/categoria/" + filtro.categoria;
        console.log(url);
        $.getJSON(url, function(data)
        {
            //producServicio = data;
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
                    });
                    console.log(producServicio);
                    $(".shoping").click(function(e){
                        selShopping(e.currentTarget.id.split("_")[1]);
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


    //Para añadir al carrito de compras...
    var selShopping = function(token)
    {
        //console.log(idDiv);
        if(!user.existe)
        {
            swal({
                    title: "Autenticación",
                    text: "Para realizar está acción tienes que estar auténticado, ¿Deseas autenticarte ahora?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Si",
                    cancelButtonText: "No",
                    closeOnConfirm: false
                },
                function()
                {
                    window.location = "/login";
                });
        }
        else
        {
            //Obtener el id del producto...
            //console.log(idDiv);
            var productoSel = (function(){
                for(var i = 0; i < producServicio.length; i++)
                {
                    if(producServicio[i].token === token)
                    {
                        return producServicio[i];
                    }
                }
            })();

            shopping.newCar({productoSel : productoSel, user : user, div : "numCompras"}, function(err, data)
            {
                if(err)
                {
                    sweetAlert("Error", "No ha sido posible realizar la acción", "error");
                }
                else
                {
                    //console.log(data);
                    //producServicio.push({token: token, id : data.id, price : data.precioActual, name : data.nombre, img : imagen});
                    $.meow({
                        title: 'Item Asociado',
                        message: "Se ha asociado " + productoSel.name + ", " + data.cantidad + " veces",
                        icon: productoSel.img
                    });
                }
            });
        }
    };

    //Generar un token único...
    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }

    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });
});
