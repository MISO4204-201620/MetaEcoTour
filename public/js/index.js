$(function()
{
    var baseItem = function(data)
    {
        //debugger;
        var txt = '<div class="col-sm-4 col-lg-4 col-md-4">' +
                  '<div class="thumbnail">' +
                  '<img src="img/portada/'+(data.front)+'" alt="">' +
                  '<div class="caption">' +
                  '<h4 class="pull-right">$'+(data.price)+'</h4>' +
                  '<h4><a href="#">'+(data.name)+'</a>' +
                  '</h4>' +
                  '<p>'+(data.summary)+'</p>' +
                  '</div>' +
                  '<div class="ratings">' +
                  '<p class="pull-right">'+(data.reviews)+'</p>' +
                  '<p>';
        //Para las estrellas...
        for(var i = 1; i <= 5; i++)
        {
            txt += '<span class = "glyphicon ' + (i <= data.rating ? 'glyphicon-star' : 'glyphicon-star-empty' ) + '"></span>';
            //console.log(i);
        }
        txt += '</p></div></div></div>';
        return txt;
    };

    //Para activar el activator:
    //export PATH=/Users/jorgerubiano/Documents/activator/:$PATH
    var elementos = (function elementos()
    {
        $.getJSON( "/api/productos/SER", function(data)
        {
            console.log(data);
            /*
            data.forEach(function(item)
            {
                $("#catalog").append(baseItem(item));
            });
            */
        });
        return elementos;
    })();
});
