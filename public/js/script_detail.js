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

    //paginador
    var numPage = 1;
    var paginacion = function(valor)
    {
        console.log(numPage, valor);
        if(numPage + valor >= 1)
        {
            $(".previous").removeClass("disabled");
            numPage += valor;
            comentarios();
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
        if(!$(".next").hasClass("disabled")) {
            paginacion(1);
        }

    });

    var comentarios = (function elementos()
    {
        $("#comentario").html("");
        $("#commentText").val("");
        //falta validar la paginacion
        var url = "/api/comentarios/" + idProducto + "/" + numPage;
        console.log(url);
        $.getJSON(url, function(data)
        {

            if(data.errorCode === undefined)
            {

                if (data.length < 10){
                    $(".next").addClass("disabled");
                }
                data.forEach(function(item)
                {
                    $("#comentario").append(baseItemComentario(item));
                });
            }
            else
            {
                numPage--;
                $(".next").addClass("disabled");

            }
            //console.log(data);
        });
        return elementos;

    })();

    var baseItemComentario = function(data)
    {
        var date = new Date(data.fecha);
        var month = date.getMonth()+1;

        var txt =   '<a class="pull-left" href="#">' +
            '<img class="media-object img-circle" src="https://s3.amazonaws.com/fabricas/avatar.jpg" alt="profile">' +
            '</a>' +
            '<div class="media-body">' +
            '<div class="well well-lg">' +
            '<h4 class="media-heading text-uppercase reviews">' + data.nombreUsuario + '</h4>' +
            '<ul class="media-date text-uppercase reviews list-inline">' +
            '<li class="dd">' + date.getDate() +'</li>' +
            '<li class="mm">' + month + '</li>' +
            '<li class="aaaa">' + date.getFullYear() +'</li>' +
            '</ul>' +
            '<p class="media-comment">' + data.comentario +
            '</p>' +
            '<a class="btn btn-info btn-circle text-uppercase" href="#" id="reply"><span class="glyphicon glyphicon-share-alt"></span> Reply</a>' ;
        if (data.numeroComentarios > 0){
            txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" onclick="callcoments('+data.id +')" href="#reply'+data.id +'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + 'comments</a>';
        }

        txt += '</div>' +
            '</div>';
        if (data.numeroComentarios > 0){
            txt += '<div class="collapse" id="reply'+data.id +'"> </div>';
        }
        return txt;
    };

    var user = JSON.parse(localStorage.getItem("user")) || {};
    console.log(user);
    $('#starts').barrating('show',
        {
            theme: 'fontawesome-stars',
            onSelect: function(value, text, event)
            {
                if (typeof(event) !== 'undefined')
                {
                    // rating was selected by a user
                    var puntuaUser = Number(value);
                    console.log(puntuaUser);
                    /*
                    if(Number(putuaVideo) !== puntuaUser)
                    {
                        //Para guardar la puntación...
                        conectaServer(17, puntuaUser, function(data)
                        {
                            if(data.status)
                            {
                                //console.log(data.data);
                                putuaVideo = puntuaUser;
                            }
                            $('#starts').barrating('set', putuaVideo);
                        });
                    }
                    */
                }
                /*
                 else
                 {
                 // rating was selected programmatically
                 // by calling `set` method
                 console.log("Ingresa");
                 }
                 */
            }
        });


    //console.log(servicio);


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

    //Evento para CONSTRUCCION COMENTARIO
    $( "#commentForm" ).submit(function( event )
    {
        var comment = {
            id : null,
            nombreUsuario : window.authToken,
            comentario : $("#commentText").val(),
            fecha : new Date(),
            numeroComentarios : idProducto

        };
        $.ajax(
            {
                url 		: "/api/comentarios/",
                type 		: "POST",
                data 		: JSON.stringify(comment),
                dataType 	: "json",
                contentType: "application/json; charset=utf-8"
            }).done(function(data)
        {
            if(data.errorCode === undefined)
            {
                comentarios();

            }
            else
            {
                sweetAlert("Error", data.desCode, "error");
            }
        }).error(function(request, status, error)
        {
            sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
        });
        event.preventDefault();
    });

});

var baseSubItemComentario = function(data, txt)
{
    var date = new Date(data.fecha);
    var month = date.getMonth()+1;

    txt += '<a class="pull-left" href="#">' +
        '<img class="media-object img-circle" src="https://s3.amazonaws.com/fabricas/avatar.jpg" alt="profile">' +
        '</a>' +
        '<div class="media-body">' +
        '<div class="well well-lg">' +
        '<h4 class="media-heading text-uppercase reviews">' + data.nombreUsuario + '</h4>' +
        '<ul class="media-date text-uppercase reviews list-inline">' +
        '<li class="dd">' + date.getDate() +'</li>' +
        '<li class="mm">' + month + '</li>' +
        '<li class="aaaa">' + date.getFullYear() +'</li>' +
        '</ul>' +
        '<p class="media-comment">' + data.comentario +
        '</p>' +
        '<a class="btn btn-info btn-circle text-uppercase" href="#" id="reply"><span class="glyphicon glyphicon-share-alt"></span> Reply</a>' ;
    if (data.numeroComentarios > 0){
        txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "com_'+(data.id)+'" href="#reply'+data.id +'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + 'comments</a>';
    }

    txt += '</div>' +
        '</div>';
    if (data.numeroComentarios > 0){
        txt += '<div class="collapse" id="reply'+data.id +'"> </div>';
    }
    return txt;
};

// Eventos para llamar los subcomentarios
var callcoments = function(data)
{

    var elemento = "#reply"+data;
    $(elemento).empty();
    var url = "/api/subcomentario/" + data ;

    $.getJSON(url, function(data)
    {

        if(data.errorCode === undefined)
        {
            data.forEach(function(item)
            {
                var txt = '<ul class="media-list">' +
                    '<li class="media media-replied">';
                $(elemento).append(txt);
                $(elemento).append(baseSubItemComentario(item, txt));
                $("#com_" + item.id).click(function(e){
                    console.log(this.id);
                });
                var txtf = '</li></ul>';
                $(elemento).append(txtf);
            });
        }
        else
        {
            numPage--;
            $(".next").addClass("disabled");

        }
        //console.log(data);
    });
    return null;
};

