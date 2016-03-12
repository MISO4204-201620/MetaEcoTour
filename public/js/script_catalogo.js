$(function()
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
        $.getJSON("/api/productos/SER", function(data)
        {
            console.log(data);
            data.forEach(function(item)
            {
                $("#catalog").append(baseItem(item));
            });
        });
        return elementos;
    })();

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
            console.log(data);
            //window.location.href = "/catalog"
            //console.log(data.authToken);
            //window.authToken = data.authToken;
        }).error(function(request, status, error)
        {
            sweetAlert("Error", "No ha sido posible realizar la autenticación!", "error");
        });
    });
});
