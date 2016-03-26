$(function()
{
    var listadoRecursosTemp = [],
        listadoCompra       = [],
        compra              = {};
    var user = tipoUser.datosUser();
    $("#listado").html("<div class = 'thumbnail' align='center'><img src = '/images/loading.gif' border = '0'/></div>");
    if(user.existe)
    {
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        shopping.numCompras(user.data.id, "numCompras");
        tipoUser.esProveedor("menuOpc");
         $.getJSON("api/producto/proveedor/" + user.data.id, function(data)
         {
             /*
              Obtener los paquetes y servicios del proveedor de forma temporal para así mostralos
              la idea es que el servicio entregue está información...
              */
             listadoRecursosTemp = data;
             //Mostrar los ítems que se tienen en el carrito de compras...
             console.log(listadoRecursosTemp);
             muestraItems();
         });
    }
    else
    {
        window.location = "/login";
    }

    var muestraItems = function()
    {
        shopping.tieneCarrito(user.data.id, function(data)
        {
            if(data.length !== 0)
            {
                compra = data[0];
                shopping.itemsCompra(compra.idCompra, function(data)
                {
                    imprimeTabla(data);
                });
            }
            else
            {
                //Mostrar que no se ha adicionado nada...
                imprimeTabla([]);
            }
        });
    };

    //Para buscar datos del producto de forma temporal, el servicio debería regresar está información...
    var buscaDatosProductoTmp = function(id)
    {
        var dataTmp = {nombre : "", imagen : "", precio: ""};
        for(var i = 0; i < listadoRecursosTemp.length; i++)
        {
            if(listadoRecursosTemp[i].id === id)
            {
                dataTmp.nombre = listadoRecursosTemp[i].nombre;
                dataTmp.imagen = listadoRecursosTemp[i].imagen;
                dataTmp.precio = listadoRecursosTemp[i].precioActual;
                break;
            }
        }
        return dataTmp;
    };

    var buscarItemCarrito = function(token)
    {
        var item = {existe : false, data : {}};
        for(var i = 0; i < listadoCompra.length; i++)
        {
            if(listadoCompra[i].token === token)
            {
                item.existe = true;
                item.indice = i;
                item.data = listadoCompra[i];
                break;
            }
        }
        return item;
    };

    var actualizaTotalesVista = function(item, type, data)
    {
        if(type === "add")
        {
            listadoCompra[item.indice].totalValor = data.precio;
            listadoCompra[item.indice].cantidad = data.cantidad;
            $("#price_" + item.data.token).html(format2(data.precio, "$"));
        }
        else if(type === "remove")
        {
            listadoCompra.splice(item.indice, 1);
            if(listadoCompra.length === 0)
            {
                imprimeTabla([]);
            }
        }
        var totales = sumatoriaTotales();
        $("#totalPrice").html(format2(totales.valor, "$"));
        $("#totalCantidad").html(totales.cantidad);
    };

    var accionesItemCarrito = function(item, type, cantidad)
    {
        if(item.existe)
        {
            var actualiza = {
                                idCompra    : compra.idCompra,
                                idProducto  : item.data.idProducto,
                                cantidad    : cantidad,
                                precio      : item.data.precio * cantidad
            };
            if (type === "remove")
            {
                swal({
                    title: "¿Estás segur@?",
                    text: "¿Deseas Eliminar este Paquete/servicio de tu carrito de compras?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Si, Eliminar",
                    closeOnConfirm: false
                }, function ()
                {
                    actualizaEliminaItem(actualiza, type, function (err, data)
                    {
                        if(!err)
                        {
                            swal({title: "Eliminado!", text: "Se ha eliminado el paquete/servicio de tu carrito de compras",   timer: 2000, type : "success" });
                            $("#tr_" + item.data.token).fadeOut("fast", function()
                            {
                                $(this).remove();
                                actualizaTotalesVista(item, type, data);
                                shopping.numCompras(user.data.id, "numCompras");
                            });
                        }
                        else
                        {
                            swal({title: "Error!", text: "No ha sido posible relizar la acción.",   timer: 2000, type : "error" });
                        }
                    });
                });
            }
            else
            {
                actualizaEliminaItem(actualiza, type, function (err, data)
                {
                    if(!err)
                    {
                        actualizaTotalesVista(item, type, data);
                    }
                    else
                    {
                        swal({title: "Error!", text: "No ha sido posible relizar la acción.",   timer: 2000, type : "error" });
                    }
                });
            }
        }
        else
        {
            sweetAlert("Error", "El item no existe", "error");
        }
    };

    var actualizaEliminaItem = function(item, type, callback)
    {
        $.ajax({
            url 		: "/api/compras/" + type + "/",
            type 		: "POST",
            data 		: JSON.stringify(item),
            dataType 	: "json",
            contentType: "application/json; charset=utf-8"
        }).done(function(data)
        {
            callback(false, data);
        }).error(function(request, status, error)
        {
            callback(true);
        });
    };

    //Para calcular la sumatoria de los totales de valor y cantidad que se tiene en el momento...
    var sumatoriaTotales = function()
    {
        var totales = {valor : 0, cantidad : 0};
        for(var i = 0; i < listadoCompra.length; i++)
        {
            totales.valor += listadoCompra[i].totalValor;
            totales.cantidad += listadoCompra[i].cantidad;
        }
        return totales;
    };

    var imprimeTabla = function(data)
    {
        //Mostrar la información...
        $("#listado").empty();
        if(data.length !== 0)
        {
            var tabla = "<table class=\"table table-hover\" id = \"tabla\"><thead><tr>";
            tabla += "<th></th><th>Paquete/Servicio</th><th>Precio</th><th>Cantidad</th></tr> </thead><tbody>";
            $("#listado").append(tabla);
            //var sumatoriaPrecio = 0;
            //var totalCantidad = 0;
            $.each(data, function()
            {
                var token = guid(),
                    urlDetalle = "api/recursos/" + this.idProducto + "/0",
                    datosTMP = buscaDatosProductoTmp(this.idProducto);

                //Guardar los datos de la compra...
                listadoCompra.push({
                    token       : token,
                    idProducto  : this.idProducto,
                    precio      : datosTMP.precio,
                    cantidad    : this.cantidad,
                    totalValor  : this.precio
                });
                var tr = "<tr id = \"tr_"+(token)+"\"><th scope=\"row\"><img src = \""+(datosTMP.imagen)+"\" border = \"0\" class=\"imagenTabla\"></th>" +
                         "<td><a href = \""+(urlDetalle)+"\">" + datosTMP.nombre + "</a></td>" +
                         "<td><div align=\"center\" id = \"price_"+token+"\">" + format2(this.precio, "$") + "</div></td>" +
                         "<td><div align=\"center\"><input type=\"number\" value = \"" + (this.cantidad) + "\" class = \"cantidad\" min=\"1\" id = \"cant_"+token+"\"></div></td>" +
                         "<td><button type=\"button\" class=\"btn btn-default btn-sm delete\" id = \"del_"+(token)+"\">" +
                         "<span class=\"glyphicon glyphicon-remove\"></span> Eliminar </button></td></tr>";
                $("#tabla").append(tr);
                //sumatoriaPrecio += this.precio;
                //totalCantidad += this.cantidad;
            });
            //Para eliminar...
            $(".delete").click(function(e)
            {
                accionesItemCarrito(buscarItemCarrito(e.currentTarget.id.split("_")[1]), "remove", 0);
            });

            $(".cantidad").change(function(e)
            {
                var idInput = e.currentTarget.id;
                accionesItemCarrito(buscarItemCarrito(idInput.split("_")[1]), "add", Number($("#" + idInput).val()));
            });
            //Para la sumatoria de totales...
            var totales = sumatoriaTotales();

            var base = "<tr><td colspan = \"5\"><hr></td></tr>" +
                       "<tr><th scope=\"row\">&nbsp;</th>" +
                       "<td>&nbsp;</td>" +
                       "<td><div align=\"center\" id = \"totalPrice\">" + format2(totales.valor, "$") + "</div></td>" +
                       "<td><div align=\"center\" id = \"totalCantidad\">"+(totales.cantidad)+"</div></td>" +
                       "<td>&nbsp;</td></tr>";
            $("#tabla").append(base);
            var btns = "<hr><div class=\"col-md-12\" align=\"center\">" +
                       "<button type=\"button\" class=\"btn btn-info btn-lg\" id = \"del_\">" +
                       "<span class=\"glyphicon glyphicon-ok\"></span> Pagar Servicio</button>" +
                       "<button type=\"button\" class=\"btn btn-warning btn-lg\" id = \"del_\">" +
                       "<span class=\"glyphicon glyphicon-remove\"></span> Cancelar Servicio</button>" +
                       "</div>";
            $(".container > .row").append(btns);
        }
        else
        {
            var txtError = "<div align='center'><img src = '/images/error.png' border = '0'/>";
            txtError += "<div>No se ha encontrado información</div></div>"
            $("#listado").html(txtError);
        }
    };


    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });

    function format2(n, currency)
    {
        return currency + " " + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
    }

    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }

});



