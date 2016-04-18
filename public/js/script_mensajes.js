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
        idUsuario = 11,
        idUsuarioDestino = 50,
        nombreUsuario = "Proveedor de Servicios especiales",
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
                    $("#usuarios").append(baseUsuario(item, i));

                });
            }
            else
            {


            }
        });
        return elementos;

    })();

    var baseUsuario = function(data, i)
    {
        var txt ="";

        if(i == 1){
            txt += '<li class="active bounceInDown">';
        } else {
            txt += '<li class="bounceInDown">';
        }

        txt += '<a href="#" class="clearfix">' +
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
    var mensajes = (function elementos()
    {

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
        txt += '<small class="pull-right text-muted"><i class="fa fa-clock-o"></i> 12 mins ago</small>' ;
        txt += '</div>' +
            '<p>'
            +  data.texto +
            '</p>' +
            '</div>' +
            '</li>';
        return txt;

    };
});



