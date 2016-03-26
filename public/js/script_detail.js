$(function()
{
    //Se obtiene el token de validación del usuario...
    /*
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
    */

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

    //Paginador de preguntas...
    var numPagePreg = 1;
    var paginacionPreg = function(valor)
    {
        console.log(numPagePreg, valor);
        if(numPagePreg + valor >= 1)
        {
            $(".previousPreg").removeClass("disabled");
            numPagePreg += valor;
            preguntas();
        }
        else
        {
            $(".previousPreg").addClass("disabled");
        }
    };

    $(".previous").click(function(e)
    {
        if($(".nextPreg").hasClass("disabled"))
        {
            $(".nextPreg").removeClass("disabled");
        }
        paginacionPreg(-1);
    });

    $(".nextPreg").click(function(e){
        if(!$(".nextPreg").hasClass("disabled")) {
            paginacionPreg(1);
        }

    });


    //Se listan las preguntas de un servicio...
    var preguntas = (function elementos()
    {
        $("#pregunta").html("");
        $("#preguntaText").val("");
        //falta validar la paginacion
        var url = "/api/comentarios/" + idProducto + "/" + numPagePreg + "/PREGUNTA";
        //console.log(url);
        $.getJSON(url, function(data)
        {
            if(data.errorCode === undefined)
            {
                if (data.length < 10)
                {
                    $(".nextPreg").addClass("disabled");
                }
                if (data.length = 10){
                    $(".nextPreg").removeClass("disabled");
                }
                data.forEach(function(item)
                {
                    //console.log(item, i);
                    var idToken = guid();
                    $("#pregunta").append(baseItemPregunta(item, idToken, false));
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
                $(".nextPreg").addClass("disabled");

            }
        });
        return elementos;
    })();

    //Se listan los comentarios de un servicio...
    var comentarios = (function elementos()
    {
        $("#comentario").html("");
        $("#commentText").val("");
        //falta validar la paginacion
        var url = "/api/comentarios/" + idProducto + "/" + numPage + "/COMENTARIO";
        //console.log(url);
        $.getJSON(url, function(data)
        {
            if(data.errorCode === undefined)
            {
                if (data.length < 10)
                {
                    $(".next").addClass("disabled");
                }
                if (data.length = 10)
                {
                    $(".next").removeClass("disabled");
                }
                data.forEach(function(item)
                {
                    //console.log(item, i);
                    var idToken = guid();
                    $("#comentario").append(baseItemComentario(item, idToken, false, false));
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

    var baseItemPregunta = function(data, token, subcomentario)
    {
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
            '</p>';

        if (data.numeroComentarios > 0)
        {
            txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "coment_'+(token)+'" data-id = "'+(data.id)+'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + ' Respuesta</a>';
        } else {
            txt += '<a class="btn btn-warning btn-circle text-uppercase"><span class="glyphicon glyphicon-comment"></span> El proveedor no ha respondido la Pregunta</a>';
        }

        txt += '</div>' +
            '</div>';
        if (data.numeroComentarios > 0){
            txt += '<div class="collapse" id = "reply_'+ (token) +'"> </div>';
        }


        if(subcomentario){
            txt += '</li></ul>';
        }
        return txt;
    };

    var baseItemComentario = function(data, token, subcomentario, pregunta)
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
            '</p>';
        if (!pregunta){
            txt += '<a class="btn btn-info btn-circle text-uppercase" data-toggle="collapse" data-id = "'+(data.id)+'" id = "rep_'+(token)+'"><span class="glyphicon glyphicon-share-alt"></span> Respuesta </a>' ;
        }
        if (data.numeroComentarios > 0)
        {
            txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "coment_'+(token)+'" data-id = "'+(data.id)+'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + ' Comentario(s)</a>';
        }

        txt += '</div>' +
            '</div>';
        if (data.numeroComentarios > 0){
            txt += '<div class="collapse" id = "reply_'+ (token) +'"> </div>';
        }
        if (!pregunta) {
            txt += '<div class="collapse" id = "new_' + (token) + '"> </div>';
        }
        if(subcomentario){
            txt += '</li></ul>';
        }
        return txt;
    };
    var user = tipoUser.datosUser();
    var calificaciones = function(valor)
    {
        //console.log(user);
        $('#example-movie').barrating('show', {
            theme: 'bars-movie',
            onSelect: function(value, text, event)
            {
                if (typeof(event) !== 'undefined')
                {
                    // rating was selected by a user
                    var calificacion = {
                        idUsuario : user.data.id,
                        idProducto : idProducto,
                        fecha : "2016-03-19",
                        valor : Number(value),
                        comentario : "Nada"
                    };
                    console.log(calificacion);
                    if(user.existe)
                    {
                        $.ajax({
                            url 		: "/api/calificacion/",
                            type 		: "POST",
                            data 		: JSON.stringify(calificacion),
                            dataType 	: "json",
                            contentType: "application/json; charset=utf-8"
                        }).done(function(data)
                        {
                            console.log(data);
                            //window.location = "/paqser";
                        }).error(function(request, status, error)
                        {
                            sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
                            //callback(true);
                        });
                    }
                    else
                    {
                        sweetAlert("Error", "No te encuentras auténticad@", "error");
                    }
                }
            }
        });
        $('#example-movie').barrating('set', valor);
    };

    if(user.existe)
    {
        //Traer las puntuaciones realizadas por el usaurio...
        //http://localhost:9000/api/calificacion/usr/2341
        $.getJSON("/api/calificacion/usr/" + user.data.id, function(data)
        {
            console.log(data);
            //Saber si el usuario actual ya ha realizado el proceso de calificación...
            var valUsuario = 0;
            for(var i = 0; i < data.length; i++)
            {
                if(Number(data[i].idProducto) === idProducto)
                {
                    valUsuario = Number(data[i].valor);
                    break;
                }
            }
            calificaciones(valUsuario);
        });
    }

    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });


    var guardaComentarios = function(valores, callback)
    {
        console.log("vamos a almacenar el tipo");
        console.log(valores.tipo);
        var comment = {
            id : null,
            nombreUsuario : window.authToken,
            comentario : valores.comentario,
            fecha : new Date(),
            numeroComentarios : idProducto,
            origen : valores.origen,
            tipo: valores.tipo
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

    //Evento para CONSTRUCCION PREGUNTA
    $( "#preguntaForm" ).submit(function( event )
    {
        if($("#preguntaText").val().length !== 0)
        {
            guardaComentarios({comentario :  $("#preguntaText").val(), origen : 0, tipo : 'PREGUNTA'}, function(error){
                if(!error)
                {
                    preguntas();
                }
            });
        }
        else
        {
            sweetAlert("Error", "Por favor escribe tu comentario", "error");
        }
        event.preventDefault();
    });

    //Evento para CONSTRUCCION COMENTARIO
    $( "#commentForm" ).submit(function( event )
    {
        if($("#commentText").val().length !== 0)
        {
            guardaComentarios({comentario :  $("#commentText").val(), origen : 0, tipo : 'COMENTARIO'}, function(error){
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
                guardaComentarios({comentario :  $("#commentReply_" + token).val(), origen : idComenta, tipo : 'COMENTARIO'}, function(error){
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
                    var pregunta = (item.tipo === 'PREGUNTA' ? true :false);
                    var idToken = guid();
                    $(elemento).append(baseItemComentario(item, idToken, true, pregunta));
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



