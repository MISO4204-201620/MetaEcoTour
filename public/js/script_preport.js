$(function()
{
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
                "<span class=\"glyphicon glyphicon-download-alt\"></span></button></td>" +
                "<td><button type=\"button\" class=\"btn btn-default btn-sm\" id = \"user_"+(producto.id)+"\" title = \"Historial de Compras\">" +
                "<span class=\"glyphicon glyphicon-transfer\"></span></button></td></tr>";
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
            generaPDF({
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
            generaPDF({
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
        generaPDF({
                    header  : ["Proveedor: " + user.data.nombre, "Tipo: " + $("#typeReport").val(), "Fechas: " + fechas.inicia + " hasta " + fechas.final],
                    columns : ["Nombre", "Tipo", "Precio", "Categoria", "Puntuación", "Cantidad"],
                    data    : data,
                    nombre  : "reporte"
        });
    });

    var generaPDF = function(opt)
    {
        var imgData = "data:image/png;base64,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";
        var doc = new jsPDF('p', 'pt');
        doc.addImage(imgData, 'JPEG', 270, 10, 60, 60);
        doc.text(250, 90, 'MetaEcoTour');
        doc.line(40, 105, 555, 105);
        //Antes de la tabla...
        var headerInicia = 130;
        doc.setFontSize(11);
        for(var i = 0; i < opt.header.length; i++)
        {
            doc.text(40, headerInicia, opt.header[i]);
            headerInicia += 15;
        }
        doc.autoTable(opt.columns, opt.data, {margin: {top: headerInicia + 5}, theme: 'grid'});
        doc.text("Reporte generado el día " + fechaActual, 40, doc.autoTableEndPosY() + 30);
        doc.save(opt.nombre + ".pdf");
    };

    if(tipoUser.esProveedor("menuOpc"))
    {
        $("#listado").html(txtLoading);
        var user = tipoUser.datosUser();
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        //console.log(user.data.id);
        shopping.numCompras(user.data.id, "numCompras");

        var tipos = [{
                        name    : "Reporte de Búsqueda",
                        value   : "BUSQUEDA"
                     },
                     {
                         name    : "Reporte de Consultas",
                         value   : "CONSULTA"
                     }];
        //Cargar el combo de tipo de reportes a generar...
        var options = $("#typeReport");
        $.each(tipos, function() {
            options.append($("<option />").val(this.value).text(this.name));
        });
        consultaReporte();
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



