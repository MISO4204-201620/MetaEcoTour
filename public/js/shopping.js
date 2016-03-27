var shopping = (function()
{
    //Guardar la selección del usuario de la asociación realizada al carrito de compras...
    var guardaItemCompra = function(saveShooping, callback)
    {
        $.ajax(
        {
            url 		: "/api/compras/add/",
            type 		: "POST",
            data 		: JSON.stringify(saveShooping),
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

    var itemsCompra = function(idCompra, callback)
    {
        $.getJSON("/api/compras/itm/" + idCompra, function(data)
        {
            callback(data);
        });
    };

    var asociaCarrito = function(compra, opc, callback)
    {
        var saveShooping = {
            idCompra    : compra.idCompra,
            idProducto  : opc.productoSel.id,
            cantidad    : 1,
            precio      : opc.productoSel.price
        };
        //Se deberá traer los ítems que ya tiene relacioandos a la compra...
        itemsCompra(compra.idCompra, function(items)
        {
            //Buscar si el producto ya había sido asociado...
            var existe = false;
            for(var i = 0; i < items.length; i++)
            {
                if(items[i].idProducto === opc.productoSel.id)
                {
                    saveShooping.cantidad += items[i].cantidad;
                    saveShooping.precio *= saveShooping.cantidad;
                    existe = true;
                    break;
                }
            }
            if(opc.div !== undefined)
            {
                $("#" + opc.div).html(items.length + (!existe ? 1 : 0));
            }
            guardaItemCompra(saveShooping, function(err, data){
                callback(err, data);
            });
        });
    };

    var tieneCarrito = function(idUser, callback)
    {
        $.getJSON("/api/compras/usr/" + idUser, function(data)
        {
            callback(data);
        });
    };

    var crearCompra = function(shoppingCart, callback)
    {
        $.ajax(
        {
            url 		: "/api/compras/save/",
            type 		: "POST",
            data 		: JSON.stringify(shoppingCart),
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


    var newCar = function(opc, callback)
    {
        //Primero, saber si ya tiene un "carrito" asociado al usuario...
        tieneCarrito(opc.user.data.id, function(data)
        {
            if(data.length === 0)
            {
                //No tiene un carrito, por lo que se deberá crear...
                var shoppingCart = {
                    idCompra  :   "",
                    idUsuario :   opc.user.data.id,
                    estado    :   "O",
                    medioPago :   ""

                };
                crearCompra(shoppingCart, function(err, data)
                {
                    if(!err)
                    {
                        asociaCarrito(data, opc, function(err, data){
                            callback(err, data);
                        });
                    }
                    else
                    {
                        callback(true);
                    }
                });
            }
            else
            {
                asociaCarrito(data[0], opc, function(err, data){
                    callback(err, data);
                });
            }
        });
    };

    var numCompras = function(id, div)
    {
        tieneCarrito(id, function(data)
        {
            if(data.length !== 0)
            {
                itemsCompra(data[0].idCompra, function(items)
                {
                    $("#" + div).html(items.length);
                });
            }
            else
            {
                $("#" + div).html("0");
            }
        });
    };
    return {
        newCar : newCar,
        numCompras : numCompras,
        tieneCarrito : tieneCarrito,
        itemsCompra : itemsCompra,
        crearCompra : crearCompra
    };
})();

if (typeof exports !== 'undefined')
{
    for (var i in shopping)
    {
        exports[i] = shopping[i];
    }
}