$(function()
{
    //Primero saber que tipo de reportes, podrá genrerar...
    var REPORTE_VENTAS  = false;
    $.getJSON("api/services/", function(data)
    {
        console.log(data[3].childs);
        var tipos = [{
                        name    : "Reporte de Búsqueda",
                        value   : "BUSQUEDA",
                        existe  : data[3].childs[0].present
                    },
                    {
                        name    : "Reporte de Consultas",
                        value   : "CONSULTA",
                        existe  : data[3].childs[1].present
                    }];
        REPORTE_VENTAS = data[3].childs[2].present;
        //Cargar el combo de tipo de reportes a generar...
        var options = $("#typeReport");
        $.each(tipos, function()
        {
            if(this.existe)
            {
                options.append($("<option />").val(this.value).text(this.name));
            }
        });
        consultaReporte();
    });


    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });
    var txtLoading = "<div align='center'><img src = '/images/loading.gif' border = '0'/></div>";
    var resultadoReporte = [],
        totalPagina      = 10,
        fechas           = {inicia : "", final : ""},
        numPagina        = 0,
        date             = new Date(),
        fechaActual      = (date.getDate() <= 9 ? "0" + date.getDate() : date.getDate())  + "-" + (date.getMonth() + 1 <= 9 ? "0" + Number(date.getMonth() + 1) : date.getMonth() + 1) + "-" + date.getFullYear();

    var generaURL = function(cuenta)
    {
        fechas = {inicia : $("#fechaInicio").val(), final : $("#fechaFinal").val()};
        if(fechas.inicia !== "")
        {
            var parFecha = fechas.inicia.split("-");
            fechas.inicia = parFecha[2] + "-" + parFecha[1] + "-" + parFecha[0];
        }
        else
        {
            fechas.inicia = "01-01-2016";
        }
        if(fechas.final !== "")
        {
            var parFecha = fechas.final.split("-");
            fechas.final = parFecha[2] + "-" + parFecha[1] + "-" + parFecha[0];
        }
        else
        {
            fechas.final = fechaActual;
        }
        var urlConsulta = "/api/busqueda/tipo/";
        urlConsulta += $("#typeReport").val();
        urlConsulta += "/provider/" + user.data.id;
        urlConsulta += "/fechaInicio/" + fechas.inicia;
        urlConsulta += "/fechaFin/" + fechas.final;
        urlConsulta += "/cuenta/" + cuenta;
        return urlConsulta;
    };

    var consultaReporte = function()
    {
        resultadoReporte = [];
        var urlConsulta = generaURL(1);
        //console.log(urlConsulta);
        $("#listado").html(txtLoading);
        $.getJSON(urlConsulta, function(data)
        {
            resultadoReporte = data;
            var numPaginas = Math.ceil(resultadoReporte.length / totalPagina);
            var txtPagina = "";
            for(var i = 1; i <= numPaginas; i++)
            {
                txtPagina += "<li class = 'pagina "+(i === 1 ? "active" : "")+"'><a href = '#'>"+(i)+"</a></li>";
            }
            //console.log(resultadoReporte);
            txtPagina = "<ul class = 'pagination'>" + txtPagina + "</ul>";
            $("#paginador").html(txtPagina);
            $(".pagina > a").click(function()
            {
                //console.log(this.innerHTML);
                var pagSelecciona = Number(this.innerHTML);
                if(pagSelecciona !== numPagina)
                {
                    $(".pagina:eq(" + (pagSelecciona - 1) + ")").addClass("active");
                    $(".pagina:eq(" + (numPagina - 1) + ")").removeClass("active");
                    numPagina = pagSelecciona;
                    muestraDatos(pagSelecciona);
                }
            });
            numPagina = 1;
            muestraDatos(1);
            //Imprimir la data...
        });
    };

    var muestraDatos = function(pagina)
    {
        //console.log("Muestra la página", pagina);
        $("#listado").empty();
        var tabla = "<table class=\"table table-hover\" id = \"tabla\"><thead><tr>";
        tabla += "<th></th><th>Nombre</th><th>Tipo</th><th>Precio</th><th>Categoria</th><th>Puntuación</th><th>Cantidad</th></tr> </thead><tbody>";
        $("#listado").append(tabla);
        var inicio   = (pagina - 1) * totalPagina,
            fin      = pagina * totalPagina > resultadoReporte.length ? resultadoReporte.length : pagina * totalPagina;
        for(var i = inicio; i < fin; i++)
        {
            var producto    = resultadoReporte[i].producto,
                categoria   = resultadoReporte[i].nombreCategoria,
                cantidad    = resultadoReporte[i].totalBusquedas,
                urlDetalle  = "api/recursos/" + producto.id + "/0",
                ratings     = "";
            var tipoRecurso = {SER : "Servicio", PAQ : "Paquete"};
            for(var c = 1; c <= 5; c++)
            {
                ratings += '<span class = "glyphicon ' + (c <= producto.puntuacion ? 'glyphicon-star' : 'glyphicon-star-empty' ) + '"></span>';
            }
            var tr = "<tr id = \"tr_"+(546)+"\" data-id = \""+(producto.id)+"\"><th scope=\"row\"><img src = \""+(producto.imagen)+"\" border = \"0\" class=\"imagenTabla\"></th>" +
                "<td><a href = \""+(urlDetalle)+"\">" + producto.nombre + "</a></td>" +
                "<td style=\"color: #F44336;\"><b>" + tipoRecurso[producto.tipo] + "</b></td>" +
                "<td>" + format2(producto.precioActual, "$") + "</td>" +
                "<td>" + categoria + "</td>" +
                "<td>" + (ratings) + "</td>" +
                "<td><div align='center'>" + (cantidad) + "</div></td>" +
                "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"detail_"+(producto.id)+"\" title = \"Descargar Detalle\">" +
                "<span class=\"glyphicon glyphicon-download-alt\"></span></button></td>";

            //Si tiene el reporte de ventas...
            if(REPORTE_VENTAS)
            {
                tr += "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"user_"+(producto.id)+"\" title = \"Reporte de Ventas\">" +
                      "<span class=\"glyphicon glyphicon-transfer\"></span></button></td>";
            }
            tr += "</tr>";
            $("#tabla").append(tr);
            $("#detail_" + producto.id).click(function()
            {
                var idProducto = Number(this.id.split("_")[1]);
                generaDetalle(idProducto);
            });
            //Para generar un reporte de compras...
            $("#user_" + producto.id).click(function()
            {
                var idProducto = Number(this.id.split("_")[1]);
                generaCompras(idProducto);
            });
        }
    };

    $("#executeFilter").click(function(){
        consultaReporte();
    });

    var buscaProducto = function(id)
    {
        var producto = {};
        for(var i = 0; i < resultadoReporte.length; i++)
        {
            if(id === resultadoReporte[i].producto.id)
            {
                producto = resultadoReporte[i].producto;
                producto.categoria = resultadoReporte[i].nombreCategoria;
                break;
            }
        }
        return producto;
    };

    var generaCompras = function(idProducto)
    {
        var urlConsulta = "/api/rptCompra/prv/"+user.data.id+"/" + idProducto;
            cont        = 1,
            producto    = buscaProducto(idProducto),
            datos       = [],
            estados     = {X : "Cancelado", C : "Cerrado", O : "Abierto", E : "Error"};
        //console.log(idproducto);
        $.getJSON(urlConsulta, function(data)
        {
           // sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
            $.each(data, function()
            {
                //console.log(this);
                datos.push([cont, this[10], this[5], (this[6] !== null ? this[6] : "--"), estados[this[7]], this[8] !== "" ? this[8] : "--"]);
                cont++;
            });
            //console.log("Ingresa a ese punto");
            //console.log(datos);
            PDF.generaPDF({
                header  : ["Producto: " + producto.nombre, "Tipo: " + producto.tipo, "Precio: " + format2(producto.precioActual, "$"), "Categoria: " + producto.categoria],
                columns : ["#", "Usuario", "Fecha Creación", "Fecha Actualiza", "Estado Compra", "Forma de Pago"],
                data    : datos,
                nombre  : "Compras"
            });
        });
    };

    var generaDetalle = function(idProducto)
    {
        var urlConsulta = generaURL(-1),
            cont        = 1,
            producto    = buscaProducto(idProducto),
            datos       = [],
            categoria   = "";
        //console.log(idProducto);
        $.getJSON(urlConsulta, function(data)
        {
            $.each(data, function()
            {
                if(this.producto.id === idProducto)
                {
                    datos.push([cont, this.fechaBusqueda]);
                    cont++;
                }
            });
            //console.log("Ingresa a ese punto");
            //console.log(datos);
            PDF.generaPDF({
                        header  : ["Producto: " + producto.nombre, "Tipo: " + producto.tipo, "Precio: " + format2(producto.precioActual, "$"), "Categoria: " + producto.categoria],
                        columns : ["#", "Fecha Búsqueda"],
                        data    : datos,
                        nombre  : "detalle"
            });
        });
    };

    $("#reportPdf").click(function()
    {
        var data = [];
        for(var i = 0; i < resultadoReporte.length; i++)
        {
            var producto    = resultadoReporte[i].producto,
                categoria   = resultadoReporte[i].nombreCategoria,
                cantidad    = resultadoReporte[i].totalBusquedas;
            data.push([producto.nombre, producto.tipo, format2(producto.precioActual, "$"), categoria, producto.puntuacion, cantidad]);
        }
        PDF.generaPDF({
                    header  : ["Proveedor: " + user.data.nombre, "Tipo: " + $("#typeReport").val(), "Fechas: " + fechas.inicia + " hasta " + fechas.final],
                    columns : ["Nombre", "Tipo", "Precio", "Categoria", "Puntuación", "Cantidad"],
                    data    : data,
                    nombre  : "REPORTE_DE_" + $("#typeReport").val()
        });
    });



    if(tipoUser.esProveedor("menuOpc"))
    {
        $("#listado").html(txtLoading);
        var user = tipoUser.datosUser();
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        //console.log(user.data.id);
        shopping.numCompras(user.data.id, "numCompras");
    }
    else
    {
        window.location = "/login";
    }

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



