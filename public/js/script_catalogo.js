$(function()
{
    //Reemplazar por la url de Amazon...
    var UrlImagen = "http://localhost:8887/maestria/base/img/portada/";
    var baseItem = function(data)
    {
        //debugger;
        var imagen = UrlImagen + "/" + data.imagen;
        var txt = '<div class="col-sm-4 col-lg-4 col-md-4">' +
                  '<div class="thumbnail">' +
                  '<img src = "'+(imagen)+'" alt="">' +
                  '<div class="caption">' +
                  '<h4 class="pull-right">$'+(data.precioActual)+'</h4>' +
                  '<h4><a href="#">'+(data.nombre)+'</a>' +
                  '</h4>' +
                  '<p>'+(data.descripcion)+'</p>' +
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

    var elementos = (function elementos()
    {
        $.getJSON( "/api/productos/SER", function(data)
        {
            console.log(data);
            data.forEach(function(item)
            {
                $("#catalog").append(baseItem(item));
            });
        });
        return elementos;
    })();
});
