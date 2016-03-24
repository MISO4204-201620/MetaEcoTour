$(function()
{
    //Se obtiene el token de validación del usuario...
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

    //Generar un token único...
    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }

    //Paginador de comentarios...
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

    //Se listan los comentarios de un servicio...
    var comentarios = (function elementos()
    {
        $("#comentario").html("");
        $("#commentText").val("");
        //falta validar la paginacion
        var url = "/api/comentarios/" + idProducto + "/" + numPage;
        //console.log(url);
        $.getJSON(url, function(data)
        {
            if(data.errorCode === undefined)
            {
                if (data.length < 10)
                {
                    $(".next").addClass("disabled");
                }
                data.forEach(function(item)
                {
                    //console.log(item, i);
                    var idToken = guid();
                    $("#comentario").append(baseItemComentario(item, idToken, false));
                    $("#rep_" + idToken).click(function(e)
                    {
                        createForm(this.id.split("_")[1]);
                    });

                    $("#coment_" + idToken).click(function(e)
                    {
                        callcoments(this.id.split("_")[1]);
                    });
                });
            }
            else
            {
                numPage--;
                $(".next").addClass("disabled");

            }
        });
        return elementos;
    })();

    var baseItemComentario = function(data, token, subcomentario)
    {
        //onclick="createForm('+data.id +')" href="#new'+data.id +'"
        //onclick="callcoments('+data.id +')" href="#reply'+data.id +'"
        var date = new Date(data.fecha);
        var month = date.getMonth() + 1;

        var txt ="";

        if(subcomentario){
            txt = '<ul class="media-list">' +
                '<li class="media media-replied">';
        }

        txt +=   '<a class="pull-left" href="#">' +
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
            '<a class="btn btn-info btn-circle text-uppercase" data-toggle="collapse" data-id = "'+(data.id)+'" id = "rep_'+(token)+'"><span class="glyphicon glyphicon-share-alt"></span> Respuesta </a>' ;
        if (data.numeroComentarios > 0)
        {
            txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "coment_'+(token)+'" data-id = "'+(data.id)+'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + ' Comentario(s)</a>';
        }

        txt += '</div>' +
            '</div>';
        if (data.numeroComentarios > 0){
            txt += '<div class="collapse" id = "reply_'+ (token) +'"> </div>';
        }
        txt += '<div class="collapse" id = "new_' + (token) +'"> </div>';

        if(subcomentario){
            txt += '</li></ul>';
        }
        return txt;
    };

    var user = JSON.parse(localStorage.getItem("user")) || {};
    console.log(user);
    $('#example-movie').barrating('show', {
        theme: 'bars-movie',
        onSelect: function(value, text, event)
        {
            if (typeof(event) !== 'undefined')
            {
                // rating was selected by a user
                var puntuaUser = Number(value);
                console.log(puntuaUser);
            }
        }
    });

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


    var guardaComentarios = function(valores, callback)
    {
        var comment = {
            id : null,
            nombreUsuario : window.authToken,
            comentario : valores.comentario,
            fecha : new Date(),
            numeroComentarios : idProducto,
            origen : valores.origen
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
                    callback(false);
                }
                else
                {
                    sweetAlert("Error", data.desCode, "error");
                    callback(true);
                }
            }).error(function(request, status, error)
            {
                sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
                callback(true);
            });
    };

    //Evento para CONSTRUCCION COMENTARIO
    $( "#commentForm" ).submit(function( event )
    {
        if($("#commentText").val().length !== 0)
        {
            guardaComentarios({comentario :  $("#commentText").val(), origen : 0}, function(error){
                if(!error)
                {
                    comentarios();
                }
            });
        }
        else
        {
            sweetAlert("Error", "Por favor escribe tu comentario", "error");
        }
        event.preventDefault();
    });


    var createForm = function(token)
    {
        var elemento = "#new_" + token;
        var idComenta = $("#rep_" + token).attr("data-id");

        var txt = '<form action = "#" method = "post" class = "form-horizontal" id = "foreply_'+(token)+'" role="form">'+
            '<div class="form-group"> <label for="email" class="col-sm-2 control-label">Comentario</label> <div class="col-sm-10">'+
            '<textarea class="form-control" id = "commentReply_'+(token)+'" rows="5"></textarea> ' +
            '</div>'+
            '<div class="form-group"> <div class="col-sm-offset-2 col-sm-10">'+
            '<button class="btn btn-success btn-circle text-uppercase" type="submit" id="submitReply_'+(token)+'"><span class="glyphicon glyphicon-send"></span> Responder Comentario</button>'+
            '</div> </div> </form>';
        //console.log(txt);
        $(elemento).html(txt);
        $(elemento).toggle();
        $( "#foreply_" + token ).submit(function( event )
        {
            if($("#commentReply_" + token).val().length !== 0)
            {
                guardaComentarios({comentario :  $("#commentReply_" + token).val(), origen : idComenta}, function(error){
                    if(!error)
                    {
                        comentarios();
                    }
                });
            }
            else
            {
                $("#commentReply_" + token).focus();
                sweetAlert("Error", "Por favor escribe tu respuesta al comentario", "error");
            }
            event.preventDefault();
        });
    };


// Eventos para llamar los subcomentarios
    var callcoments = function(token)
    {
        console.log("callcoments" );
        console.log("token" + token );
        var elemento = "#reply_" + token;
        var idComenta = $("#coment_" + token).attr("data-id");
        console.log("idComenta" + idComenta);
        $(elemento).empty();
        var url = "/api/subcomentario/" + idComenta ;
        $.getJSON(url, function(data)
        {
            if(data.errorCode === undefined)
            {
                data.forEach(function(item)
                {

                    var idToken = guid();
                    $(elemento).append(baseItemComentario(item, idToken, true));
                    $("#rep_" + idToken).click(function(e)
                    {
                        createForm(this.id.split("_")[1]);
                    });

                    $("#coment_" + idToken).click(function(e)
                    {
                        callcoments(this.id.split("_")[1]);
                    });

                });
                $(elemento).toggle();
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

});



