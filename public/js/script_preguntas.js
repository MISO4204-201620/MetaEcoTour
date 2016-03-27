$(function()
{
    //Paginador de comentarios...
    var numPage = 1;
    var paginacion = function(valor)
    {
        console.log(numPage, valor);
        if(numPage + valor >= 1)
        {
            $(".previous").removeClass("disabled");
            numPage += valor;
            preguntas();
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

    if(tipoUser.esProveedor("menuOpc"))
    {
        var user = tipoUser.datosUser();
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        shopping.numCompras(user.data.id, "numCompras");
    }
    else
    {
        window.location = "/login";
    }

    var campos  = ["idCategoria", "nombre", "descripcion", "precioActual", "imagen"],
        idProducto = 0;
    function S_GET(id)
    {
        var a = new RegExp(id+"=([^&#=]*)");
        return decodeURIComponent(window.location.search !== "" ? a.exec(window.location.search)[1] : "0");
    }

    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });

    var cargarIdProducto = function()
    {
        console.log("entra a cargarId ");
        idProducto = Number(S_GET("id"));
        console.log("id producto " + idProducto);
        if(idProducto !== 0)
        {
            //cargar las preguntas
            //datosProducto();

        }
    };



    //Se listan las preguntas de un servicio...
    var preguntas = (function elementos()
    {
        $("#pregunta").html("");
        $("#preguntaText").val("");
        //falta validar la paginacion
        var url = "/api/comentarios/" + idProducto + "/" + numPage + "/PREGUNTA";
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
                    $("#pregunta").append(baseItemPregunta(item, idToken, false));
                    $("#rep_" + idToken).click(function(e)
                    {
                        createForm(this.id.split("_")[1], false, item);
                    });

                    $("#coment_" + idToken).click(function(e)
                    {
                        callcoments(this.id.split("_")[1]);
                        //createForm(this.id.split("_")[1], true, item);
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
            txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "coment_'+(token)+'" data-comentario="'+(data.comentario)+'"  data-id = "'+(data.id)+'"><span class="glyphicon glyphicon-comment"></span> Ver Respuesta </a>';
        } else {
            //txt += '<a class="btn btn-warning btn-circle text-uppercase"><span class="glyphicon glyphicon-comment"></span> El proveedor no ha respondido la Pregunta</a>';
            txt += '<a class="btn btn-info btn-circle text-uppercase" data-toggle="collapse" data-id = "'+(data.id)+'" id = "rep_'+(token)+'"><span class="glyphicon glyphicon-share-alt"></span> Respuesta </a>' ;
        }

        txt += '</div>' +
            '</div>';
        if (data.numeroComentarios > 0){
            txt += '<div class="collapse" id = "reply_'+ (token) +'"> </div>';
        } else {
            txt += '<div class="collapse" id = "new_' + (token) +'"> </div>';
        }


        if(subcomentario){
            txt += '</li></ul>';
        }
        return txt;
    };

    var createForm = function(token, edicion, item)
    {
        //console.log(item);
        var idPregunta = null;
        if (edicion){
            idPregunta = item.id;
        }
        var elemento = '';
        var idComenta = '';
        var comentario = '';
        if (edicion){
            comentario = item.comentario;
            idComenta = $("#coment_" + token).attr("data-id");
            elemento = "#reply_" + token;
        } else {
            idComenta = $("#rep_" + token).attr("data-id");
            elemento = "#new_" + token;
        }


        var txt = '<form action = "#" method = "post" class = "form-horizontal" id = "foreply_'+(token)+'" role="form">'+
            '<div class="form-group"> <label for="email" class="col-sm-2 control-label">Escribe tu Respuesta</label> <div class="col-sm-10">';

        if (edicion){
            txt += '<textarea class="form-control" id = "commentReply_'+(token)+'" rows="5" >' + (comentario) + '</textarea> ';
        } else {
            txt += '<textarea class="form-control" id = "commentReply_'+(token)+'" rows="5"></textarea> ';
        }

            txt += '</div>'+
            '<div class="form-group"> <div class="col-sm-offset-2 col-sm-10">';
        if (edicion){
            txt += '<button class="btn btn-success btn-circle text-uppercase" type="submit" id="submitReply_'+(token)+'"><span class="glyphicon glyphicon-send"></span> Editar Respuesta </button>';
        } else {
            txt += '<button class="btn btn-success btn-circle text-uppercase" type="submit" id="submitReply_'+(token)+'"><span class="glyphicon glyphicon-send"></span> Responder Pregunta</button>';
        }

            txt += '</div> </div> </form>';
        //console.log("elemento");
        //console.log($(elemento));
        $(elemento).html(txt);
        if (!edicion){
            $(elemento).toggle();
        }
        $( "#foreply_" + token ).submit(function( event )
        {
            if($("#commentReply_" + token).val().length !== 0)
            {
                guardaComentarios({id : idPregunta, comentario :  $("#commentReply_" + token).val(), origen : idComenta, tipo : 'PREGUNTA'}, function(error){
                    if(!error)
                    {
                        preguntas();
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

    var guardaComentarios = function(valores, callback)
    {
        var comment = {
            id : valores.id,
            nombreUsuario : window.authToken,
            comentario : valores.comentario,
            fecha : new Date(),
            numeroComentarios : idProducto,
            origen : valores.origen,
            tipo: valores.tipo
        };
        console.log(comment);
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
                    //$(elemento).append(baseItemComentario(item, idToken, true));
                    //$(elemento).append(createForm(idToken, true, item));
                    createForm(token, true, item);

                    //var createForm = function(token, edicion, item)
                    /*
                    $("#rep_" + idToken).click(function(e)
                    {
                        createForm(this.id.split("_")[1]);
                    });

                    $("#coment_" + idToken).click(function(e)
                    {
                        callcoments(this.id.split("_")[1]);
                    });*/

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

    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }


    cargarIdProducto();
    preguntas();





});



