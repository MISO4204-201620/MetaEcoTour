$(function()
{

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
        idUsuario = tipoUser.datosUser().data.id,
        idUsuarioDestino = 0,
        nombreUsuario = tipoUser.datosUser().data.nombre,
        nombreUsuarioDestino = "Paseos Ecologicos";


    $("#logout").click(function(event)
    {
        tipoUser.logout();
    });

    function guid() {
        function _p8(s) {
            var p = (Math.random().toString(16)+"000000000").substr(2,8);
            return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
        }
        return _p8() + _p8(true) + _p8(true) + _p8();
    }



    //Se listan las preguntas de un servicio...
    var usuarios = (function elementos()
    {
        //console.log("get usuarios, idUsuario: " + idUsuario);
        $("#usuarios").html("");
       //falta validar la paginacion
        var url = "/api/mensajeria/usuarios/" + idUsuario + "/MENSAJE";

        $.getJSON(url, function(data)
        {
            if(data.errorCode === undefined)
            {
                var i = 0;

                data.forEach(function(item)
                {
                    i++;
                    var idToken = guid();

                    $("#usuarios").append(baseUsuario(item, i, idToken));

                    $("#user_" + idToken).click(function(e)
                    {

                        mensajes(this.id.split("_")[1]);
                        //createForm(this.id.split("_")[1], true, item);
                    });


                });
            }
            else
            {


            }
        });
        return elementos;

    })();

    var baseUsuario = function(data, i, token)
    {
        var txt ="";

        if(i == 1){
            txt += '<li id="active_'+(token)+'" class="active bounceInDown">';
            //realiza el llamado a los mensajes del primer contacto
            idUsuarioDestino = data.id;
            nombreUsuarioDestino = data.nombre;
            mensajes(null);
        } else {
            txt += '<li id="active_'+(token)+'" class="bounceInDown">';
        }

        txt += '<a href="#"  id="user_'+(token)+'"  data-id="'+(data.id)+'" class="clearfix">' +
            '<img src="http://bootdey.com/img/Content/user_1.jpg" alt="" class="img-circle">' +
            '<strong>' + data.nombre +'</strong>' +
            '</div>' +
            '<div class="last-message text-muted"></div>' +
            //'<small class="time text-muted">Just now</small>' +
            //'<small class="chat-alert label label-danger">1</small>' +
            '</a>' +
            '</li>';
        return txt;
    };

    //Se listan las preguntas de un servicio...
    var mensajes = (function elementos(token)
    {

        if (token){
            idUsuarioDestino = $("#user_" + token).attr("data-id");
            //<li id="active_'+(token)+'" class="active bounceInDown">
            //eliminar todas las clases active de las clases bounceInDown
            $(".bounceInDown").removeClass("active");
            //adicionar la clase active al componente con id id="active_'+(token)+'"
            $("#active_"+token).addClass("active");

        }
        console.log("get mensajes, idUsuario: " + idUsuario + ", idusuario destino: " + idUsuarioDestino);

        $("#chats").html("");
        //falta validar la paginacion
        var url = "/api/mensajeria/msn/" + idUsuario + "/" + idUsuarioDestino;

        $.getJSON(url, function(data)
        {
            if(data.errorCode === undefined)
            {
                var i = 0;

                data.forEach(function(item)
                {

                    $("#chats").append(baseMensaje(item));

                });
            }
            else
            {


            }
        });
        return elementos;

    })();

    var baseMensaje = function(data)
    {
        var txt ="";
        if(data.idusuario == idUsuario){
            txt += '<li class="right clearfix">'+
                '<span class="chat-img pull-right">'+
                '<img src="http://bootdey.com/img/Content/user_3.jpg" alt="User Avatar">';
            ;
        } else {
            txt += '<li class="left clearfix">'+
                '<span class="chat-img pull-left">' +
                '<img src="http://bootdey.com/img/Content/user_1.jpg" alt="User Avatar">';
        }
        txt += '</span>' +
            '<div class="chat-body clearfix">' +
            '<div class="header">' ;
        if(data.idusuario == idUsuario){
            txt += '<strong class="primary-font">' +nombreUsuario +'</strong>';
        } else {
            txt += '<strong class="primary-font">' +nombreUsuarioDestino +'</strong>';
        }
        var fecha = ajustarFecha(data.fecha);
        txt += '<small class="pull-right text-muted"><i class="fa fa-clock-o"></i>' + fecha+ '</small>' ;
        txt += '</div>' +
            '<p>'
            +  data.texto +
            '</p>' +
            '</div>' +
            '</li>';
        return txt;

    };

    function ajustarFecha(fecha) {

        var date = new Date(fecha);

        var dateActual = new Date();
        var txt = " ";
        if (date.getDate() != dateActual.getDate()){
            txt += date.getDate() + " - "+ (date.getMonth() + 1) + " - " + date.getFullYear();
        } else {

            if (date.getHours() == dateActual.getHours()){
                txt += "hace " + (dateActual.getMinutes()- date.getMinutes()) + " minutos";
            } else {
                txt += "hace " + (dateActual.getHours()- date.getHours()) + " horas";
            }
        }
        return txt;

    }


});



