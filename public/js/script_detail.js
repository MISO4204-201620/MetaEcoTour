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
    var TIENE_MENSAJERIA            = false,
        CALIFICACIONES_VARUABILIDAD = {consultar : false, comentar : false},
        valorCalifica               = 0;


    var listaPuntuaciones = function(data)
    {
        var txtPuntua = "";
        console.info("Los comentarios");
        console.log(data);
        for(var i = 0; i < data.length; i++)
        {
            txtPuntua += "<div class=\"panel panel-default\">" +
                         "<div class=\"panel-body\"><h4><b>"+(data[i].nombre)+"</b></h4> "+(data[i].fecha) + "<p>";
            for(var c = 1; c <= 5; c++)
            {
                txtPuntua += '<span class = "glyphicon ' + (c <= data[i].valor ? 'glyphicon-star' : 'glyphicon-star-empty' ) + '"></span>';
            }
            txtPuntua += "<p>"+(data[i].comentario)+"</p></div></div>";
        }
        $("#vepuntuacion").html(txtPuntua);
    };

    var puntuacionesProducto = function()
    {
        $.getJSON("/api/calificacion/prd/" + idProducto, function(data)
        {
            //console.info("Para traer las calificaciones");
            //console.log(data);
            if(data.length !== 0)
            {
                var traeNombre = function(ind)
                {
                    $.getJSON("/api/usuarios/userId/"+(data[ind].idUsuario)+"/1", function(persona)
                    {
                        data[ind]["correo"] = persona.correo;
                        data[ind]["nombre"] = persona.nombre;
                        ind++;
                        if(ind < data.length)
                        {
                            traeNombre(ind);
                        }
                        else
                        {
                            listaPuntuaciones(data);
                        }
                    }).
                    error(function()
                    {
                        data[ind]["nombre"] = "";
                        ind++;
                        if(ind < data.length)
                        {
                            traeNombre(ind);
                        }
                        else
                        {
                            listaPuntuaciones(data);
                        }
                    });
                };
                traeNombre(0);
            }
            else
            {
                $("#vepuntuacion").html("<div align='center'>No hay puntuaciones de este producto</div>");
            }

            //http://localhost:9000/api/usuarios/userId/51/1
        });
    };

    var calificaciones = function(valor)
    {
        //console.log(user);
        valorCalifica = valor;
        $('#example-movie').barrating('show',
            {
                theme: 'bars-movie',
                onSelect: function(value, text, event)
                {
                    if (typeof(event) !== 'undefined')
                    {
                        valorCalifica = Number(value);
                    }
                }
            });
        $('#example-movie').barrating('set', valor);
    };

    $.getJSON("/api/services/", function(data)
    {
        console.log("Variabilidad");
        if(data[4].present)
        {
            //Inyectar el script de mesa de ayuda...
            $.getScript("//virtualnet2.umb.edu.co/chatDemo/embeb/?t=abe74da7bc3f303ee1f98eaaa689cbf64270aa6e", function (data, textStatus, jqxhr)
            {
                console.log(jqxhr.status); // 200
                console.log("Mesa de ayuda cargada...");
            });
        }
        //Saber si tiene opción de redes sociales...
        console.log(data[1]);
        if(data[1].present)
        {
            console.log("Tiene redes sociales");
            var txtRed = "<div class=\"a2a_kit a2a_kit_size_32 a2a_default_style\" style=\"margin-left: 30%;\">"+
                         "<a class=\"a2a_dd\" href=\"https://www.addtoany.com/share\"></a>" +
                         "<a class=\"a2a_button_facebook\"></a>" +
                         "<a class=\"a2a_button_twitter\"></a>" +
                         "<a class=\"a2a_button_google_plus\"></a>" +
                         "<script type=\"text/javascript\" src=\"//static.addtoany.com/menu/page.js\"></script>";
            $("#sharebtn").html(txtRed);
        }
        else
        {
            $("#sharebtn").remove();
        }
        //console.log("Es un paquete, por lo que se deben traer los servicios asociados al paquete");
        $.getJSON("/api/itemServ/pqt/" + idProducto, function(data)
        {
            console.log("LOS SERVICIOS");
            var items = "";
            for(var i = 0; i < data.length; i++)
            {
                items += baseItemServicio(data[i]);
                //$("#servicios").append(baseItemServicio(data[i]));
            }
            $("#servicios").html(items);
        });

        CALIFICACIONES_VARUABILIDAD.consultar = data[2].childs[0].present;
        CALIFICACIONES_VARUABILIDAD.comentar = data[2].childs[1].present;
        //Se debe mostrar la opción...
        if(CALIFICACIONES_VARUABILIDAD.consultar)
        {
            $("#tabs").append("<li><a href=\"#vepuntuacion\" data-toggle=\"tab\">Puntuaciones</a></li>");
            $("#my-tab-content").append("<div class=\"tab-pane\" id=\"vepuntuacion\"></div>");
            //Llamar al servicio que trae las puntuaciones...
            puntuacionesProducto();
        }
        if(CALIFICACIONES_VARUABILIDAD.comentar)
        {
            $("#executeCalifica").click(function (e)
            {
                if(user.existe)
                {
                    $.getJSON("/api/calificacion/usr/" + user.data.id, function (data) {
                        //console.log(data);
                        //Saber si el usuario actual ya ha realizado el proceso de calificación...
                        var valUsuario = 0;
                        for (var i = 0; i < data.length; i++) {
                            if (Number(data[i].idProducto) === idProducto) {
                                valUsuario = Number(data[i].valor);
                                break;
                            }
                        }
                        $('#myReview').modal('show');
                        calificaciones(valUsuario);
                    });
                }
                else
                {
                    sweetAlert("Error", "No te encuestras auténticado", "error");
                }
            }).fadeIn("fast");

            $("#saveCalifica").click(function(e)
            {
                if(valorCalifica !== 0)
                {
                    var calificacion = {
                        idUsuario: user.data.id,
                        idProducto: idProducto,
                        fecha: "2016-03-16T00:00:00",
                        valor: valorCalifica,
                        comentario: $("#comenPuntua").val()
                    };
                    console.log(calificacion);
                    if (user.existe) {
                        $.ajax({
                            url: "/api/calificacion/",
                            type: "POST",
                            data: JSON.stringify(calificacion),
                            dataType: "json",
                            contentType: "application/json; charset=utf-8"
                        }).done(function (data)
                        {
                            $("#comenPuntua").val()
                            if(data.errorCode === undefined)
                            {
                                console.log(data);
                                if(CALIFICACIONES_VARUABILIDAD.consultar)
                                {
                                    puntuacionesProducto();
                                }
                            }
                            else
                            {
                                sweetAlert("Error", data.desCode, "error");
                            }
                        }).error(function (request, status, error)
                        {
                            sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
                        });
                    }
                    else
                    {
                        sweetAlert("Error", "No te encuentras auténticad@", "error");
                    }
                    $('#myReview').modal('hide');
                }
                else
                {
                    sweetAlert("Error", "Selecciona la calificación que deseas dar al producto", "error");
                }
            });
        }
        else
        {
            $("#myReview").remove();
            $("#executeCalifica").remove();
        }
        if(!CALIFICACIONES_VARUABILIDAD.consultar && !CALIFICACIONES_VARUABILIDAD.comentar)
        {
            $("#calProducto").remove();
            $("#sharebtn").remove();
        }
        //Para saber si tiene la opción de mensajería...
        TIENE_MENSAJERIA = data[5].present;
        comentarios();
    });

    var user = tipoUser.datosUser();

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




    if(user.existe)
    {
        $("#usuario").html(user.data.nombre + " <b class=\"caret\"></b>");
        //Saber la cantidad de compras que tiene...
        //numCompras
        shopping.numCompras(user.data.id, "numCompras");
        tipoUser.esProveedor("menuOpc");
        tipoUser.esAdministrador("menuOpc");
    }
    else
    {
        $("#usuario").html("Iniciar Sesión").attr("href", "/login").removeAttr("data-toggle");
        $("#opcMenu").remove();
        $("#divCarrito").remove();
    }

    //Para guardar la acción de almacenamiento de la consulta...
    var guardaConsulta = (function()
    {
        //console.log(idProducto);
        var date = new Date();
        var fechaBusqueda = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        fechaBusqueda += "T00:00:00"; //+ date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        var consulta = {
                            idProducto      : idProducto,
                            fechaBusqueda   : fechaBusqueda,
                            tipoBusqueda    : "CONSULTA"
                        };
        console.log(consulta);
        $.ajax({
            url 		: "/api/busqueda/",
            type 		: "POST",
            data 		: JSON.stringify(consulta),
            dataType 	: "json",
            contentType: "application/json; charset=utf-8"
        }).done(function(data)
        {
            console.log(data);
        });
    })();

    function format2(n, currency)
    {
        return currency + " " + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
    }

    var baseItemServicio = function(data)
    {
        var urlDetalle = "/api/recursos/" + data.id + "/0";
        console.log(urlDetalle);
        var imagen = data.imagen;
        var txt = '<div class="col-sm-4 col-lg-4 col-md-4">' +
            '<div class="thumbnail">' +
            '<img src = "'+(imagen)+'" alt="">' +
            '<div class="caption">' +
            '<h4 class="pull-right">'+format2(data.precioActual, "$")+'</h4>' +
            '<h4><a href="'+(urlDetalle)+'">'+(data.nombre)+'</a>' +
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
        }
        txt += '</p></div></div></div>';
        return txt;
    };

    //Para traer los atributos de paquete o servicio
    var listadoAtributos = (function()
    {
        $.getJSON("/api/producto/atr/" + idProducto, function(data)
        {
            //console.log("Listado de atribiutos");
            var txtAttr = "<div align='center'>No se han encontrado atributos</div>";
            if(data.length !== 0)
            {
                var txtAttr = "<ul>";
                for(var i = 0; i < data.length; i++)
                {
                    txtAttr += "<li><b>" + data[i].nombre + ": </b>" + data[i].valor + "</li>";
                }
                txtAttr += "</ul>";
            }
            $("#atributesList").html(txtAttr);
            if(tipoProducto === "PAQ")
            {
                $("#tabs").append("<li><a href=\"#servicios\" data-toggle=\"tab\">Servicios del Paquete</a></li>");
                $("#my-tab-content").append("<div class=\"tab-pane\" id=\"servicios\"></div>");
                //console.log("Es un paquete, por lo que se deben traer los servicios asociados al paquete");
                $.getJSON("/api/itemServ/pqt/" + idProducto, function(data)
                {
                    console.log("LOS SERVICIOS");
                    var items = "";
                    for(var i = 0; i < data.length; i++)
                    {
                        items += baseItemServicio(data[i]);
                        //$("#servicios").append(baseItemServicio(data[i]));
                    }
                    $("#servicios").html(items);
                });
            }
        });
    })();

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

    var idDestino = 0;
    var muestraPopup = function(e)
    {
        var token   = e.currentTarget.id.split("_")[1],
            nombre  = $("#nom_" + token).html();
        //Buscar el id del destino...
        for(var i = 0; i < preguntasProdcuto.length; i++)
        {
            if(preguntasProdcuto[i].token === token)
            {
                //console.log(preguntasProdcuto[i]);
                idDestino = preguntasProdcuto[i].idUsuario;
                break;
            }
        }
        console.log("El id de destino es: ", idDestino);
        $("#textTitle").html("<b>Enviar mensaje a: " + nombre + "</b>");
        $("#mensaje").attr("placeholder", "Escibe tú mensaje para " + nombre).val("");
        $('#myModal').modal('show');
    };

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
    var preguntasProdcuto = [];
    var comentarios = function()
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
                    //console.log(item);
                    var idToken = guid();
                    preguntasProdcuto.push(item);
                    preguntasProdcuto[preguntasProdcuto.length - 1].token = idToken;
                    $("#comentario").append(baseItemComentario(item, idToken, false, false));
                    $("#rep_" + idToken).click(function(e)
                    {
                        createForm(this.id.split("_")[1]);
                    });

                    $("#coment_" + idToken).click(function(e)
                    {
                        callcoments(this.id.split("_")[1]);
                    });

                    $("#msg_" + idToken).click(function(e)
                    {
                        muestraPopup(e);
                    });
                });
                console.info("DATOS DE COMENTARIOS");
                console.log(preguntasProdcuto);
            }
            else
            {
                numPage--;
                $(".next").addClass("disabled");

            }
        });
    };

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
            '<h4 class="media-heading text-uppercase reviews" id = "nom_'+(token)+'">' + data.nombreUsuario + '</h4>' +
            '<ul class="media-date text-uppercase reviews list-inline">' +
            '<li class="dd">' + date.getDate() +'</li>' +
            '<li class="mm">' + month + '</li>' +
            '<li class="aaaa">' + date.getFullYear() +'</li>' +
            '</ul>' +
            '<p class="media-comment">' + data.comentario +
            '</p>';
        if (!pregunta)
        {
            txt += '<a class="btn btn-info btn-circle text-uppercase" data-toggle="collapse" data-id = "'+(data.id)+'" id = "rep_'+(token)+'"><span class="glyphicon glyphicon-share-alt"></span> Respuesta </a>';
        }
        if(TIENE_MENSAJERIA)
        {
            txt += '<a class="btn btn-info btn-circle text-uppercase" data-id = "' + (data.id) + '" id = "msg_' + (token) + '"><span class="glyphicon glyphicon-envelope"></span> Enviar Mensaje </a>';
        }
        if (data.numeroComentarios > 0)
        {
            txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "coment_'+(token)+'" data-id = "'+(data.id)+'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + ' Comentario(s)</a>';
        }

        //txt += '<a class="btn btn-warning btn-circle text-uppercase" data-toggle="collapse" id = "coment_'+(token)+'" data-id = "'+(data.id)+'"><span class="glyphicon glyphicon-comment"></span>' + data.numeroComentarios + ' Comentario(s)</a>';



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
    //var user = tipoUser.datosUser();

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

    //Para guadar el mensaje...
    $("#newMensaje").click(function(e)
    {
        if($("#mensajeria").val().length !== 0)
        {
            var comment = {
                comentario : $("#mensajeria").val(),
                fecha : new Date(),
                tipo: "MENSAJE",
                usuarioDestino: idDestino,
                id : null,
                nombreUsuario : window.authToken
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
                    console.log(data);
                    $('#myModal').modal('hide');
                    /*
                    if(data.errorCode === undefined)
                    {
                        callback(false);
                    }
                    else
                    {
                        sweetAlert("Error", data.desCode, "error");
                        callback(true);
                    }
                    */
                }).error(function(request, status, error)
                {
                    //sweetAlert("Error", "No ha sido posible guardar el comentario", "error");
                    $('#myModal').modal('hide');
                    //callback(true);
                });
        }
        else
        {
            sweetAlert("Error", "Por favor escribe tu mensaje", "error");
        }
    });


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
                    $("#msg_" + idToken).click(function(e)
                    {
                        muestraPopup(e);
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



